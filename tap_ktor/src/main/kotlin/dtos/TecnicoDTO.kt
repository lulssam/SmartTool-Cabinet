package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TecnicoDTO(
    val id: Int,
    val nome: String,
    val turno: String,
    val disponivel: Boolean
)
