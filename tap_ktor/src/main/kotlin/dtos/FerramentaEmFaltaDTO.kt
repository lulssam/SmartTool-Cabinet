package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa uma ferramenta que ainda não foi devolvida.
 *
 * Inclui a informação necessária para identificar a ferramenta
 * e a pessoa que ficou responsável pela sua requisição.
 *
 * @property idFerramenta Identificador da ferramenta.
 * @property nomeFerramenta Nome da ferramenta.
 * @property detentor Nome do funcionário que requisitou a ferramenta.
 * @property dataRequisicao Data em que a ferramenta foi requisitada.
 */

//#my_code
@Serializable
data class FerramentaEmFaltaDTO(
    val idFerramenta: Int,
    val nomeFerramenta: String,
    val detentor: String,
    val dataRequisicao: String
)