package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class FerramentaEmFaltaDTO(
    val idFerramenta: Int,
    val nomeFerramenta: String,
    val detentor: String,
    val dataRequisicao: String
)