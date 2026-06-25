package pfc.a50727a50799.smarttool_cabinet.core.alerta

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.AlertaUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.Gravidade

/**
 * O alerta tal como vem do backend (GET /api/alertas).
 * Os nomes TÊM de bater certo com o JSON: tipo, descricao, referencia.
 */
@Serializable
data class AlertaDto(
    val tipo: String,
    val descricao: String,
    val referencia: String
)

/**Converte o alerta do backend para o que o alerta card mostra*/
fun AlertaDto.toUi(): AlertaUi = AlertaUi(
    titulo = when (tipo) {
        "FERRAMENTA_EM_FALTA" -> "Ferramenta em falta"
        "ARMARIO_DESTRANCADO" -> "Armário destrancado"
        "FERRAMENTAS_EM_MAU_ESTADO" -> "Ferramenta em mau estado"
        else -> "Erro desconhecido"
        // todo: adicionar mais tipos
    },
    descricao = descricao,
    hora = "",
    gravidade = when (tipo) {
        "FERRAMENTA_EM_FALTA" -> Gravidade.CRITICO
        else -> Gravidade.AVISO
    }
)
