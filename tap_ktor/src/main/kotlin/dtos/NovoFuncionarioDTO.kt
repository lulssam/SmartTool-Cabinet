package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class NovoFuncionarioDTO(
    val nomeCompleto: String,
    val email: String,
    val turno: String, // "MANHA", "TARDE", "NOITE"
    val cargo: String  // "GESTOR", "TECNICO", "BACKOFFICE"
)