package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class FerramentaID_DTO(
    val codigoTipo: Int,
    val nFerramenta: Int
)