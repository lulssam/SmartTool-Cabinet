package pfc.a50727a50799.smarttool_cabinet.core.historico

import kotlinx.serialization.Serializable

@Serializable
data class HistoricoDto(
    val idRequisicao: Int,
    val nomeFuncionario: String,
    val idFerramenta: Int,
    val nomeFerramenta: String,
    val dhRequisicao: String,
    val dhDevolucao: String? = null
)
