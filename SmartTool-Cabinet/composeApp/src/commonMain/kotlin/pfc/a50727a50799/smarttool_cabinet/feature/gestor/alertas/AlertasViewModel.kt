package pfc.a50727a50799.smarttool_cabinet.feature.gestor.alertas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.alerta.AlertaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.alerta.toUi
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class AlertasViewModel(
    private val alertas: AlertaRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(
        AlertasUiState(isLoading = true)
    )

    val state: StateFlow<AlertasUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // alertas
            val lista = when (val r = alertas.getAlertas()) {
                is ApiResult.Success ->
                    _state.update { it.copy(alertas = r.data.map { dto -> dto.toUi() }) }

                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            _state.update { it.copy(isLoading = false, error = null) }

        }

    }

    /** Transforma o erro tipado numa frase legível para o utilizador. */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}