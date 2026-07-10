package pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.alerta.AlertaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.toGestorUi
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
import pfc.a50727a50799.smarttool_cabinet.core.tarefa.FerramentaIdDto
import pfc.a50727a50799.smarttool_cabinet.core.tarefa.NovaTarefaDto
import pfc.a50727a50799.smarttool_cabinet.core.tarefa.TarefaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.tarefa.toGestorUi
import pfc.a50727a50799.smarttool_cabinet.core.tecnico.TecnicoRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.tecnico.toUi

/**
 * Gere o estado e a lógica do ecrã TarefasGestor.
 *
 * Vai buscar os dados ao backend e guarda-os no estado para o ecrã mostrar.
 * O ecrã nunca fala diretamente com o backend — passa sempre por aqui.
 */
class TarefasGestorViewModel(
    private val tarefas: TarefaRemoteDataSource,
    private val alertas: AlertaRemoteDataSource,
    private val ferramentas: FerramentaRemoteDataSource,
    private val tecnicos: TecnicoRemoteDataSource,
    private val idGestor: Int
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
    /**
     * Abre o diálogo para criar uma nova tarefa.
     *
     * Além de mostrar o formulário, carrega do backend as ferramentas disponíveis
     * e a lista de técnicos para que possam ser selecionados.
     */
    fun abrirNovaTarefa() {
        _state.update { it.copy(mostrarNovaTarefa = true) }
        viewModelScope.launch {

            // carrega ferramentas
            val ferrs = when (val r = ferramentas.getFerramentas()) {
                is ApiResult.Success -> r.data
                    .filter { it.disponibilidade == "Disponivel" && it.estado == "Operacional"}
                    .map { it.toGestorUi() }
                is ApiResult.Error -> emptyList()
            }

            // carregar tecnicos
            val tecs = when (val r = tecnicos.getTecnicos()) {
                is ApiResult.Success -> r.data.map { it.toUi() }
                is ApiResult.Error -> emptyList()
            }

            println("tecs: $tecs")

            _state.update { it.copy(ferramentasDisponiveis = ferrs, tecnicos = tecs) }
        }
    }
    /**
     * Fecha o diálogo de criação de tarefa.
     */
    fun fecharNovaTarefa() {
        _state.update { it.copy(mostrarNovaTarefa = false) }
    }
    /**
     * Atualiza o título da nova tarefa.
     */
    fun onTituloChange(newTitulo: String) = _state.update { it.copy(titulo = newTitulo) }
    /**
     * Atualiza a descrição da nova tarefa.
     */
    fun onDescricaoChange(newDescricao: String) =
        _state.update { it.copy(descricao = newDescricao) }
    /**
     * Guarda o técnico atualmente selecionado.
     */

    fun onTecnicoSelecionado(newTecnico: Int?) =
        _state.update { it.copy(tecnicoSelecionado = newTecnico) }
    /**
     * Atualiza a prioridade escolhida para a nova tarefa.
     */
    fun onNovaPrioridade(p: PrioridadeTarefa) = _state.update { it.copy(novaPrioridade = p) }

    /**
     * Atualiza o texto de pesquisa das ferramentas.
     *
     * O ecrã utiliza este valor para filtrar a lista apresentada ao utilizador.
     */
    fun onQueryFerramentasChange(newQuery: String) =
        _state.update { it.copy(queryFerramentas = newQuery) }
    /**
     * Adiciona ou remove uma ferramenta da seleção.
     *
     * Se a ferramenta já estiver selecionada é removida; caso contrário,
     * passa a fazer parte da nova tarefa.
     */

    fun onToggleFerramenta(id: Int) = _state.update {
        val novo = if (id in it.ferramentasSelecionadas) it.ferramentasSelecionadas - id
        else it.ferramentasSelecionadas + id
        it.copy(ferramentasSelecionadas = novo)
    }
    /**
     * Cria uma nova tarefa no backend.
     *
     * Valida os dados obrigatórios, constrói o DTO com a informação do formulário
     * e envia o pedido ao servidor. Em caso de sucesso, fecha o diálogo e
     * recarrega a lista de tarefas.
     */
    fun onAdicionarTarefa() {
        val s = _state.value
        val idTecnico = s.tecnicoSelecionado ?: return
        if (s.titulo.isBlank()) return
        viewModelScope.launch {
            val dto = NovaTarefaDto(
                idGestor = idGestor,
                idTecnico = idTecnico,
                titulo = s.titulo,
                descricao = s.descricao,
                prioridade = s.novaPrioridade.name,
                ferramentasPermitidasIds = s.ferramentasSelecionadas.map {
                    FerramentaIdDto(codigoTipo = it / 100000, nFerramenta = it % 100000)
                }
            )

            when (tarefas.criarTarefa(dto)) {
                is ApiResult.Success -> {
                    fecharNovaTarefa();
                    carregar()
                }

                is ApiResult.Error ->
                    _state.update { it.copy(error = "Não foi possível criar a tarefa") }
            }
        }
    }
}


