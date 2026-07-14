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
/**
 * ViewModel responsável por gerir o histórico de movimentos do técnico.
 *
 * Carrega os movimentos do técnico autenticado, organiza-os por data
 * e atualiza o estado utilizado pelo ecrã de histórico.
 *
 * @param historicoDataSource Fonte de dados responsável por obter o histórico.
 * @param idTecnico Identificador do técnico autenticado.
 */
class HistoricoViewModel(
    private val historicoDataSource: HistoricoRemoteDataSource,
    private val idTecnico: Int
) : ViewModel() {

    private val _state = MutableStateFlow(HistoricoUiState(isLoading = true))
    val state: StateFlow<HistoricoUiState> = _state.asStateFlow()

    init {
        carregar()
    }
    /**
     * Carrega o histórico do técnico.
     *
     * Obtém os movimentos a partir da API, converte-os para o modelo da interface,
     * organiza-os por data e atualiza o estado do ecrã.
     *
     * @param isRefresh Verdadeiro quando o pedido vem do gesto "puxar para atualizar".
     *                  Nesse caso mantém a lista visível (usa isRefreshing em vez de isLoading).
     */
    fun carregar(isRefresh: Boolean = false) {
        if (idTecnico == -1) {
            _state.update { it.copy(isLoading = false, error = "Sessão inválida") }
            return
        }

        viewModelScope.launch {
            _state.update {
                if (isRefresh) it.copy(isRefreshing = true, error = null)
                else it.copy(isLoading = true, error = null)
            }

            val resposta = historicoDataSource.getHistoricoTecnico(idTecnico)


            val dtos: List<HistoricoDto> = if (resposta is ApiResult.Success) {
                resposta.data
            } else if (resposta is ApiResult.Error) {
                _state.update { it.copy(isLoading = false, isRefreshing = false, error = mensagem(resposta.error)) }
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
                    isRefreshing = false,
                    grupos = grupos
                )
            }
        }
    }

    /**
     * Recarrega o histórico em resposta ao gesto de "puxar para atualizar".
     * Mantém a lista visível e mostra o indicador do gesto no topo.
     */
    fun refresh() = carregar(isRefresh = true)
    /**
     * Devolve a etiqueta correspondente a uma determinada data.
     *
     * Apresenta "HOJE", "ONTEM" ou a data formatada em português.
     *
     * @param dia Data a apresentar.
     * @param hoje Data atual utilizada para comparação.
     * @return Texto correspondente à data.
     */
    private fun etiqueta(dia: LocalDate, hoje: LocalDate): String = when (dia) {
        hoje -> "HOJE"
        hoje.minus(1, DateTimeUnit.DAY) -> "ONTEM"
        // 3. Trocado 'dayOfMonth' por 'day' a pedido do compilador
        else -> "${dia.day} ${mesPt(dia.month)}"
    }
    /**
     * Converte um mês para a respetiva designação em português.
     *
     * @param mes Mês a converter.
     * @return Nome do mês em português.
     */
    private fun mesPt(mes: Month): String = when (mes) {
        Month.JANUARY -> "JANEIRO"; Month.FEBRUARY -> "FEVEREIRO"
        Month.MARCH -> "MARÇO"; Month.APRIL -> "ABRIL"
        Month.MAY -> "MAIO"; Month.JUNE -> "JUNHO"
        Month.JULY -> "JULHO"; Month.AUGUST -> "AGOSTO"
        Month.SEPTEMBER -> "SETEMBRO"; Month.OCTOBER -> "OUTUBRO"
        Month.NOVEMBER -> "NOVEMBRO"; Month.DECEMBER -> "DEZEMBRO"
    }
    /**
     * Converte um registo do histórico num movimento apresentado na interface.
     *
     * Cria um objeto contendo a informação do movimento e a respetiva
     * data para posterior agrupamento.
     *
     * @param dto Registo obtido da API.
     * @param raw Data e hora do movimento.
     * @param tipo Tipo de movimento realizado.
     * @param idSufixo Sufixo utilizado para garantir um identificador único.
     * @return Movimento convertido para a interface.
     */
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
    /**
     * Estrutura auxiliar utilizada para associar um movimento
     * à respetiva data e hora.
     */
    private data class MovimentoComData(
        val dataHora: LocalDateTime,
        val item: HistoricoItemUi
    )
    /**
     * Converte um erro da API numa mensagem apresentada ao utilizador.
     *
     * @param erro Erro devolvido pela camada de rede.
     * @return Mensagem correspondente ao erro.
     */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}