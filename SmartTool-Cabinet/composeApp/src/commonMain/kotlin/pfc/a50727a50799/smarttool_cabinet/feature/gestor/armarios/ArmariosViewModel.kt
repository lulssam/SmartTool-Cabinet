package pfc.a50727a50799.smarttool_cabinet.feature.gestor.armarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.alerta.AlertaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.armario.ArmarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.armario.toUi
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.ArmarioUi

/**
 * Lógica do ecrã de Armários. Vai buscar ao backend os armários e as
 * ferramentas (precisas das ferramentas para contar os Slots ocupados e
 * as que estão Em Falta por armário) e guarda tudo no estado.
 */
class ArmariosViewModel(
    private val ferramentas: FerramentaRemoteDataSource,
    private val armarios: ArmarioRemoteDataSource,
    private val alertas: AlertaRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(
        ArmariosUiState(isLoading = true)
    )

    val state: StateFlow<ArmariosUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    /**
     * @param isRefresh Verdadeiro quando vem do gesto "puxar para atualizar";
     *                  mantém a lista visível (usa isRefreshing em vez de isLoading).
     */
    fun carregar(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _state.update {
                if (isRefresh) it.copy(isRefreshing = true, error = null)
                else it.copy(isLoading = true, error = null)
            }

            // ferramentas agrupadas por localização
            val lista = when (val r = ferramentas.getFerramentas()) {
                is ApiResult.Success -> r.data
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, isRefreshing = false, error = mensagem(r.error)) }
                    return@launch
                }
            }
            val ferramentasPorArmario = lista.groupBy { it.localizacao }

            // armarios
            when (val r = armarios.getArmarios()) {
                is ApiResult.Success ->
                    _state.update { estado ->
                        estado.copy(
                            armarios = r.data.map { dto ->
                                val doArmario =
                                    ferramentasPorArmario["Arm. ${dto.nArmario}"].orEmpty()
                                dto.toUi(
                                    slotsOcupados = doArmario.count { f -> f.disponibilidade == "Disponivel" },
                                    emFalta = doArmario.count { f -> f.disponibilidade == "Requisitada" }
                                )
                            }
                        )
                    }

                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, isRefreshing = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            when (val r = alertas.getAlertas()) {
                is ApiResult.Success ->
                    _state.update { it.copy(alertasAtivos = r.data.size) }

                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, isRefreshing = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            _state.update { it.copy(isLoading = false, isRefreshing = false) }
        }
    }

    /**
     * Recarrega os armários em resposta ao gesto de "puxar para atualizar".
     * Mantém a lista visível e mostra o indicador do gesto no topo.
     */
    fun refresh() = carregar(isRefresh = true)

    /** Atualiza o texto da pesquisa (o ecrã chama isto a cada tecla).
     * @param query texto introduzido pelo user*/
    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    /**
     * Troca o chip do filtro selecionado.
     * @param filtro Chip selecionado.*/
    fun onFiltroChange(filtro: FiltroArmario) {
        _state.update { it.copy(filtroAtual = filtro) }
    }

    /** Transforma o erro tipado numa frase legível para o utilizador. */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}