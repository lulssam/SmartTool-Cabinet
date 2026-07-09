package com.tapktor.dtos

import kotlinx.serialization.Serializable
/**
 * Representa o histórico de uma requisição de ferramenta.
 *
 * Guarda a informação sobre quem requisitou a ferramenta,
 * quando foi levantada e quando foi devolvida.
 *
 * @property idRequisicao Identificador da requisição.
 * @property nomeFuncionario Nome do funcionário que efetuou a requisição.
 * @property idFerramenta Identificador da ferramenta requisitada.
 * @property nomeFerramenta Nome da ferramenta requisitada.
 * @property dhRequisicao Data e hora em que a ferramenta foi requisitada.
 * @property dhDevolucao Data e hora da devolução da ferramenta. É nula enquanto a ferramenta não tiver sido devolvida.
 */
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
