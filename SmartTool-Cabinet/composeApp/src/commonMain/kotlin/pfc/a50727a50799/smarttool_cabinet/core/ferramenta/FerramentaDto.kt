package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import kotlinx.serialization.Serializable

@Serializable
data class FerramentaDto(
    val idFerramenta: Int,
    val nome: String,
    val estado: String,
    val categoria: String,
    val localizacao: String
)