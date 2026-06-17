package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AlertasDTO(
    val tipo: String,
    val descricao: String,
    val referencia: String
)
