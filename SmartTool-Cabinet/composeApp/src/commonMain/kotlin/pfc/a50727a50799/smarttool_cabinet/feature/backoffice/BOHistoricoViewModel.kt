package pfc.a50727a50799.smarttool_cabinet.feature.backoffice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock
import pfc.a50727a50799.smarttool_cabinet.core.alerta.AlertaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.historico.HistoricoDto
import pfc.a50727a50799.smarttool_cabinet.core.historico.HistoricoRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class BOHistoricoViewModel(
    private val historico: HistoricoRemoteDataSource,
) : ViewModel() {

    private val _state = MutableStateFlow(BOHistoricoUiState(isLoading = true))
    val state: StateFlow<BOHistoricoUiState> = _state.asStateFlow()

    init {
        carregar()
    }

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

            val movimentos = dtos.flatMap { dto ->
                buildList {
                    add(movimento(dto, dto.dhRequisicao, TipoMovimentoBO.RETIROU, 0))
                    dto.dhDevolucao?.let { add(movimento(dto, it, TipoMovimentoBO.DEVOLVEU, 1)) }
                }
            }

            val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val secoes = movimentos
                .sortedByDescending { it.dataHora }
                .groupBy { it.dataHora.date }
                .map { (dia, lista) ->
                    SecaoBOHistoricoUi(
                        data = etiqueta(dia, hoje),
                        movimentos = lista.map { it.item }
                    )
                }

            _state.update {
                it.copy(
                    isLoading = false,
                    secoes = secoes,

                )
            }
        }
    }

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

    private fun movimento(
        dto: HistoricoDto,
        raw: String,
        tipo: TipoMovimentoBO,
        idSufixo: Int
    ): MovimentoComData {
        val dt = LocalDateTime.parse(raw.replace(" ", "T"))
        return MovimentoComData(
            dataHora = dt,
            item = BOHistoricoItemUi(
                id = "${dto.idRequisicao}-${dto.idFerramenta}-$idSufixo",
                nomeFerramenta = dto.nomeFerramenta,
                funcionario = dto.nomeFuncionario,
                hora = "${dt.hour.toString().padStart(2, '0')}:${
                    dt.minute.toString().padStart(2, '0')
                }",
                tipo = tipo
            )
        )
    }

    private data class MovimentoComData(
        val dataHora: LocalDateTime,
        val item: BOHistoricoItemUi
    )

    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}