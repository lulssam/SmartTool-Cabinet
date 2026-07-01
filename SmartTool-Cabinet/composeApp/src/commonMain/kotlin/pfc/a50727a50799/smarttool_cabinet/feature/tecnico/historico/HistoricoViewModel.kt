package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock
import pfc.a50727a50799.smarttool_cabinet.core.historico.HistoricoDto
import pfc.a50727a50799.smarttool_cabinet.core.historico.HistoricoRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class HistoricoViewModel(
    private val historicoDataSource: HistoricoRemoteDataSource,
    private val idTecnico: Int
) : ViewModel() {

    private val _state = MutableStateFlow(HistoricoUiState(isLoading = true))
    val state: StateFlow<HistoricoUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        if (idTecnico == -1) {
            _state.update { it.copy(isLoading = false, error = "Sessão inválida") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val resposta = historicoDataSource.getHistoricoTecnico(idTecnico)


            val dtos: List<HistoricoDto> = if (resposta is ApiResult.Success) {
                resposta.data
            } else if (resposta is ApiResult.Error) {
                _state.update { it.copy(isLoading = false, error = mensagem(resposta.error)) }
                return@launch
            } else {
                return@launch
            }

            val movimentos = dtos.flatMap { dto: HistoricoDto ->
                buildList {
                    add(movimento(dto, dto.dhRequisicao, TipoMovimento.RETIROU, 0))
                    dto.dhDevolucao?.let { add(movimento(dto, it, TipoMovimento.DEVOLVEU, 1)) }
                }
            }

            val hoje = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val grupos = movimentos
                .sortedByDescending { it.dataHora }
                .groupBy { it.dataHora.date }
                .map { (dia, lista) ->
                    GrupoHistoricoUi(
                        data = etiqueta(dia, hoje),
                        movimentos = lista.map { it.item }
                    )
                }

            _state.update {
                it.copy(
                    isLoading = false,
                    grupos = grupos
                )
            }
        }
    }

    private fun etiqueta(dia: LocalDate, hoje: LocalDate): String = when (dia) {
        hoje -> "HOJE"
        hoje.minus(1, DateTimeUnit.DAY) -> "ONTEM"
        // 3. Trocado 'dayOfMonth' por 'day' a pedido do compilador
        else -> "${dia.day} ${mesPt(dia.month)}"
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
        tipo: TipoMovimento,
        idSufixo: Int
    ): MovimentoComData {
        val dt = LocalDateTime.parse(raw.replace(" ", "T"))
        return MovimentoComData(
            dataHora = dt,
            item = HistoricoItemUi(
                id = "${dto.idRequisicao}-${dto.idFerramenta}-$idSufixo",
                nomeFerramenta = dto.nomeFerramenta,
                detalhe = dto.nomeFuncionario,
                hora = "${dt.hour.toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}",
                tipo = tipo
            )
        )
    }

    private data class MovimentoComData(
        val dataHora: LocalDateTime,
        val item: HistoricoItemUi
    )

    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}