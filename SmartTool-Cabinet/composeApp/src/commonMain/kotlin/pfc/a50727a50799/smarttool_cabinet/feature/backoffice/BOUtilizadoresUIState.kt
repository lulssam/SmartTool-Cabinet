package pfc.a50727a50799.smarttool_cabinet.feature.backoffice

enum class FiltroUtilizador(val label: String) {
    TODOS("Todos"),
    GESTOR("Gestor"),
    TECNICO("Técnico"),
    BACK_OFFICE("Back Office")
}

data class UtilizadorListaUi(
    val id: Int,
    val nome: String,
    val email: String,
    val iniciais: String,
    val cargoTag: String,
    val turno: String,
    val isAtivo: Boolean
)

data class BOUtilizadoresUiState(
    val searchQuery: String = "",
    val filtroAtual: FiltroUtilizador = FiltroUtilizador.TODOS,
    val utilizadores: List<UtilizadorListaUi> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)