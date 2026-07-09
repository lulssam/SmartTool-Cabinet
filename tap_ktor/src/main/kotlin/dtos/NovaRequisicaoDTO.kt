package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class NovaRequisicaoDTO(
    val idTecnico: Int,
    val codigoTipo: Int,
    val nFerramenta: Int
)
