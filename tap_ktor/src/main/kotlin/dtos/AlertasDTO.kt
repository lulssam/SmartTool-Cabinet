package com.tapktor.dtos

import kotlinx.serialization.Serializable

/**
 * Representa um alerta recebido ou enviado pela aplicação.
 *
 * Cada alerta contém o tipo de ocorrência, uma descrição do que aconteceu
 * e uma referência que permite identificar o elemento relacionado com o alerta.
 *
 * @property tipo Indica a categoria ou o tipo do alerta.
 * @property descricao Explica, de forma legível, o motivo ou conteúdo do alerta.
 * @property referencia Identificador associado ao alerta, utilizado para relacioná-lo
 * com o elemento ou registo correspondente.
 */
@Serializable
data class AlertasDTO(
    val tipo: String,
    val descricao: String,
    val referencia: String
)