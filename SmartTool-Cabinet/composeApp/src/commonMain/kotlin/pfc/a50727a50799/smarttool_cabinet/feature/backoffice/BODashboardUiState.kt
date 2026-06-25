package pfc.a50727a50799.smarttool_cabinet.feature.backoffice

data class EstatisticasBOUi(
    val totalUtilizadores: Int,
    val ativosHoje: Int,
    val gestores: Int,
    val tecnicos: Int
)

data class UtilizadorRecenteUi(
    val id: Int,
    val nome: String,
    val iniciais: String,
    val cargoSubtitulo: String, // ex: "Técnico · Turno Manhã"
    val cargoTag: String        // ex: "Técnico"
)

data class ArmarioResumoUi(
    val id: Int,
    val nome: String,
    val estado: String // Online, Alerta, Offline
)

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