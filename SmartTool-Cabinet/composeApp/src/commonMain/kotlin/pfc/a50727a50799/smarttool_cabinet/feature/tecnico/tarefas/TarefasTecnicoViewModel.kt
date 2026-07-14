package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.tarefas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
import pfc.a50727a50799.smarttool_cabinet.core.tarefa.TarefaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.tarefa.toGestorUi
import pfc.a50727a50799.smarttool_cabinet.core.tarefa.toTecnicoUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.EstadoTarefa
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.TarefaUi

/**
 * Gere o estado e a lógica do ecrã TarefasTecnico.
 *
 * Vai buscar os dados necessários ao backend e guarda-os no estado
 * para o ecrã mostrar. O ecrã nunca fala diretamente com o backend —
 * passa sempre por aqui.
 */
class TarefasTecnicoViewModel(
    private val tarefas: TarefaRemoteDataSource,
    private val idTecnico: Int,
    turno: String
) : ViewModel() {

    private val _state = MutableStateFlow(TarefasTecnicoUiState(isLoading = true, turno = turno))

    /**
     * O estado atual do ecrã, disponível para o Composable observar.
     * Só o ViewModel pode alterar este valor — o ecrã apenas o lê.
     */
    val state: StateFlow<TarefasTecnicoUiState> = _state.asStateFlow()

    private var todasTarefas: List<TarefaUi> = emptyList()

    init {
        carregar()
    }


    /**
     * Vai buscar os dados ao backend e atualiza o estado do ecrã.
     *
     * Mostra um indicador de carregamento enquanto espera,
     * e um erro se algo correr mal.
     */
    fun carregar(isRefresh: Boolean = false) {
        if (idTecnico == -1) {
            _state.update { it.copy(isLoading = false, error = "Sessão inválida") }
            return
        }
        viewModelScope.launch {
            _state.update {
                if (isRefresh) it.copy(isRefreshing = true, error = null)
                else it.copy(isLoading = true, error = null)
            }

            todasTarefas = when (val r = tarefas.getTarefasTecnico(idTecnico)) {
                is ApiResult.Success -> r.data.map { it.toTecnicoUi() }
                is ApiResult.Error -> {
                    _state.update {
                        if (isRefresh) it.copy(isRefreshing = false, mensagem = "Não foi possível atualizar")
                        else it.copy(isLoading = false, error = "Não foi possível carregar as tarefas")
                    }
                    return@launch
                }
            }

            _state.update {
                it.copy(
                    nPendentes = todasTarefas.count { t -> t.estado == EstadoTarefa.PENDENTE },
                    nEmCurso = todasTarefas.count { t -> t.estado == EstadoTarefa.EM_CURSO },
                    nConcluidas = todasTarefas.count { t -> t.estado == EstadoTarefa.CONCLUIDA },
                )
            }
            recomputar()

            _state.update { it.copy(isLoading = false, isRefreshing = false) }
        }
    }

    fun onFiltroChange(filtroTarefa: FiltroTarefa) {
        _state.update { it.copy(filtroAtual = filtroTarefa) }
        recomputar()
    }

    /**
     * Aplica o chip atual à lista*/
    private fun recomputar() {
        val filtro = _state.value.filtroAtual
        val visiveis = todasTarefas.filter { t ->
            when (filtro) {
                FiltroTarefa.TODAS -> true
                FiltroTarefa.PENDENTES -> t.estado == EstadoTarefa.PENDENTE
                FiltroTarefa.EM_CURSO -> t.estado == EstadoTarefa.EM_CURSO
                FiltroTarefa.CONCLUIDAS -> t.estado == EstadoTarefa.CONCLUIDA
            }
        }

        _state.update { it.copy(tarefas = visiveis) }
    }


    fun refresh() = carregar(isRefresh = true)
    /**
     * Abre/fecha um card.
     * */
    fun onToggleExpandir(id: String) = _state.update {
        val novo = if (id in it.expandidas) it.expandidas - id else it.expandidas + id
        it.copy(expandidas = novo)
    }

    fun concluir(idTarefa: String) {
        viewModelScope.launch {
            when (val r = tarefas.concluirTarefa(idTarefa.toInt())) {
                is ApiResult.Success -> carregar()
                is ApiResult.Error -> {
                    val msg = when (val e = r.error) {
                        is ApiError.Unknown -> e.message ?: "Não foi possível concluir a tarefa"
                        ApiError.NetworkError -> "Sem ligação ao servidor"
                    }
                    _state.update { it.copy(mensagem = msg) }
                }
            }
        }
    }

    fun limparMensagem() =
        _state.update { it.copy(mensagem = null) } // chamado depois do snackbar aparecer
}

/**
 * Transforma um erro do backend numa frase legível para o utilizador.
 *
 * @param erro O erro devolvido pelo backend.
 * @return Uma mensagem em português para mostrar no ecrã.
 */
private fun mensagem(erro: ApiError): String = when (erro) {
    ApiError.NetworkError -> "Não foi possível contactar o servidor"
    is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
}
