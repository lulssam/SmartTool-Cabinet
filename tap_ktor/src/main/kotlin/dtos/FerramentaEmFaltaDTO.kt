package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class FerramentaEmFaltaDTO(
    val idFerramenta: Int,
    val nomeFerramenta: String,
    val detentor: String,
    val dataRequisicao: String
)