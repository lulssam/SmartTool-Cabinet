package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class NovaFerramentaDto(
    val nome: String,
    val categoria: String,
    val nArmario: Int? = null,
    val disponibilidade: String
)
