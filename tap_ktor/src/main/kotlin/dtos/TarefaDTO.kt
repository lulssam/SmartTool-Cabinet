package com.tapktor.dtos

import kotlinx.serialization.Serializable
//#my_code
@Serializable
data class TarefaDTO(
    val idTarefa: Int,
    val titulo: String,
    val descricao: String,
    val tecnico: String,
    val estado: String,
    val prioridade: String,
    val dhAtribuicao: String,
    val ferramentas: MutableList<String> = mutableListOf()
) {
}