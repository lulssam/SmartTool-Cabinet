package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.ferramentas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
/**
 * Este é o cérebro do ecrã das ferramentas.
 * É ele que vai à internet pedir as informações, guarda os dados todos, e decide o que o ecrã deve mostrar a cada momento.
 *
 * @param ferramentasDataSource O serviço que liga a nossa aplicação ao servidor para ir buscar ou guardar dados.
 * @param idTecnico O número que identifica quem é o utilizador (técnico) que tem o telemóvel na mão.
 */
class FerramentasViewModel(
    private val ferramentasDataSource: FerramentaRemoteDataSource,
    private val idTecnico: Int,
) : ViewModel() {

    private val _state = MutableStateFlow(FerramentasUiState())
    val state: StateFlow<FerramentasUiState> = _state.asStateFlow()

    init {
        carregar()
    }
    /**
     * Pede as informações mais recentes das ferramentas ao servidor e avisa o ecrã quando elas chegarem.
     * Se o técnico não estiver reconhecido no sistema, dá um erro.
     */
    fun carregar() {
        if (idTecnico == -1) {
            _state.update { it.copy(isLoading = false, error = "Sessão inválida") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val rTodas = ferramentasDataSource.getFerramentas()
            val rMinhas = ferramentasDataSource.getFerramentaTecnico(idTecnico)
            val rReservadas = ferramentasDataSource.getReservadasTecnico(idTecnico)

            if (rTodas is ApiResult.Success && rMinhas is ApiResult.Success) {
                val minhasIds = rMinhas.data.map { it.idFerramenta }.toSet()
                val reservadasIds =
                    (rReservadas as? ApiResult.Success)?.data?.map { it.idFerramenta }?.toSet()
                        ?: emptySet()

                val listaMapeada = rTodas.data.map { dto ->
                    val isMinha = dto.idFerramenta in minhasIds
                    val isDisponivel = dto.disponibilidade.equals("Disponivel", ignoreCase = true)
                    val isReservadaParaMim =
                        dto.idFerramenta in reservadasIds && dto.disponibilidade.equals(
                            "Reservada",
                            ignoreCase = true
                        )
                    val isDanificada = dto.estado.equals("Danificada", ignoreCase = true) ||
                            dto.disponibilidade.equals("Em Manutencao", ignoreCase = true)

                    val estadoVis = when {
                        isDisponivel -> EstadoFerramentaLista.DISPONIVEL
                        isDanificada -> EstadoFerramentaLista.MANUTENCAO
                        isReservadaParaMim -> EstadoFerramentaLista.RESERVADA
                        else -> EstadoFerramentaLista.EM_USO
                    }

                    val codigoTipoCalculado = dto.idFerramenta / 100000
                    val nFerramentaCalculado = dto.idFerramenta % 100000

                    FerramentaListaUi(
                        id = dto.idFerramenta,
                        codigoTipo = codigoTipoCalculado,
                        nFerramenta = nFerramentaCalculado,
                        idRequisicao = rMinhas.data.find { it.idFerramenta == dto.idFerramenta }?.idRequisicao,
                        nome = dto.nome,
                        detalhes = "${dto.localizacao} · ${dto.categoria}",
                        estado = estadoVis,
                        showDevolverButtons = isMinha,
                        showRequisitarButton = isReservadaParaMim
                    )
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                        todasAsFerramentas = listaMapeada
                    )
                }
                aplicarFiltros()
            } else {
                _state.update { it.copy(isLoading = false, error = "Erro ao carregar ferramentas") }
            }
        }
    }
    /**
     * Avisa o sistema de que uma ferramenta está estragada e devolve-a automaticamente.
     *
     * @param idFerramenta O número que identifica a ferramenta estragada.
     * @param idRequisicao O número do pedido em que o técnico estava a usar esta ferramenta.
     */
    fun marcarMauEstado(idFerramenta: Int, idRequisicao: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val rEstado = ferramentasDataSource.mudarEstadoFerramenta(idFerramenta, "Danificada")
            if (rEstado is ApiResult.Success) {
                ferramentasDataSource.devolverFerramenta(idRequisicao)
            }
            carregar()
        }
    }
    /**
     * Entrega uma ferramenta que estava a ser usada pelo técnico de volta ao armário.
     * Se der erro, mostra uma mensagem de aviso no ecrã.
     *
     * @param idRequisicao O número do pedido de quando o técnico levou a ferramenta.
     */
    fun devolver(idRequisicao: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val r = ferramentasDataSource.devolverFerramenta(idRequisicao)) {
                is ApiResult.Success -> carregar()
                is ApiResult.Error -> _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao devolver"
                    )
                }
            }
        }
    }
    /**
     * Chamado sempre que o utilizador escreve ou apaga uma letra na barra de pesquisa.
     * Guarda o texto novo e filtra as ferramentas para mostrar apenas as que combinam.
     *
     * @param query O texto atual que está escrito na barra.
     */
    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        aplicarFiltros()
    }
    /**
     * Chamado quando o utilizador toca num dos botões de filtro (como "As minhas" ou "Disponíveis").
     * Atualiza o ecrã para mostrar apenas o que interessa.
     *
     * @param filtro A nova categoria que o utilizador escolheu.
     */
    fun onFiltroChange(filtro: FiltroFerramenta) {
        _state.update { it.copy(filtroAtual = filtro) }
        aplicarFiltros()
    }
    /**
     * Pega na lista completa de ferramentas e cria uma lista mais pequena,
     * escondendo as que não combinam com a palavra pesquisada ou com o botão de filtro selecionado.
     */
    private fun aplicarFiltros() {
        val atual = _state.value
        var filtradas = atual.todasAsFerramentas

        if (atual.searchQuery.isNotBlank()) {
            filtradas = filtradas.filter { it.nome.contains(atual.searchQuery, ignoreCase = true) }
        }

        filtradas = when (atual.filtroAtual) {
            FiltroFerramenta.TODAS -> filtradas
            FiltroFerramenta.AS_MINHAS -> filtradas.filter { it.showDevolverButtons }
            FiltroFerramenta.DISPONIVEIS -> filtradas.filter { it.estado == EstadoFerramentaLista.DISPONIVEL }
        }

        _state.update { it.copy(ferramentas = filtradas) }
    }
    /**
     * Pede autorização ao sistema para o técnico levar e começar a usar uma ferramenta.
     *
     * @param codigoTipo O número que identifica o tipo de ferramenta.
     * @param nFerramenta O número específico desta ferramenta.
     */
    fun requisitarFerramenta(codigoTipo: Int, nFerramenta: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val r =
                ferramentasDataSource.requisitarFerramenta(idTecnico, codigoTipo, nFerramenta)) {
                is ApiResult.Success -> {
                    carregar()
                }

                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = mensagem(r.error)
                        )
                    }
                }
            }
        }
    }
    /**
     * Apaga a mensagem de erro do ecrã, fechando o aviso visual.
     */
    fun limparErro() {
        _state.update { it.copy(error = null) }
    }
    /**
     * Abre a lista para ver as ferramentas dentro de um conjunto, ou fecha-a se já estiver aberta.
     *
     * @param templateId O número que identifica em qual conjunto o utilizador clicou.
     */
    fun toggleTemplate(templateId: Int) {
        _state.update { currentState ->
            val novosTemplates = currentState.templates.map { template ->
                if (template.id == templateId) template.copy(isExpanded = !template.isExpanded) else template
            }
            currentState.copy(templates = novosTemplates)
        }
    }
    /**
     * Transforma um erro de internet complicado numa frase simples e fácil de entender para o utilizador.
     *
     * @param erro O problema técnico que aconteceu ao tentar falar com o servidor.
     * @return A frase simples em português a explicar o que falhou.
     */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}