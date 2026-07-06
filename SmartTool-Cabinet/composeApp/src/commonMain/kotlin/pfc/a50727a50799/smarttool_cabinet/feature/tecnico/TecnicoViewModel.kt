package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

/**
 * Trata da lógica do ecrã do Técnico.
 * (No futuro, injetas aqui o teu FerramentaRemoteDataSource igual ao do Gestor)
 */
class TecnicoViewModel(
    private val ferramentas: FerramentaRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(TecnicoUiState())
    val state: StateFlow<TecnicoUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = false, error = null) }

        }
    }

    /** Transforma o erro tipado numa frase legível para o utilizador. */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}