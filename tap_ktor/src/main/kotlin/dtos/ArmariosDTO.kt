package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa um armário onde as ferramentas podem ser armazenadas.
 *
 * Contém a informação necessária para identificar o armário,
 * conhecer a sua capacidade e saber se está disponível para utilização.
 *
 * @property nArmario Número que identifica o armário.
 * @property capacidade Quantidade máxima de ferramentas que o armário pode armazenar.
 * @property estado Estado atual do armário.
 * @property trancado Indica se o armário se encontra trancado.
 */
//#my_code
@Serializable
data class ArmariosDTO(
    val nArmario: Int,
    val capacidade: Int,
    val estado: String,
    val trancado: Boolean,
)