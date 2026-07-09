package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class NovoTurnoDTO(
    val turno: String //recebe MANHA, TARDE ou NOITE
)