package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.utilizadores
//#my_code
/**
 * Representa os filtros disponíveis para a lista de utilizadores.
 *
 * @property label Texto apresentado ao utilizador para identificar cada filtro.
 */
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
/**
 * Representa um utilizador apresentado na lista de gestão de utilizadores.
 *
 * @property id Identificador único do utilizador.
 * @property nome Nome completo do utilizador.
 * @property email Endereço de email do utilizador.
 * @property iniciais Iniciais apresentadas no avatar do utilizador.
 * @property cargoTag Cargo apresentado no ecrã.
 * @property turno Turno associado ao utilizador.
 * @property isAtivo Indica se a conta do utilizador está ativa.
 */
data class UtilizadorListaUi(
    val id: Int,
    val nome: String,
    val email: String,
    val iniciais: String,
    val cargoTag: String,
    val turno: String,
    val isAtivo: Boolean
)
/**
 * Guarda toda a informação necessária para mostrar o ecrã
 * de gestão de utilizadores.
 *
 * @property searchQuery Texto introduzido na pesquisa.
 * @property filtroAtual Filtro atualmente selecionado.
 * @property utilizadores Lista de utilizadores apresentada no ecrã.
 * @property isLoading Indica se os dados ainda estão a ser carregados.
 * @property error Mensagem de erro apresentada ao utilizador.
 * É nula quando não existe nenhum erro.
 */
data class BOUtilizadoresUiState(
    val searchQuery: String = "",
    val filtroAtual: FiltroUtilizador = FiltroUtilizador.TODOS,
    val utilizadores: List<UtilizadorListaUi> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)