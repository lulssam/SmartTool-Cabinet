package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.FerramentaTecnicoUi

@Serializable
data class FerramentaDto(
    val idFerramenta: Int,
    val nome: String,
    val estado: String,
    val categoria: String,
    val disponibilidade: String,
    val localizacao: String
)

fun FerramentaDto.toTecnicoUi(): FerramentaTecnicoUi = FerramentaTecnicoUi(
    id = idFerramenta,
    nome = nome,
    detalhes = "$localizacao, $categoria",
    estado = when (disponibilidade) {
        "Requisitada" -> "Em Uso"
        else -> disponibilidade
    }
)