package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class NovaTarefaDTO(
    val idGestor: Int,
    val idTecnico: Int,
    val titulo: String,
    val descricao: String,
    val prioridade: String,
    val ferramentasPermitidasIds: List<FerramentaID_DTO>
)