package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa um técnico disponível para receber tarefas ou requisições.
 *
 * Contém a informação necessária para identificar o técnico
 * e verificar a sua disponibilidade.
 *
 * @property id Identificador do técnico.
 * @property nome Nome do técnico.
 * @property turno Turno de trabalho do técnico.
 * @property disponivel Indica se o técnico está disponível.
 */
//#my_code
@Serializable
data class TecnicoDTO(
    val id: Int,
    val nome: String,
    val turno: String,
    val disponivel: Boolean
)
