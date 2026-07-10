package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.tarefas

import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.TarefaUi

//#my_code
/**
 * Tudo o que o ecrã TarefasTecnico precisa para se mostrar corretamente.
 *
 * É imutável — quando algo muda, cria-se uma cópia nova com `.copy()`.
 * O Compose deteta essas mudanças e redesenha apenas o necessário.
 *
 * @property isLoading True enquanto estamos à espera de dados do backend.
 *                     O ecrã mostra um indicador de carregamento durante este tempo.
 * @property error Mensagem de erro para mostrar ao utilizador.
 *                 Null significa que não há nenhum erro.
 */
data class TarefasTecnicoUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val turno: String = "",
    val mensagem: String? = null,
    val filtroAtual: FiltroTarefa = FiltroTarefa.TODAS,
    val tarefas: List<TarefaUi> = emptyList(),
    val expandidas: Set<String> = emptySet(),
    val nPendentes: Int = 0,
    val nEmCurso: Int = 0,
    val nConcluidas: Int = 0,
)
/**
 * Representa os filtros disponíveis para a lista de tarefas.
 *
 * Cada valor corresponde a um estado pelo qual as tarefas
 * podem ser filtradas no ecrã.
 */
enum class FiltroTarefa(val label: String) {
    TODAS("Todas"),
    PENDENTES("Pendentes"),
    EM_CURSO("Em Curso"),
    CONCLUIDAS("Concluídas")
}
