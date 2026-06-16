package com.tapktor.respostas

import kotlinx.serialization.Serializable

@Serializable
data class FerramentaResponse(
    val idFerramenta: Int,
    val nome: String,
    val categoria: String,
    val estado: String,
    val disponibilidade: String,
    val localizacao: String
)