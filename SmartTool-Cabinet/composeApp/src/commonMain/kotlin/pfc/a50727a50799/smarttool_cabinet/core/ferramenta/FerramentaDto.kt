package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.DisponibilidadeFerramenta
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.EstadoFerramenta
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.FerramentaUi
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.dashboard.FerramentaTecnicoUi

@Serializable
data class FerramentaDto(
    val idRequisicao: Int? = null,
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

/**
 * Junta os dados crus da ferramenta com o detentor (vindo do endpoint /em-falta)
 * e devolve o modelo já pronto para o card do gestor.
 *
 * @param detentor Quem tem a ferramenta agora. Null quando está no armário.
 */
fun FerramentaDto.toGestorUi(detentor: String? = null): FerramentaUi = FerramentaUi(
    idFerramenta = idFerramenta,
    codigo = "${(idFerramenta / 100000).toString().padStart(4, '0')}-" +
            (idFerramenta % 100000).toString().padStart(5, '0'),
    nome = nome,
    categoria = categoria,
    localizacao = localizacao,
    disponibilidade = when (disponibilidade) {
        "Disponivel" -> DisponibilidadeFerramenta.DISPONIVEL
        "Requisitada" -> DisponibilidadeFerramenta.REQUISITADA
        "Reservada" -> DisponibilidadeFerramenta.RESERVADA
        "Em Manutencao" -> DisponibilidadeFerramenta.EM_MANUTENCAO
        else -> DisponibilidadeFerramenta.DISPONIVEL
    },
    estado = when (estado) {
        "Operacional" -> EstadoFerramenta.OPERACIONAL
        "Danificada" -> EstadoFerramenta.DANIFICADA
        "Abatida" -> EstadoFerramenta.ABATIDA
        else -> EstadoFerramenta.OPERACIONAL
    },
    funcionario = detentor
)