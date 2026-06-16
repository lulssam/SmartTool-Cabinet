package com.tapktor.respostas

import kotlinx.serialization.Serializable

@Serializable
data class FuncionariosResponse(
    val idFunc: Int,
    val nome: String,
    val cargo: String
)


