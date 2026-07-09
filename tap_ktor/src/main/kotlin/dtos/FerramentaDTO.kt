package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa a informação de uma ferramenta.
 *
 * Contém os dados necessários para identificar a ferramenta,
 * conhecer as suas características e saber onde se encontra.
 *
 * @property idRequisicao Identificador da requisição associada à ferramenta, caso exista.
 * @property idFerramenta Identificador único da ferramenta.
 * @property nome Nome da ferramenta.
 * @property categoria Categoria à qual a ferramenta pertence.
 * @property estado Estado atual da ferramenta.
 * @property disponibilidade Indica se a ferramenta está disponível para utilização.
 * @property localizacao Local onde a ferramenta se encontra.
 */
//#my_code
@Serializable
data class FerramentaDTO(
    val idRequisicao: Int? = null,
    val idFerramenta: Int,
    val nome: String,
    val categoria: String,
    val estado: String,
    val disponibilidade: String,
    val localizacao: String
)