package pfc.a50727a50799.smarttool_cabinet.feature.gestor.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import pfc.a50727a50799.smarttool_cabinet.core.alerta.AlertaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.historico.HistoricoDto
import pfc.a50727a50799.smarttool_cabinet.core.historico.HistoricoRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult


/**
 * Gere o estado e a lógica do ecrã HistoricoGestor.
 *
 * Vai buscar os dados necessários ao backend e guarda-os no estado
 * para o ecrã mostrar. O ecrã nunca fala diretamente com o backend —
 * passa sempre por aqui.
 */
class HistoricoGestorViewModel(
    private val historico: HistoricoRemoteDataSource,
    private val alertas: AlertaRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(HistoricoGestorUiState(isLoading = true))

    /**
     * O estado atual do ecrã, disponível para o Composable observar.
     * Só o ViewModel pode alterar este valor — o ecrã apenas o lê.
     */
    val state: StateFlow<HistoricoGestorUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    /**
     * Vai buscar os dados ao backend e atualiza o estado do ecrã.
     *
     * Mostra um indicador de carregamento enquanto espera,
     * e um erro se algo correr mal.
     */
    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val dtos = when (val r = historico.getHistorico()) {
                is ApiResult.Success -> r.data
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            // alertas
            val nAlertas = when (val r = alertas.getAlertas()) {
                is ApiResult.Success -> r.data.size
                is ApiResult.Error -> 0
            }

            // cada requisição
            val movimentos = dtos.flatMap { dto ->
                buildList {
                    add(movimento(dto, dto.dhRequisicao, TipoMovimento.RETIROU, 0))
                    dto.dhDevolucao?.let { add(movimento(dto, it, TipoMovimento.DEVOLVEU, 1)) }
                }
            }

            // ordenar -> agrupar por dia -> etiquetar
            val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val secoes = movimentos
                .sortedByDescending { it.dataHora }
                .groupBy { it.dataHora.date }
                .map { (dia, lista) ->
                    SecaoHistoricoUi(
                        data = etiqueta(dia, hoje),
                        movimentos = lista.map { it.item }
                    )
                }

            _state.update {
                it.copy(
                    isLoading = false,
                    secoes = secoes,
                    alertasAtivos = nAlertas
                )
            }
        }
    }

    /** "HOJE" / "ONTEM" / "15 MAIO" conforme o dia comparado com hoje. */
    private fun etiqueta(dia: LocalDate, hoje: LocalDate): String = when (dia) {
        hoje -> "HOJE"
        hoje.minus(1, DateTimeUnit.DAY) -> "ONTEM"
        else -> "${dia.dayOfMonth} ${mesPt(dia.month)}"
    }

    private fun mesPt(mes: Month): String = when (mes) {
        Month.JANUARY -> "JANEIRO"; Month.FEBRUARY -> "FEVEREIRO"
        Month.MARCH -> "MARÇO"; Month.APRIL -> "ABRIL"
        Month.MAY -> "MAIO"; Month.JUNE -> "JUNHO"
        Month.JULY -> "JULHO"; Month.AUGUST -> "AGOSTO"
        Month.SEPTEMBER -> "SETEMBRO"; Month.OCTOBER -> "OUTUBRO"
        Month.NOVEMBER -> "NOVEMBRO"; Month.DECEMBER -> "DEZEMBRO"
    }


    /** Constrói um movimento a partir de uma data/hora crua da requisição. */
    private fun movimento(
        dto: HistoricoDto,
        raw: String,
        tipo: TipoMovimento,
        idSufixo: Int
    ): MovimentoComData {
        val dt = LocalDateTime.parse(raw.replace(" ", "T"))
        return MovimentoComData(
            dataHora = dt,
            item = HistoricoItemUi(
                id = dto.idRequisicao * 2 + idSufixo,   // 2 movimentos da mesma req. não colidem
                nomeFerramenta = dto.nomeFerramenta,
                funcionario = dto.nomeFuncionario,
                hora = "${dt.hour.toString().padStart(2, '0')}:${
                    dt.minute.toString().padStart(2, '0')
                }",
                tipo = tipo
            )
        )
    }

    /** Movimento já pronto + a data/hora crua, usada só para ordenar e agrupar. */
    private data class MovimentoComData(
        val dataHora: LocalDateTime,
        val item: HistoricoItemUi
    )

    /**
     * Transforma um erro do backend numa frase legível para o utilizador.
     *
     * @param erro O erro devolvido pelo backend.
     * @return Uma mensagem em português para mostrar no ecrã.
     */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}