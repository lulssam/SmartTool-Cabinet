package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import kotlinx.serialization.Serializable

class NovaRequisicaoDTO {
    @Serializable
    data class NovaRequisicaoDTO(
        val idTecnico: Int,
        val codigoTipo: Int,
        val nFerramenta: Int
    )
}