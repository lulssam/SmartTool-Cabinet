package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class NovaRequisicaoDTO(
    val idTecnico: Int,
    val codigoTipo: Int,
    val nFerramenta: Int
)
