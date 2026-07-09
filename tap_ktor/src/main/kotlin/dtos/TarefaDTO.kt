package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa uma tarefa atribuída a um técnico.
 *
 * Contém toda a informação necessária para apresentar a tarefa
 * e as ferramentas que podem ser utilizadas na sua execução.
 *
 * @property idTarefa Identificador da tarefa.
 * @property titulo Título da tarefa.
 * @property descricao Descrição da tarefa.
 * @property tecnico Nome do técnico responsável pela tarefa.
 * @property estado Estado atual da tarefa.
 * @property prioridade Prioridade atribuída à tarefa.
 * @property dhAtribuicao Data e hora em que a tarefa foi atribuída.
 * @property ferramentas Lista com os nomes das ferramentas associadas à tarefa.
 */
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