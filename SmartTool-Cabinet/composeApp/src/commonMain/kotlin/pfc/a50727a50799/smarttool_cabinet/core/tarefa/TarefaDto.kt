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

//#my_code
/**
 * Representa a informação de uma tarefa recebida da API.
 *
 * Contém todos os dados necessários para apresentar a tarefa
 * na interface da aplicação.
 *
 * @property idTarefa Identificador da tarefa.
 * @property titulo Título da tarefa.
 * @property descricao Descrição da tarefa.
 * @property tecnico Nome do técnico responsável pela tarefa.
 * @property estado Estado atual da tarefa.
 * @property prioridade Prioridade atribuída à tarefa.
 * @property dhAtribuicao Data e hora em que a tarefa foi atribuída.
 * @property ferramentas Lista das ferramentas associadas à tarefa.
 */
@Serializable
data class TarefaDto(
    val idTarefa: Int,
    val titulo: String,
    val descricao: String,
    val tecnico: String,
    val estado: String,
    val prioridade: String,
    val dhAtribuicao: String,
    val ferramentas: List<String> = emptyList()
)

/**
 * Converte a tarefa para o modelo utilizado pelo ecrã do gestor.
 *
 * Durante a conversão são adaptados os valores recebidos da API
 * para o formato esperado pela interface.
 *
 * @return Objeto utilizado para apresentar a tarefa ao gestor.
 */
fun TarefaDto.toGestorUi(): TarefaUi = TarefaUi(
    id = idTarefa.toString(),
    titulo = titulo,
    descricao = descricao,
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
/**
 * Converte a tarefa para o modelo utilizado pelo ecrã do técnico.
 *
 * @return Objeto utilizado para apresentar a tarefa ao técnico.
 */
fun TarefaDto.toTecnicoUi(): TarefaUi = toGestorUi().copy(
    quando = formatarQuando(dhAtribuicao)
)

//#my_code end

/**
 * Obtém a hora e uma descrição simples do dia a partir de uma data.
 *
 * A data é convertida para um formato mais fácil de apresentar
 * ao utilizador, indicando se corresponde a hoje, ontem ou outra data.
 *
 * @param raw Data e hora no formato recebido pela API.
 * @return Um par composto pela hora e pela descrição do dia.
 */
private fun horaEDia(raw: String): Pair<String, String> {
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
    return hora to dia
}

/**
 * Formata a data de atribuição de uma tarefa para apresentação
 * na interface da aplicação.
 *
 * @param raw Data e hora no formato recebido pela API.
 * @return Texto formatado com o dia e a hora.
 */
private fun formatarQuando(raw: String): String {
    val (hora, dia) = horaEDia(raw)
    return "$dia: $hora"
}