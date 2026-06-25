package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

// Tipos de movimento possíveis no histórico
enum class TipoMovimento {
    RETIROU,
    DEVOLVEU
}

// Representa uma única linha do histórico
data class HistoricoItemUi(
    val id: Int,
    val nomeFerramenta: String,
    val tipo: TipoMovimento
)

// Representa um grupo de movimentos (ex: "HOJE", "ONTEM")
data class SecaoHistoricoUi(
    val data: String,
    val movimentos: List<HistoricoItemUi>
)

data class HistoricoUiState(
    val secoes: List<SecaoHistoricoUi> = emptyList(),
    val isLoading: Boolean = true, // Começa a true para o loading inicial
    val error: String? = null
)