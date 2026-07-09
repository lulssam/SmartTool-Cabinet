package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

//#my_code

import kotlinx.serialization.Serializable
/**
 * Representa um pedido para criar uma nova requisição de ferramenta.
 */

class NovaRequisicaoDTO {
    /**
     * Contém a informação necessária para criar uma nova requisição.
     *
     * @property idTecnico Identificador do técnico que faz a requisição.
     * @property codigoTipo Código do tipo da ferramenta.
     * @property nFerramenta Número da ferramenta.
     */
    @Serializable
    data class NovaRequisicaoDTO(
        val idTecnico: Int,
        val codigoTipo: Int,
        val nFerramenta: Int
    )
}