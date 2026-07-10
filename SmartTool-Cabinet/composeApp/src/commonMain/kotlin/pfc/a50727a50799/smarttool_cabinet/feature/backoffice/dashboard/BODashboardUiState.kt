package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.dashboard
//#my_code
/**
 * Estatísticas apresentadas no dashboard do Back Office.
 */
data class EstatisticasBOUi(
    val totalUtilizadores: Int,
    val ativosHoje: Int,
    val gestores: Int,
    val tecnicos: Int
)
/**
 * Informação de um utilizador apresentada na lista
 * de utilizadores recentes.
 */
data class UtilizadorRecenteUi(
    val id: Int,
    val nome: String,
    val iniciais: String,
    val cargoSubtitulo: String,
    val cargoTag: String
)
/**
 * Informação resumida de um armário apresentada
 * no dashboard.
 */
data class ArmarioResumoUi(
    val id: Int,
    val nome: String,
    val estado: String
)
/**
 * Estado da interface do dashboard do Back Office.
 *
 * Contém a informação do utilizador com sessão iniciada,
 * as estatísticas, os utilizadores recentes, o estado
 * dos armários e o estado do carregamento.
 */
data class BODashboardUiState(
    val nomeBackOffice: String = "",
    val cargo: String = "",
    val turno: String = "",
    val estatisticas: EstatisticasBOUi = EstatisticasBOUi(0, 0, 0, 0),
    val utilizadoresRecentes: List<UtilizadorRecenteUi> = emptyList(),
    val armarios: List<ArmarioResumoUi> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)