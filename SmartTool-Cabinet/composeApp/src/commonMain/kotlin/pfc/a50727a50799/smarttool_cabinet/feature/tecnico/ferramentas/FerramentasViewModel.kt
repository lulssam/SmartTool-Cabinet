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

class FerramentasViewModel(
    private val ferramentasDataSource: FerramentaRemoteDataSource,
    private val idTecnico: Int,
) : ViewModel() {

    private val _state = MutableStateFlow(FerramentasUiState())
    val state: StateFlow<FerramentasUiState> = _state.asStateFlow()

    init {
        carregar()
    }

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

    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        aplicarFiltros()
    }

    fun onFiltroChange(filtro: FiltroFerramenta) {
        _state.update { it.copy(filtroAtual = filtro) }
        aplicarFiltros()
    }

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

    fun limparErro() {
        _state.update { it.copy(error = null) }
    }

    fun toggleTemplate(templateId: Int) {
        _state.update { currentState ->
            val novosTemplates = currentState.templates.map { template ->
                if (template.id == templateId) template.copy(isExpanded = !template.isExpanded) else template
            }
            currentState.copy(templates = novosTemplates)
        }
    }

    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}