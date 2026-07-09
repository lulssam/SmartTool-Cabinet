package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class EstadoFerramentaDTO(
    val estado: String
)
