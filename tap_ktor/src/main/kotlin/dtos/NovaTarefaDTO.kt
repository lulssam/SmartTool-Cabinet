package com.tapktor.dtos

import kotlinx.serialization.Serializable

@Serializable
data class NovaTarefaDTO(
    val idGestor: Int,
    val idTecnico: Int,
    val descricao: String,
    val ferramentasPermitidasIds: List<FerramentaID_DTO>
)