package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FerramentasViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        FerramentasUiState(
            templates = listOf(
                TemplateDiarioUi(1, "Inspeção de Rotina A320", 4),
                TemplateDiarioUi(
                    id = 2,
                    nome = "Manutenção Aviónicos",
                    totalFerramentas = 3,
                    ferramentas = listOf("Torquimetro 60Nm", "Pistola de Calor", "Alicate de Corte"),
                    isExpanded = true // Este vem aberto por defeito (como no Figma)
                ),
                TemplateDiarioUi(3, "Inspeção de Rotina A330", 4)
            ),
            ferramentas = listOf(
                FerramentaListaUi(
                    id = 1, nome = "Chave de Caixa 10mm", detalhes = "F-001 · Chaves · Arm. 1",
                    estado = EstadoFerramentaLista.DISPONIVEL
                ),
                FerramentaListaUi(
                    id = 2, nome = "Alicate de Bico", detalhes = "F-002 · Alicates · Arm. 2",
                    estado = EstadoFerramentaLista.EM_USO, showDevolverButtons = true
                ),
                FerramentaListaUi(
                    id = 3, nome = "Multímetro Digital", detalhes = "F-003 · Medição · Arm. 3",
                    estado = EstadoFerramentaLista.DISPONIVEL, showRequisitarButton = true
                ),
                FerramentaListaUi(
                    id = 4, nome = "Torquímetro 60Nm", detalhes = "F-005 · Medição · Arm. 5",
                    estado = EstadoFerramentaLista.MANUTENCAO
                )
            )
        )
    )
    val state: StateFlow<FerramentasUiState> = _state.asStateFlow()

    // Lógica para a pesquisa
    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    // Lógica para os filtros (Todas, As Minhas, Disponíveis)
    fun onFiltroChange(filtro: FiltroFerramenta) {
        _state.update { it.copy(filtroAtual = filtro) }
    }

    // Lógica para abrir/fechar os templates
    fun toggleTemplate(templateId: Int) {
        _state.update { currentState ->
            val novosTemplates = currentState.templates.map { template ->
                if (template.id == templateId) template.copy(isExpanded = !template.isExpanded)
                else template
            }
            currentState.copy(templates = novosTemplates)
        }
    }
}