package com.tapktor.dtos

import kotlinx.serialization.Serializable


@Serializable
data class NovoCargoDTO(
    val cargo: String //recebe TÉCNICO, GESTOR ou BACKOFFICE
)