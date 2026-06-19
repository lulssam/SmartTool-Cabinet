package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import kotlinx.serialization.Serializable

@Serializable
data class FerramentaDto(
    val id: Int,
    val nome: String,
    val estado: String,
    val armarioId: Int
)