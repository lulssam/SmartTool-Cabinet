package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TarefaDTO(
    val idTarefa: Int,
    val descricao: String,
    val tecnico: String,
    val estado: String,
    val prioridade: String,
    val dhAtribuicao: String,
    val ferramentas: MutableList<String> = mutableListOf()
) {
}