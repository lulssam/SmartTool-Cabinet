package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.utilizadores
//#my_code
enum class FiltroUtilizador(val label: String) {
    TODOS("Todos"),
    GESTOR("Gestor"),
    TECNICO("Técnico"),
    BACK_OFFICE("Back Office")
}

enum class PerfilOpcao(val label: String, val valor: String) {
    TECNICO("Técnico", "TECNICO"), GESTOR("Gestor", "GESTOR"), BACKOFFICE("Back Office", "BACKOFFICE")
}
enum class TurnoOpcao(val label: String, val valor: String) {
    MANHA("Manhã", "MANHA"), TARDE("Tarde", "TARDE"), NOITE("Noite", "NOITE")
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