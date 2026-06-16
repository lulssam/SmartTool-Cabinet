package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class FuncionariosDTO(
    val idFunc: Int,
    val nome: String,
    val cargo: String
)


