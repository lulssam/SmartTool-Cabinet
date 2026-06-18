package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class FerramentaID_DTO(
    val codigoTipo: Int,
    val nFerramenta: Int
)