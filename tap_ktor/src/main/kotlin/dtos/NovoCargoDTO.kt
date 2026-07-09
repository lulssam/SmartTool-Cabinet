package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa um pedido para alterar o cargo de um funcionário.
 *
 * @property cargo Novo cargo a atribuir ao funcionário.
 */
//#my_code
@Serializable
data class NovoCargoDTO(
    val cargo: String //recebe TÉCNICO, GESTOR ou BACKOFFICE
)