package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.dashboard
//#my_code
/**
 * Guarda toda a informação necessária para mostrar o dashboard do técnico.
 *
 * @property nomeTecnico Nome do técnico apresentado no ecrã.
 * @property cargo Cargo associado ao técnico.
 * @property turno Turno em que o técnico trabalha.
 * @property ferramentasEmUso Quantidade de ferramentas atualmente utilizadas pelo técnico.
 * @property ferramentasParaDevolver Quantidade de ferramentas que precisam de ser devolvidas.
 * @property minhasFerramentas Lista das ferramentas associadas ao técnico.
 * @property isLoading Indica se os dados ainda estão a ser carregados.
 * @property isRefreshing Indica se o utilizador puxou para atualizar; a lista continua visível e mostra-se apenas o indicador do gesto no topo.
 * @property error Mensagem de erro apresentada ao utilizador.
 * É nula quando não existe nenhum erro.
 */
data class TecnicoUiState(
    val nomeTecnico: String = "",
    val cargo: String = "",
    val turno: String = "",
    val ferramentasEmUso: Int = 0,
    val ferramentasParaDevolver: Int = 0,
    val ferramentasReservadas: Int = 0,
    val minhasFerramentas: List<FerramentaTecnicoUi> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

data class FerramentaTecnicoUi(
    val id: Int,
    val nome: String,
    val detalhes: String,
    val estado: String
)