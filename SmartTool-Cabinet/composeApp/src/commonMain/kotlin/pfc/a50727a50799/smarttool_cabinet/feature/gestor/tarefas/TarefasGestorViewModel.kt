package pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.alerta.AlertaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
import pfc.a50727a50799.smarttool_cabinet.core.tarefa.TarefaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.tarefa.toGestorUi

/**
 * Gere o estado e a lógica do ecrã TarefasGestor.
 *
 * Vai buscar os dados ao backend e guarda-os no estado para o ecrã mostrar.
 * O ecrã nunca fala diretamente com o backend — passa sempre por aqui.
 */
class TarefasGestorViewModel(
    private val tarefas: TarefaRemoteDataSource,
    private val alertas: AlertaRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(TarefasGestorUiState(isLoading = true))

    /**
     * O estado atual do ecrã, disponível para o Composable observar.
     * Só o ViewModel pode alterar este valor — o ecrã apenas o lê.
     */
    val state: StateFlow<TarefasGestorUiState> = _state.asStateFlow()
    private var todas: List<TarefaUi> = emptyList()

    init {
        carregar()
    }

    /** Vai buscar os dados ao backend e atualiza o estado do ecrã. */
    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            todas = when (val r = tarefas.getTarefas()) {
                is ApiResult.Success -> r.data.map { it.toGestorUi() }
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = "Não foi possível carregar as tarefas")
                    }
                    return@launch
                }
            }

            val nAlertas = when (val r = alertas.getAlertas()) {
                is ApiResult.Success -> r.data.size
                is ApiResult.Error -> 0
            }

            _state.update { it.copy(isLoading = false, alertasAtivos = nAlertas) }
            recomputar()
        }
    }

    /** Troca o chip de filtro e recalcula a lista visível. */
    fun onFiltroChange(filtro: FiltroTarefa) {
        _state.update { it.copy(filtroAtual = filtro) }
        recomputar()
    }

    /**
     * Aplica o filtro atual à lista crua. Corre no arranque e sempre que o
     * filtro muda — nunca vai à rede, só reorganiza o que já está em memória.
     */
    private fun recomputar() {
        val filtro = _state.value.filtroAtual
        val visiveis = todas.filter { t ->
            when (filtro) {
                FiltroTarefa.TODAS -> true
                FiltroTarefa.PENDENTES -> t.estado == EstadoTarefa.PENDENTE
                FiltroTarefa.EM_CURSO -> t.estado == EstadoTarefa.EM_CURSO
                FiltroTarefa.CONCLUIDAS -> t.estado == EstadoTarefa.CONCLUIDA
            }
        }
        _state.update { it.copy(tarefas = visiveis) }
    }
}