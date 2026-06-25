package pfc.a50727a50799.smarttool_cabinet.core.armario

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ArmarioUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.EstadoArmario

@Serializable
data class ArmarioDto(
    val nArmario: Int,
    val capacidade: Int,
    val estado: String, // operacional, avariado
    val trancado: Boolean
)

fun ArmarioDto.toUi() = ArmarioUi(
    nome = "Armário $nArmario",
    slotsTotal = capacidade,
    slotsOcupados = 0, // TODO: mudar que ainda não vai buscar a api porque ela não devolve mesmo, tem que se calcular somehow, problema para mais tarde
    emFalta = 0,
    trancado = trancado,
    estadoArmario = when (estado.uppercase()) {
        "Operacional" -> EstadoArmario.OPERACIONAL
        else -> EstadoArmario.AVARIADO
    }
)