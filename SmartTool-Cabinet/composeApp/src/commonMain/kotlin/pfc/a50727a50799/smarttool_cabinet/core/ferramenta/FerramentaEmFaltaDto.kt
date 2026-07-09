package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import kotlinx.serialization.Serializable
//#my_code
/**
 * Representa uma ferramenta que ainda não foi devolvida.
 *
 * Contém a informação necessária para identificar a ferramenta
 * e saber quem a requisitou.
 *
 * @property idFerramenta Identificador da ferramenta.
 * @property nomeFerramenta Nome da ferramenta.
 * @property detentor Nome do funcionário que tem a ferramenta.
 * @property dataRequisicao Data em que a ferramenta foi requisitada.
 */
@Serializable
data class FerramentaEmFaltaDto(
 val idFerramenta: Int,
    val nomeFerramenta: String,
    val detentor: String,
    val dataRequisicao: String
) {
}