package pfc.a50727a50799.smarttool_cabinet.core.tecnico

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.TecnicoUi

//#my_code
@Serializable
data class TecnicoDto(
    val id: Int,
    val nome: String,
    val turno: String,
    val disponivel: Boolean
)

fun TecnicoDto.toUi(): TecnicoUi = TecnicoUi(
    id = id,
    nome = nome,
    turno = turno,
    disponivel = disponivel
)