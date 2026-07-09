package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class FerramentaEmFaltaDto(
 val idFerramenta: Int,
    val nomeFerramenta: String,
    val detentor: String,
    val dataRequisicao: String
) {
}