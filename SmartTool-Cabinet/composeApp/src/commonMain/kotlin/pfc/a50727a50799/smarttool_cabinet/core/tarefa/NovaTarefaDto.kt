package pfc.a50727a50799.smarttool_cabinet.core.tarefa

import kotlinx.serialization.Serializable

@Serializable
data class NovaTarefaDto(
    val idGestor: Int,
    val idTecnico: Int,
    val titulo: String,
    val descricao: String,
    val prioridade: String = "NORMAL",
    val ferramentasPermitidasIds: List<FerramentaIdDto>
)

@Serializable
data class FerramentaIdDto(
    val codigoTipo: Int,
    val nFerramenta: Int
)
