package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.historico

//#my_code
/**
 * Representa os diferentes tipos de movimentos que podem ser
 * efetuados sobre uma ferramenta.
 */

enum class TipoMovimento { RETIROU, DEVOLVEU, MARCOU_AVARIA }
/**
 * Representa a informação de um movimento apresentado
 * no histórico do técnico.
 *
 * @property id Identificador único do movimento.
 * @property nomeFerramenta Nome da ferramenta associada ao movimento.
 * @property detalhe Informação adicional sobre o movimento.
 * @property hora Hora a que o movimento foi realizado.
 * @property tipo Tipo de movimento efetuado.
 */
data class HistoricoItemUi(
    val id: String,
    val nomeFerramenta: String,
    val detalhe: String,
    val hora: String,
    val tipo: TipoMovimento
)
/**
 * Representa um grupo de movimentos organizados por data.
 *
 * @property data Data correspondente ao grupo de movimentos.
 * @property movimentos Lista de movimentos dessa data.
 */
data class GrupoHistoricoUi(
    val data: String,
    val movimentos: List<HistoricoItemUi> = emptyList()
)
/**
 * Representa o estado da interface do ecrã de histórico do técnico.
 *
 * @property grupos Lista de movimentos agrupados por data.
 * @property isLoading Indica se os dados estão a ser carregados.
 * @property isRefreshing Indica se o utilizador puxou a lista para atualizar; a lista continua visível e mostra-se apenas o indicador do gesto no topo.
 * @property error Mensagem de erro apresentada quando ocorre uma falha.
 */
data class HistoricoUiState(
    val grupos: List<GrupoHistoricoUi> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)