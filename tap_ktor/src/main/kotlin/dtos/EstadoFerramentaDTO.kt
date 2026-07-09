package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa uma alteração ao estado de uma ferramenta.
 *
 * É utilizado para indicar o novo estado que deve ser atribuído à ferramenta.
 *
 * @property estado Novo estado da ferramenta.
 */
//#my_code
@Serializable
data class EstadoFerramentaDTO(
    val estado: String
)
