package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class FuncionariosDTO(
    val idFunc: Int,
    val nome: String,
    val cargo: String,
    val turno: String,
    val ativo: Boolean
)


