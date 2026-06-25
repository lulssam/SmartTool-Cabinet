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

fun ArmarioDto.toUi(
    slotsOcupados: Int,
    emFalta: Int
) = ArmarioUi(
    nome = "Armário $nArmario",
    slotsTotal = capacidade,
    slotsOcupados = slotsOcupados,
    emFalta = emFalta,
    trancado = trancado,
    estadoArmario = when (estado) {
        "Operacional" -> EstadoArmario.OPERACIONAL
        "Alerta" -> EstadoArmario.ALERTA
        else -> EstadoArmario.AVARIADO
    }

)