package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import kotlinx.serialization.Serializable

@Serializable
data class NovaFerramentaDto(
    val nome: String,
    val categoria: String,
    val nArmario: Int? = null,
    val disponibilidade: String
)