package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa um pedido para alterar o turno de um funcionário.
 *
 * @property turno Novo turno a atribuir ao funcionário.
 */
//#my_code
@Serializable
data class NovoTurnoDTO(
    val turno: String //recebe MANHA, TARDE ou NOITE
)