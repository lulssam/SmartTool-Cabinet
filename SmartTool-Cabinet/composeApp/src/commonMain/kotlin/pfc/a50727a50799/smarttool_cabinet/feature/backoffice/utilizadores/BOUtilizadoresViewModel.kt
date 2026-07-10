package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.utilizadores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.backoffice.BackOfficeRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
/**
 * Gere toda a informação apresentada no ecrã de gestão de utilizadores.
 *
 * É responsável por obter os utilizadores do servidor, aplicar pesquisas
 * e filtros, bem como criar contas, alterar cargos, alterar turnos
 * e desativar utilizadores.
 *
 * @param backOfficeDataSource Fonte de dados utilizada para comunicar
 * com o servidor.
 */
class BOUtilizadoresViewModel(
    private val backOfficeDataSource: BackOfficeRemoteDataSource
) : ViewModel() {
    /**
     * Guarda o estado atual do ecrã.
     * Sempre que este valor é atualizado, o ecrã apresenta automaticamente
     * a informação mais recente.
     */
    private val _state = MutableStateFlow(BOUtilizadoresUiState())
    /**
     * Estado que pode ser observado pelo ecrã para mostrar a informação
     * ao utilizador.
     */
    val state: StateFlow<BOUtilizadoresUiState> = _state.asStateFlow()
    /**
     * Guarda a lista completa de utilizadores recebida do servidor.
     * Esta lista é utilizada para aplicar pesquisas e filtros
     * sem ser necessário voltar a pedir os dados.
     */
    private var listaCompleta: List<UtilizadorListaUi> = emptyList()

    init {
        carregar()
    }
    /**
     * Obtém os utilizadores do servidor e prepara os dados para serem
     * apresentados no ecrã.
     */
    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val r = backOfficeDataSource.getFuncionarios()) {
                is ApiResult.Success -> {
                    listaCompleta = r.data.map { dto ->
                        UtilizadorListaUi(
                            id = dto.idFunc,
                            nome = dto.nome,
                            email = dto.email ?: "",
                            iniciais = getIniciais(dto.nome),
                            cargoTag = formatarCargo(dto.cargo),
                            turno = formatarTurno(dto.turno),
                            isAtivo = dto.ativo
                        )
                    }
                    aplicarFiltros()
                }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                }
            }
        }
    }
    /**
     * Atualiza o texto da pesquisa e aplica novamente os filtros.
     *
     * @param query Texto introduzido pelo utilizador.
     */
    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        aplicarFiltros()
    }
    /**
     * Atualiza o filtro selecionado e aplica novamente os filtros.
     *
     * @param filtro Novo filtro escolhido pelo utilizador.
     */
    fun onFiltroChange(filtro: FiltroUtilizador) {
        _state.update { it.copy(filtroAtual = filtro) }
        aplicarFiltros()
    }
    /**
     * Atualiza a lista apresentada de acordo com o filtro selecionado
     * e o texto da pesquisa.
     */
    private fun aplicarFiltros() {
        val queryAtual = _state.value.searchQuery
        val filtroAtual = _state.value.filtroAtual

        var listaFiltrada = when (filtroAtual) {
            FiltroUtilizador.TODOS -> listaCompleta
            FiltroUtilizador.GESTOR -> listaCompleta.filter { it.cargoTag == "Gestor" }
            FiltroUtilizador.TECNICO -> listaCompleta.filter { it.cargoTag == "Técnico" }
            FiltroUtilizador.BACK_OFFICE -> listaCompleta.filter { it.cargoTag == "Back Office" }
        }

        if (queryAtual.isNotBlank()) {
            listaFiltrada = listaFiltrada.filter {
                it.nome.contains(queryAtual, ignoreCase = true) ||
                        it.email.contains(queryAtual, ignoreCase = true)
            }
        }

        _state.update {
            it.copy(
                utilizadores = listaFiltrada,
                isLoading = false
            )
        }
    }
    /**
     * Altera o cargo de um utilizador.
     *
     * Quando a alteração termina com sucesso, a lista é atualizada.
     *
     * @param id Identificador do utilizador.
     * @param novoCargo Novo cargo que será atribuído.
     */
    fun alterarCargo(id: Int, novoCargo: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val r = backOfficeDataSource.mudarCargo(id, novoCargo)
            if (r is ApiResult.Success) carregar() else _state.update { it.copy(isLoading = false, error = "Erro ao alterar cargo") }
        }
    }
    /**
     * Altera o turno de um utilizador.
     *
     * Quando a alteração termina com sucesso, a lista é atualizada.
     *
     * @param id Identificador do utilizador.
     * @param novoTurno Novo turno que será atribuído.
     */
    fun alterarTurno(id: Int, novoTurno: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val r = backOfficeDataSource.mudarTurno(id, novoTurno)
            if (r is ApiResult.Success) carregar() else _state.update { it.copy(isLoading = false, error = "Erro ao alterar turno") }
        }
    }
    /**
     * Desativa a conta de um utilizador.
     *
     * Quando a operação termina com sucesso, a lista é atualizada.
     *
     * @param id Identificador do utilizador.
     */
    fun desativarConta(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val r = backOfficeDataSource.desativarFuncionario(id)
            if (r is ApiResult.Success) carregar() else _state.update { it.copy(isLoading = false, error = "Erro ao desativar conta") }
        }
    }

    /**
     * Remove a mensagem de erro atualmente apresentada no ecrã.
     */
    fun limparErro() {
        _state.update { it.copy(error = null) }
    }
    /**
     * Obtém as iniciais a partir do nome de um utilizador.
     *
     * @param nome Nome completo do utilizador.
     * @return Iniciais que serão apresentadas no avatar.
     */
    private fun getIniciais(nome: String): String {
        val partes = nome.trim().split(" ")
        return if (partes.size >= 2) "${partes.first().first()}${partes.last().first()}".uppercase()
        else nome.take(2).uppercase()
    }
    /**
     * Converte o cargo recebido do servidor para um texto
     * mais fácil de ler.
     *
     * @param cargoDB Cargo recebido do servidor.
     * @return Nome do cargo apresentado ao utilizador.
     */
    private fun formatarCargo(cargoDB: String?): String = when (cargoDB?.uppercase()) {
        "GESTOR" -> "Gestor"
        "TECNICO" -> "Técnico"
        "BACKOFFICE" -> "Back Office"
        else -> "Sem Cargo"
    }
    /**
     * Converte o turno recebido do servidor para um texto
     * mais fácil de ler.
     *
     * @param turnoDB Turno recebido do servidor.
     * @return Nome do turno apresentado ao utilizador.
     */
    private fun formatarTurno(turnoDB: String?): String {
        if (turnoDB == null) return "Sem Turno"
        return turnoDB.lowercase().replaceFirstChar { it.uppercase() }
    }
    /**
     * Cria um novo utilizador.
     *
     * Quando a criação termina com sucesso, a lista de utilizadores
     * é atualizada automaticamente.
     *
     * @param nome Nome do novo utilizador.
     * @param email Endereço de email do novo utilizador.
     * @param cargo Cargo atribuído ao novo utilizador.
     * @param turno Turno atribuído ao novo utilizador.
     */
    fun criarUtilizador(nome: String, email: String, cargo: String, turno: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val r = backOfficeDataSource.criarFuncionario(nome, email, cargo, turno)

            if (r is ApiResult.Success) {
                carregar()
            } else {
                _state.update { it.copy(isLoading = false, error = "Erro ao criar utilizador") }
            }
        }
    }
    /**
     * Converte um erro da comunicação com o servidor numa mensagem
     * simples para apresentar ao utilizador.
     *
     * @param erro Erro ocorrido durante a comunicação.
     * @return Mensagem que será apresentada no ecrã.
     */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }

}