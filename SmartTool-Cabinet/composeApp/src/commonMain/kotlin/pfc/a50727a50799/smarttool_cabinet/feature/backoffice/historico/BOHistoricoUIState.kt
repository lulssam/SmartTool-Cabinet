package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.historico
//#my_code
enum class TipoMovimentoBO { RETIROU, DEVOLVEU, MARCOU_AVARIA }

data class SecaoBOHistoricoUi(
    val data: String,
    val movimentos: List<BOHistoricoItemUi> = emptyList()
)

data class BOHistoricoItemUi(
    val id: String,
    val nomeFerramenta: String,
    val funcionario: String,
    val hora: String,
    val tipo: TipoMovimentoBO
)

data class BOHistoricoUiState(
    val secoes: List<SecaoBOHistoricoUi> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)