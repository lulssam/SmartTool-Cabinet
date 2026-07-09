package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Identifica uma ferramenta através do seu tipo e do seu número.
 *
 * Esta informação é utilizada sempre que é necessário referenciar
 * uma ferramenta específica.
 *
 * @property codigoTipo Código que identifica o tipo da ferramenta.
 * @property nFerramenta Número da ferramenta dentro desse tipo.
 */
//#my_code
@Serializable
data class FerramentaID_DTO(
    val codigoTipo: Int,
    val nFerramenta: Int
)