package pfc.a50727a50799.smarttool_cabinet.feature.gestor.historico

/**
 * Tudo o que o ecrã HistoricoGestor precisa para se mostrar corretamente.
 *
 * É imutável — quando algo muda, cria-se uma cópia nova com `.copy()`.
 * O Compose deteta essas mudanças e redesenha apenas o necessário.
 *
 * @property secoes Lista de todos os movimentos.
 * @property isLoading True enquanto estamos à espera de dados do backend.
 *                     O ecrã mostra um indicador de carregamento durante este tempo.
 * @property error Mensagem de erro para mostrar ao utilizador.
 *                 Null significa que não há nenhum erro.
 * @property alertasAtivos Número de alertas ativos.
 */

//#my_code
data class HistoricoGestorUiState(
    val secoes: List<SecaoHistoricoUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val alertasAtivos: Int = 0
)

enum class TipoMovimento { RETIROU, DEVOLVEU, MARCOU_AVARIA }

data class SecaoHistoricoUi(
    val data: String,
    val movimentos: List<HistoricoItemUi> = emptyList(),
)

data class HistoricoItemUi(
    val id: String,
    val nomeFerramenta: String,
    val funcionario: String,
    val hora: String,
    val tipo: TipoMovimento
)