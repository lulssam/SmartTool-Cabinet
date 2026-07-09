package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa os dados necessários para criar um novo funcionário.
 *
 * Contém a informação básica utilizada durante o registo.
 *
 * @property nomeCompleto Nome completo do funcionário.
 * @property email Endereço de correio eletrónico do funcionário.
 * @property turno Turno de trabalho atribuído ao funcionário.
 * @property cargo Cargo que o funcionário irá desempenhar.
 */
//#my_code
@Serializable
data class NovoFuncionarioDTO(
    val nomeCompleto: String,
    val email: String,
    val turno: String, // "MANHA", "TARDE", "NOITE"
    val cargo: String  // "GESTOR", "TECNICO", "BACKOFFICE"
)