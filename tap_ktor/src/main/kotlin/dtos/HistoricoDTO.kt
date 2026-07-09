package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class HistoricoDTO(
    val idRequisicao: Int,
    val nomeFuncionario: String,
    val idFerramenta: Int,
    val nomeFerramenta: String,
    val dhRequisicao: String,
    val dhDevolucao: String? // null porque ainda não devolveram
)
