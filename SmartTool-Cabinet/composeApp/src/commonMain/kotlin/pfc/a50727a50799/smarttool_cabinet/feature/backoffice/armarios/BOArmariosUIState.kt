package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.armarios

import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.ArmarioUi
//#my_code
/**
 * Filtros disponíveis para a listagem de armários.
 */
enum class FiltroBOArmario(val label: String) {
    TODOS("Todos"),
    ONLINE("Online"),
    ALERTA("Alerta"),
    OFFLINE("Offline")
}
/**
 * Estado do ecrã de gestão de armários do Backoffice.
 *
 * Contém a lista de armários obtida do backend, o estado da pesquisa,
 * o filtro atualmente selecionado e informação sobre o carregamento
 * dos dados e possíveis erros.
 */
data class BOArmariosUiState(
    val armarios: List<ArmarioUi> = emptyList(),
    val searchQuery: String = "",
    val filtroAtual: FiltroBOArmario = FiltroBOArmario.TODOS,
    val alertasAtivos: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
) {
    /**
     * Lista de armários após aplicação da pesquisa e do filtro selecionado.
     *
     * Primeiro filtra pelo texto introduzido na pesquisa e, de seguida,
     * aplica o filtro correspondente ao estado do armário.
     */
    val armariosFiltrados: List<ArmarioUi>
        get() {
            var lista = armarios

            if (searchQuery.isNotBlank()) {
                lista = lista.filter { it.nome.contains(searchQuery, ignoreCase = true) }
            }

            return when (filtroAtual) {
                FiltroBOArmario.TODOS -> lista
                FiltroBOArmario.ONLINE -> lista.filter { it.estadoArmario.name == "OPERACIONAL" }
                FiltroBOArmario.ALERTA -> lista.filter { it.estadoArmario.name == "ALERTA" }
                FiltroBOArmario.OFFLINE -> lista.filter { it.estadoArmario.name == "AVARIADO" }
            }
        }
}