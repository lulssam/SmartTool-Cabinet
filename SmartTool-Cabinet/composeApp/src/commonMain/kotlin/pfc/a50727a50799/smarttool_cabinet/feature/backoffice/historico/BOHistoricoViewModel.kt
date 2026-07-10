package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.historico

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
 * Gere toda a informação apresentada no ecrã de histórico do BackOffice.
 *
 * É responsável por pedir os movimentos ao servidor, organizá-los por data
 * e preparar os dados para que possam ser mostrados no ecrã.
 *
 * @param historico Fonte de dados utilizada para obter o histórico de movimentos.
 */
class BOHistoricoViewModel(
    private val historico: HistoricoRemoteDataSource,
) : ViewModel() {
    /**
     * Guarda o estado atual do ecrã.
     * Sempre que este valor é atualizado, o ecrã apresenta automaticamente
     * a informação mais recente.
     */
    private val _state = MutableStateFlow(BOHistoricoUiState(isLoading = true))
    val state: StateFlow<BOHistoricoUiState> = _state.asStateFlow()

    init {
        carregar()
    }
    /**
     * Obtém o histórico de movimentos e prepara os dados para serem
     * apresentados no ecrã.
     *
     * Enquanto os dados estão a ser carregados, o ecrã mostra um indicador
     * de carregamento. Se ocorrer algum problema, é apresentada uma mensagem
     * de erro ao utilizador.
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
    /**
     * Converte uma data para um texto mais fácil de ler.
     *
     * Se a data corresponder ao dia de hoje ou de ontem, devolve esses
     * textos. Caso contrário, devolve o dia e o mês.
     *
     * @param dia Data que será apresentada.
     * @param hoje Data correspondente ao dia atual.
     * @return Texto que identifica a data.
     */
    private fun etiqueta(dia: LocalDate, hoje: LocalDate): String = when (dia) {
        hoje -> "HOJE"
        hoje.minus(1, DateTimeUnit.DAY) -> "ONTEM"
        else -> "${dia.dayOfMonth} ${mesPt(dia.month)}"
    }
    /**
     * Converte um mês para o respetivo nome em português.
     *
     * @param mes Mês que será convertido.
     * @return Nome do mês em letras maiúsculas.
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
     * Cria um movimento pronto a ser apresentado no histórico.
     *
     * Também converte a data recebida do servidor para um formato que pode
     * ser utilizado pela aplicação.
     *
     * @param dto Informação recebida do servidor.
     * @param raw Data e hora do movimento em formato de texto.
     * @param tipo Tipo de movimento realizado.
     * @param idSufixo Valor utilizado para garantir que o identificador
     * do movimento é único.
     * @return Movimento preparado para ser apresentado no ecrã.
     */
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
    /**
     * Guarda um movimento juntamente com a respetiva data.
     *
     * Esta informação é utilizada para ordenar e agrupar os movimentos
     * antes de serem apresentados ao utilizador.
     *
     * @property dataHora Data e hora do movimento.
     * @property item Movimento que será apresentado no histórico.
     */

    private data class MovimentoComData(
        val dataHora: LocalDateTime,
        val item: BOHistoricoItemUi
    )
    /**
     * Converte um erro da comunicação com o servidor numa mensagem
     * simples para apresentar ao utilizador.
     *
     * @param erro Erro que ocorreu durante o pedido ao servidor.
     * @return Mensagem que será mostrada no ecrã.
     */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}