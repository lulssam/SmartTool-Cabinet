package pfc.a50727a50799.smarttool_cabinet.feature.gestor.armarios

import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.AlertaUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.ArmarioUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.EstadoArmario

/**
 * Estado do ecrã de Armários: a lista vinda do backend mais o que o
 * utilizador escolheu na pesquisa e nos filtros.
 */
//#my_code
data class ArmariosUiState(
    val armarios: List<ArmarioUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val alertasAtivos: Int = 0,
    val filtroAtual: FiltroArmario = FiltroArmario.TODOS
) {
    /**
     * Lista já pronta para mostrar: primeiro filtra pelo texto da pesquisa,
     * depois pelo filtro de estado selecionado. É isto que a LazyColumn usa.
     */
    val armariosFiltrados: List<ArmarioUi>
        get() = armarios
            .filter { it.nome.contains(searchQuery, ignoreCase = true) }
            .filter { armario ->
                when (filtroAtual) {
                    FiltroArmario.TODOS -> true
                    FiltroArmario.ONLINE -> armario.estadoArmario == EstadoArmario.OPERACIONAL
                    FiltroArmario.ALERTA -> armario.estadoArmario == EstadoArmario.ALERTA
                    FiltroArmario.OFFLINE -> armario.estadoArmario == EstadoArmario.AVARIADO
                }
            }
}

/** Os 4 chips do design. O `label` é o texto mostrado em cada pill. */
enum class FiltroArmario(val label: String) {
    TODOS("Todos"),
    ONLINE("Online"),
    ALERTA("Alerta"),
    OFFLINE("Offline")
}


