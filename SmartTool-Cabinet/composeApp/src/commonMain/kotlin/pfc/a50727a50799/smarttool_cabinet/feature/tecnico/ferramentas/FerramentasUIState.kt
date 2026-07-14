package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.ferramentas

//#my_code
/**
 * Indica a situação em que uma ferramenta se encontra no momento,
 * para sabermos se podemos pegá-la ou não.
 */
enum class EstadoFerramentaLista {
    DISPONIVEL, EM_USO, MANUTENCAO, RESERVADA
}
/**
 * As opções que o utilizador tem para arrumar e ver apenas as ferramentas que lhe interessam.
 *
 * @property label O texto bonito que aparece escrito no botão do ecrã.
 */
enum class FiltroFerramenta(val label: String) {
    TODAS("Todas"),
    AS_MINHAS("As minhas"),
    DISPONIVEIS("Disponíveis")
}
/**
 * Representa um conjunto de ferramentas que costumam ser usadas juntas num dia normal de trabalho.
 *
 * @property id O número que identifica este pacote no sistema.
 * @property nome O nome do conjunto (por exemplo, "Kit Manutenção").
 * @property totalFerramentas Quantas ferramentas estão guardadas dentro deste pacote.
 * @property ferramentas A lista com os nomes de cada ferramenta deste pacote.
 * @property isExpanded Se for verdadeiro, significa que o cartão está aberto no ecrã para mostrar todas as ferramentas.
 */
data class TemplateDiarioUi(
    val id: Int,
    val nome: String,
    val totalFerramentas: Int,
    val ferramentas: List<String> = emptyList(),
    val isExpanded: Boolean = false
)
/**
 * Guarda toda a informação que precisamos para desenhar o cartão de uma única ferramenta no ecrã.
 *
 * @property id O número único da ferramenta.
 * @property idRequisicao O número do pedido se o técnico estiver a usar a ferramenta neste momento. Se não estiver, é nulo.
 * @property nome O nome da ferramenta para mostrar ao utilizador.
 * @property detalhes Uma frase pequena a explicar onde a ferramenta está guardada e a sua categoria.
 * @property estado A situação da ferramenta (livre, em uso, etc.) para sabermos que cor usar no cartão.
 * @property showDevolverButtons Se verdadeiro, mostra os botões para devolver a ferramenta porque ela está connosco.
 * @property showRequisitarButton Se verdadeiro, mostra o botão para pedir a ferramenta porque ela está reservada para nós.
 * @property codigoTipo A primeira parte do código da ferramenta (necessário para a pedir ao servidor).
 * @property nFerramenta A segunda parte do código da ferramenta (necessário para a pedir ao servidor).
 */
data class FerramentaListaUi(
    val id: Int,
    val idRequisicao: Int? = null,
    val nome: String,
    val detalhes: String,
    val estado: EstadoFerramentaLista,
    val showDevolverButtons: Boolean = false,
    val showRequisitarButton: Boolean = false,
    val codigoTipo: Int,
    val nFerramenta: Int
)
/**
 * Guarda tudo o que o ecrã precisa de saber para se desenhar num dado momento.
 * Sempre que qualquer valor aqui mudar, o ecrã atualiza-se sozinho.
 *
 * @property searchQuery O texto que o utilizador escreveu na barra de pesquisa.
 * @property filtroAtual Qual a categoria de filtro que o utilizador clicou.
 * @property templates A lista dos pacotes de ferramentas disponíveis.
 * @property ferramentas A lista de ferramentas que vão aparecer no ecrã depois de aplicarmos os filtros e a pesquisa.
 * @property todasAsFerramentas A lista completa com todas as ferramentas, sem esconder nenhuma.
 * @property isLoading Se for verdadeiro, o ecrã deve mostrar uma roda de carregamento enquanto espera pela internet.
 * @property isRefreshing Se for verdadeiro, o utilizador puxou a lista para atualizar; a lista continua visível e mostra-se apenas o indicador do gesto no topo.
 * @property error Uma mensagem de erro para avisar o utilizador que algo correu mal. Se for nulo, está tudo bem.
 */

data class FerramentasUiState(
    val searchQuery: String = "",
    val filtroAtual: FiltroFerramenta = FiltroFerramenta.TODAS,
    val templates: List<TemplateDiarioUi> = emptyList(),
    val ferramentas: List<FerramentaListaUi> = emptyList(),
    val todasAsFerramentas: List<FerramentaListaUi> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
)