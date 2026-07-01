package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.historico

enum class TipoMovimento { RETIROU, DEVOLVEU, MARCOU_AVARIA }

data class HistoricoItemUi(
    val id: String,
    val nomeFerramenta: String,
    val detalhe: String,
    val hora: String,
    val tipo: TipoMovimento
)

data class GrupoHistoricoUi(
    val data: String,
    val movimentos: List<HistoricoItemUi> = emptyList()
)

data class HistoricoUiState(
    val grupos: List<GrupoHistoricoUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)