package pfc.a50727a50799.smarttool_cabinet.feature.backoffice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

/**
 * Trata da lógica do ecrã do Dashboard de Back Office.
 */
class BODashboardViewModel(
    // private val backOfficeRepository: BackOfficeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BODashboardUiState())
    val state: StateFlow<BODashboardUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // Lógica real da API no futuro:
            /*
            when (val r = backOfficeRepository.getDashboardData()) {
                is ApiResult.Success -> {
                    // Mapeamento e atualização
                }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }
            */
        }
    }

    /** Transforma o erro tipado numa frase legível para o utilizador. */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}