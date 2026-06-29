package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

enum class TipoMovimento {
    RETIROU,
    DEVOLVEU
}

data class HistoricoItemUi(
    val id: Int,
    val nomeFerramenta: String,
    val tipo: TipoMovimento
)

data class SecaoHistoricoUi(
    val data: String,
    val movimentos: List<HistoricoItemUi>
)

data class HistoricoUiState(
    val secoes: List<SecaoHistoricoUi> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)