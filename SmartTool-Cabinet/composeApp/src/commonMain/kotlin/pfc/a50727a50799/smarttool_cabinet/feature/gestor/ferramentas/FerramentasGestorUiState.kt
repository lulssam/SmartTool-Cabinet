package pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas

/**
 * Tudo o que o ecrã Ferramentas precisa para se mostrar corretamente.
 *
 * É imutável — quando algo muda, cria-se uma cópia nova com `.copy()`.
 * O Compose deteta essas mudanças e redesenha apenas o necessário.
 *
 * @property seccoes Lista já pronta a mostrar: ferramentas agrupadas por categoria
 * @property isLoading True enquanto estamos à espera de dados do backend.
 *                     O ecrã mostra um indicador de carregamento durante este tempo.
 * @property error Mensagem de erro para mostrar ao utilizador.
 *                 Null significa que não há nenhum erro.
 * @property searchQuery Texto introduzido pelo utilizador na barra de pesquisa.
 * @property filtroAtual O chip de filtro selecionado atualmente.
 * @property alertasAtivos Número de alertas ativos mostrado na TopBar.
 */

//#my_code
data class FerramentasUiState(
    val seccoes: List<SecaoFerramentas> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val filtroAtual: FiltroFerramenta = FiltroFerramenta.TODAS,
    val alertasAtivos: Int = 0
)


/**
 * Em que situação está a ferramenta neste momento.
 *
 * É isto que decide a cor e o texto da "pill" no card, e também o que cada
 * chip de filtro vai procurar. Há um valor para cada uma das 4 pills do design.
 */
enum class DisponibilidadeFerramenta { DISPONIVEL, REQUISITADA, EM_FALTA, EM_MANUTENCAO, RESERVADA }


/**
 * A condição física da ferramenta: se está boa, partida, ou já abatida.
 *
 * Não aparece na pill deste ecrã, mas é precisa para a gestão
 * (avaliar reparação ou abate).
 */
enum class EstadoFerramenta { OPERACIONAL, DANIFICADA, ABATIDA }


/**
 * Os chips de filtro no topo da lista. O `label` é o texto mostrado no chip.
 */
enum class FiltroFerramenta(val label: String) {
    TODAS("Todas"),
    DISPONIVEL("Disponível"),
    EM_USO("Em Uso"),
    EM_FALTA("Em Falta"),
    MANUTENCAO("Manutenção"),
    RESERVADA("Reservada")
}

/**
 * Uma ferramenta, já pronta para ser mostrada num card.
 *
 * Os dados crus vêm do backend (FerramentaDto) e são traduzidos para aqui no
 * mapper (toGestorUi), para o ecrã não ter de saber nada das strings da API.
 *
 * @property idFerramenta Identificador único. Serve de "key" na lista e para gerar o código.
 * @property codigo Código já formatado para mostrar, ex: "600001".
 * @property nome Nome da ferramenta, ex: "Chave de Caixa 10mm".
 * @property categoria Tipo da ferramenta, ex: "Chaves". É por aqui que a lista é agrupada.
 * @property localizacao Onde a ferramenta mora, ex: "Arm. 1".
 * @property disponibilidade Situação atual — decide a pill e responde aos filtros.
 * @property estado Condição física (operacional / danificada / abatida).
 * @property funcionario Quem tem a ferramenta neste momento. Null quando está no armário.
 */
data class FerramentaUi(
    val idFerramenta: Int,
    val codigo: String,
    val nome: String,
    val categoria: String,
    val localizacao: String,
    val disponibilidade: DisponibilidadeFerramenta,
    val estado: EstadoFerramenta,
    val funcionario: String? = null
)

/**
 * Um grupo de ferramentas da mesma categoria.
 *
 * A lista do ecrã é uma lista destas secções: cada uma vira um cabeçalho fixo
 * (sticky header) com a categoria, seguido dos cards das suas ferramentas.
 *
 * @property categoria Nome da categoria mostrado no cabeçalho, ex: "Chaves".
 * @property ferramentas As ferramentas dessa categoria, já filtradas.
 */
data class SecaoFerramentas(
    val categoria: String,
    val ferramentas: List<FerramentaUi>
)