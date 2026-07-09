package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa um funcionário da empresa.
 *
 * Contém os dados necessários para identificar o funcionário
 * e conhecer a sua função e disponibilidade.
 *
 * @property idFunc Identificador único do funcionário.
 * @property nome Nome do funcionário.
 * @property cargo Cargo desempenhado pelo funcionário.
 * @property turno Turno de trabalho do funcionário.
 * @property ativo Indica se o funcionário se encontra ativo.
 */
//#my_code
@Serializable
data class FuncionariosDTO(
    val idFunc: Int,
    val nome: String,
    val cargo: String,
    val turno: String,
    val ativo: Boolean
)


