package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

enum class EstadoFerramentaLista {
    DISPONIVEL, EM_USO, MANUTENCAO
}

enum class FiltroFerramenta(val label: String) {
    TODAS("Todas"),
    AS_MINHAS("As minhas"),
    DISPONIVEIS("Disponíveis")
}

data class TemplateDiarioUi(
    val id: Int,
    val nome: String,
    val totalFerramentas: Int,
    val ferramentas: List<String> = emptyList(),
    val isExpanded: Boolean = false
)

data class FerramentaListaUi(
    val id: Int,
    val idRequisicao: Int?,
    val nome: String,
    val detalhes: String,
    val estado: EstadoFerramentaLista,
    val showDevolverButtons: Boolean = false,
    val showRequisitarButton: Boolean = false
)


data class FerramentasUiState(
    val searchQuery: String = "",
    val filtroAtual: FiltroFerramenta = FiltroFerramenta.TODAS,
    val templates: List<TemplateDiarioUi> = emptyList(),
    val ferramentas: List<FerramentaListaUi> = emptyList(),          // Lista filtrada (a que aparece no ecrã)
    val todasAsFerramentas: List<FerramentaListaUi> = emptyList(), // Lista original com tudo
    val isLoading: Boolean = true,
    val error: String? = null
)