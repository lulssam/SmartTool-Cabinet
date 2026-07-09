package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class ArmariosDTO(
    val nArmario: Int,
    val capacidade: Int,
    val estado: String,
    val trancado: Boolean,
)