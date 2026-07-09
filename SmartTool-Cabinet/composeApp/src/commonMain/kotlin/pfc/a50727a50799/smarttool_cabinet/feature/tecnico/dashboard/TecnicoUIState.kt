package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.dashboard
//#my_code
data class TecnicoUiState(
    val nomeTecnico: String = "",
    val cargo: String = "",
    val turno: String = "",
    val ferramentasEmUso: Int = 0,
    val ferramentasParaDevolver: Int = 0,
    val minhasFerramentas: List<FerramentaTecnicoUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Modelo visual específico para o ecrã do Técnico para preencher
 * os detalhes exatos do Figma (nome, armário/categoria e estado).
 */
data class FerramentaTecnicoUi(
    val id: Int,
    val nome: String,
    val detalhes: String,
    val estado: String
)