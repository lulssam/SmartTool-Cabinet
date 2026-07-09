package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa um pedido para criar uma nova requisição de ferramenta.
 *
 * Contém a informação necessária para identificar o técnico
 * e a ferramenta que pretende requisitar.
 *
 * @property idTecnico Identificador do técnico que faz a requisição.
 * @property codigoTipo Código do tipo da ferramenta.
 * @property nFerramenta Número da ferramenta.
 */
//#my_code
@Serializable
data class NovaRequisicaoDTO(
    val idTecnico: Int,
    val codigoTipo: Int,
    val nFerramenta: Int
)
