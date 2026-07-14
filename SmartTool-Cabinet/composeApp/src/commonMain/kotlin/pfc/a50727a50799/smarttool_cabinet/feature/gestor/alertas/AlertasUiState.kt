package pfc.a50727a50799.smarttool_cabinet.feature.gestor.alertas

import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.AlertaUi
//#my_code
/**
 * Estado do ecrã dos Alertas: a lista vinda do backend.
 */
data class AlertasUiState(
    val alertas: List<AlertaUi> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val alertasAtivos: Int = 0
) {
}