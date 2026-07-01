package pfc.a50727a50799.smarttool_cabinet.core.tarefa

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.EstadoTarefa
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.PrioridadeTarefa
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.TarefaUi
import kotlin.time.Clock

@Serializable
data class TarefaDto(
    val idTarefa: Int,
    val descricao: String,
    val tecnico: String,
    val estado: String,
    val prioridade: String,
    val dhAtribuicao: String,
    val ferramentas: List<String> = emptyList()
)


/** Converte os dados crus da tarefa no modelo pronto para o card do gestor. */
fun TarefaDto.toGestorUi(): TarefaUi = TarefaUi(
    id = idTarefa.toString(),
    titulo = descricao,
    codigo = "#" + idTarefa.toString().padStart(4, '0'),
    quando = formatarQuando(dhAtribuicao),
    tecnico = tecnico,
    estado = when (estado.uppercase()) {
        "EM CURSO", "EM_CURSO" -> EstadoTarefa.EM_CURSO
        "CONCLUIDA", "CONCLUÍDA" -> EstadoTarefa.CONCLUIDA
        else -> EstadoTarefa.PENDENTE
    },
    prioridade = when (prioridade.uppercase()) {
        "ALTA" -> PrioridadeTarefa.ALTA
        "BAIXA" -> PrioridadeTarefa.BAIXA
        else -> PrioridadeTarefa.NORMAL
    },
    ferramentas = ferramentas
)

/** "Hoje: 08:00" / "Ontem: 14:00" / "27/06: 16:00" — mesma lógica do histórico. */
private fun formatarQuando(raw: String): String {
    val dt = LocalDateTime.parse(raw.replace(" ", "T"))
    val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val hora = "${dt.hour.toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}"
    val dia = when (dt.date) {
        hoje -> "Hoje"
        hoje.minus(1, DateTimeUnit.DAY) -> "Ontem"
        else -> "${dt.dayOfMonth.toString().padStart(2, '0')}/${
            dt.monthNumber.toString().padStart(2, '0')
        }"
    }
    return "$dia: $hora"
}
