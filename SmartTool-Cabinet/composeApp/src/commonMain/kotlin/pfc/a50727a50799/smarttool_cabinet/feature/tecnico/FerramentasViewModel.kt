package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

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
 * Trata da lógica do ecrã de Ferramentas.
 */
class FerramentasViewModel(
    private val ferramentas: FerramentaRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(FerramentasUiState())
    val state: StateFlow<FerramentasUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // Aqui entrará a lógica real da API no futuro:
            // when (val r = ferramentas.getFerramentas()) { ... }
        }
    }

    // Ações de UI que mantemos no ViewModel pois são manipulações de estado válidas
    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onFiltroChange(filtro: FiltroFerramenta) {
        _state.update { it.copy(filtroAtual = filtro) }
    }

    fun toggleTemplate(templateId: Int) {
        _state.update { currentState ->
            val novosTemplates = currentState.templates.map { template ->
                if (template.id == templateId) template.copy(isExpanded = !template.isExpanded)
                else template
            }
            currentState.copy(templates = novosTemplates)
        }
    }

    /** Transforma o erro tipado numa frase legível para o utilizador. */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}