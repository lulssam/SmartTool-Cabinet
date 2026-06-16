package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class FerramentaDTO(
    val idFerramenta: Int,
    val nome: String,
    val categoria: String,
    val estado: String,
    val disponibilidade: String,
    val localizacao: String
)