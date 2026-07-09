package pfc.a50727a50799.smarttool_cabinet.core.historico

import kotlinx.serialization.Serializable
//#my_code
/**
 * Representa um registo do histórico de requisições de ferramentas.
 *
 * Contém a informação necessária para identificar a requisição,
 * a ferramenta utilizada e as datas de levantamento e devolução.
 *
 * @property idRequisicao Identificador da requisição.
 * @property nomeFuncionario Nome do funcionário que efetuou a requisição.
 * @property idFerramenta Identificador da ferramenta requisitada.
 * @property nomeFerramenta Nome da ferramenta requisitada.
 * @property dhRequisicao Data e hora em que a ferramenta foi requisitada.
 * @property dhDevolucao Data e hora em que a ferramenta foi devolvida. É nula enquanto a devolução não tiver sido registada.
 */
@Serializable
data class HistoricoDto(
    val idRequisicao: Int,
    val nomeFuncionario: String,
    val idFerramenta: Int,
    val nomeFerramenta: String,
    val dhRequisicao: String,
    val dhDevolucao: String? = null
)
