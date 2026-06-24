package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

data class TecnicoUiState(
    val nomeTecnico: String = "Luísa Sampaio",
    val cargo: String = "Técnica",
    val turno: String = "8:00-16:00",
    val ferramentasEmUso: Int = 2,
    val ferramentasParaDevolver: Int = 1,
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
    val detalhes: String, // ex: "Arm. 1 · Chaves"
    val estado: String    // ex: "Em Uso"
)