package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa os dados necessários para criar uma nova tarefa.
 *
 * Inclui a informação sobre quem cria a tarefa,
 * a quem será atribuída e quais as ferramentas permitidas.
 *
 * @property idGestor Identificador do gestor que cria a tarefa.
 * @property idTecnico Identificador do técnico responsável pela tarefa.
 * @property titulo Título da tarefa.
 * @property descricao Descrição da tarefa.
 * @property prioridade Prioridade atribuída à tarefa.
 * @property ferramentasPermitidasIds Lista das ferramentas que podem ser utilizadas na tarefa.
 */
//#my_code
@Serializable
data class NovaTarefaDTO(
    val idGestor: Int,
    val idTecnico: Int,
    val titulo: String,
    val descricao: String,
    val prioridade: String,
    val ferramentasPermitidasIds: List<FerramentaID_DTO>
)