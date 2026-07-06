package pfc.a50727a50799.smarttool_cabinet.feature.backoffice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class BOUtilizadoresViewModel(
    // private val repository: BackOfficeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BOUtilizadoresUiState())
    val state: StateFlow<BOUtilizadoresUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

        }
    }

    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onFiltroChange(filtro: FiltroUtilizador) {
        _state.update { it.copy(filtroAtual = filtro) }
    }

    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}