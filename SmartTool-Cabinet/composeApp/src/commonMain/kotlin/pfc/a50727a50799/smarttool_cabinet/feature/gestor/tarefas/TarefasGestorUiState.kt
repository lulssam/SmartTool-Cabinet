package pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas

import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.FerramentaUi
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.TecnicoUiState

/**
 * Tudo o que o ecrã TarefasGestor precisa para se mostrar corretamente.
 *
 * É imutável — quando algo muda, cria-se uma cópia nova com `.copy()`.
 * O Compose deteta essas mudanças e redesenha apenas o necessário.
 *
 * @property isLoading True enquanto estamos à espera de dados do backend.
 *                     O ecrã mostra um indicador de carregamento durante este tempo.
 * @property error Mensagem de erro para mostrar ao utilizador.
 *                 Null significa que não há nenhum erro.
 */
data class TarefasGestorUiState(
    val tarefas: List<TarefaUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filtroAtual: FiltroTarefa = FiltroTarefa.TODAS,
    val alertasAtivos: Int = 0,

    // coisas do pop up da nova tarefa
    val mostrarNovaTarefa: Boolean = false,
    val titulo: String = "",
    val descricao: String = "",
    val tecnicoSelecionado: Int? = null,
    val novaPrioridade: PrioridadeTarefa = PrioridadeTarefa.NORMAL,
    val ferramentasSelecionadas: Set<Int> = emptySet(),
    val queryFerramentas: String = "",
    val tecnicos: List<TecnicoUi> = emptyList(),
    val ferramentasDisponiveis: List<FerramentaUi> = emptyList()
)

data class TecnicoUi(
    val id: Int,
    val nome: String,
    val turno: String,
    val disponivel: Boolean
) {

}

data class TarefaUi(
    val id: String,
    val titulo: String,
    val codigo: String,
    val quando: String,
    val tecnico: String,
    val estado: EstadoTarefa,
    val prioridade: PrioridadeTarefa,
    val ferramentas: List<String> = emptyList()  // nomes para os chips
)

enum class EstadoTarefa { PENDENTE, EM_CURSO, CONCLUIDA }
enum class PrioridadeTarefa(val label: String) { ALTA("Alta"), NORMAL("Normal"), BAIXA("Baixa") }

enum class FiltroTarefa(val label: String) {
    TODAS("Todas"),
    PENDENTES("Pendentes"),
    EM_CURSO("Em Curso"),
    CONCLUIDAS("Concluídas")
}

