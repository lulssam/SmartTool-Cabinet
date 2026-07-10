package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.historico
//#my_code
/**
 * Representa os diferentes tipos de movimentos que podem aparecer
 * no histórico do BackOffice.
 */
enum class TipoMovimentoBO { RETIROU, DEVOLVEU, MARCOU_AVARIA }
/**
 * Representa um conjunto de movimentos pertencentes ao mesmo dia.
 *
 * @property data Data apresentada como título da secção.
 * @property movimentos Lista dos movimentos realizados nesse dia.
 */
data class SecaoBOHistoricoUi(
    val data: String,
    val movimentos: List<BOHistoricoItemUi> = emptyList()
)
/**
 * Representa um movimento apresentado no histórico.
 *
 * @property id Identificador único do movimento.
 * @property nomeFerramenta Nome da ferramenta associada ao movimento.
 * @property funcionario Nome do funcionário responsável pelo movimento.
 * @property hora Hora em que o movimento aconteceu.
 * @property tipo Tipo de movimento realizado.
 */
data class BOHistoricoItemUi(
    val id: String,
    val nomeFerramenta: String,
    val funcionario: String,
    val hora: String,
    val tipo: TipoMovimentoBO
)
/**
 * Guarda toda a informação necessária para mostrar o ecrã
 * de histórico do BackOffice.
 *
 * @property secoes Lista de secções organizadas por data.
 * @property isLoading Indica se os dados ainda estão a ser carregados.
 * @property error Mensagem de erro a apresentar ao utilizador.
 * É nula quando não existe nenhum erro.
 */
data class BOHistoricoUiState(
    val secoes: List<SecaoBOHistoricoUi> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)