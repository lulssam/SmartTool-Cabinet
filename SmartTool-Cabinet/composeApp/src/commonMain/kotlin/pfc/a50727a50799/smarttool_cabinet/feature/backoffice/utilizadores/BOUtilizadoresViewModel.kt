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

class BOUtilizadoresViewModel(
    private val backOfficeDataSource: BackOfficeRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(BOUtilizadoresUiState())
    val state: StateFlow<BOUtilizadoresUiState> = _state.asStateFlow()
    private var listaCompleta: List<UtilizadorListaUi> = emptyList()

    init {
        carregar()
    }

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

    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        aplicarFiltros()
    }

    fun onFiltroChange(filtro: FiltroUtilizador) {
        _state.update { it.copy(filtroAtual = filtro) }
        aplicarFiltros()
    }

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

    fun alterarCargo(id: Int, novoCargo: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val r = backOfficeDataSource.mudarCargo(id, novoCargo)
            if (r is ApiResult.Success) carregar() else _state.update { it.copy(isLoading = false, error = "Erro ao alterar cargo") }
        }
    }

    fun alterarTurno(id: Int, novoTurno: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val r = backOfficeDataSource.mudarTurno(id, novoTurno)
            if (r is ApiResult.Success) carregar() else _state.update { it.copy(isLoading = false, error = "Erro ao alterar turno") }
        }
    }

    fun desativarConta(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val r = backOfficeDataSource.desativarFuncionario(id)
            if (r is ApiResult.Success) carregar() else _state.update { it.copy(isLoading = false, error = "Erro ao desativar conta") }
        }
    }

    fun limparErro() {
        _state.update { it.copy(error = null) }
    }

    private fun getIniciais(nome: String): String {
        val partes = nome.trim().split(" ")
        return if (partes.size >= 2) "${partes.first().first()}${partes.last().first()}".uppercase()
        else nome.take(2).uppercase()
    }

    private fun formatarCargo(cargoDB: String?): String = when (cargoDB?.uppercase()) {
        "GESTOR" -> "Gestor"
        "TECNICO" -> "Técnico"
        "BACKOFFICE" -> "Back Office"
        else -> "Sem Cargo"
    }

    private fun formatarTurno(turnoDB: String?): String {
        if (turnoDB == null) return "Sem Turno"
        return turnoDB.lowercase().replaceFirstChar { it.uppercase() }
    }
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

    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }

}