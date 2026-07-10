package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.armarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.armario.ArmarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.armario.toUi
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
/**
 * Gere a lógica do ecrã de armários do Backoffice.
 *
 * Obtém os armários e as ferramentas do backend, calcula a ocupação
 * de cada armário e disponibiliza toda a informação necessária para
 * a interface através do estado.
 *
 * @param ferramentas Fonte de dados das ferramentas.
 * @param armarios Fonte de dados dos armários.
 */
class BOArmariosViewModel(
    private val ferramentas: FerramentaRemoteDataSource,
    private val armarios: ArmarioRemoteDataSource,

) : ViewModel() {

    private val _state = MutableStateFlow(BOArmariosUiState(isLoading = true))
    val state: StateFlow<BOArmariosUiState> = _state.asStateFlow()

    init {
        carregar()
    }
    /**
     * Carrega os dados necessários para o ecrã.
     *
     * Obtém a lista de ferramentas e de armários, calcula o número de
     * posições ocupadas e de ferramentas em falta em cada armário e
     * atualiza o estado apresentado pela interface.
     */
    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val lista = when (val r = ferramentas.getFerramentas()) {
                is ApiResult.Success -> r.data
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }
            val ferramentasPorArmario = lista.groupBy { it.localizacao }

            when (val r = armarios.getArmarios()) {
                is ApiResult.Success ->
                    _state.update { estado ->
                        estado.copy(
                            armarios = r.data.map { dto ->
                                val doArmario = ferramentasPorArmario["Arm. ${dto.nArmario}"].orEmpty()
                                dto.toUi(
                                    slotsOcupados = doArmario.count { f -> f.disponibilidade == "Disponivel" },
                                    emFalta = doArmario.count { f -> f.disponibilidade == "Requisitada" }
                                )
                            }
                        )
                    }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }
    /**
     * Atualiza o texto utilizado na pesquisa de armários.
     *
     * @param query Novo texto introduzido pelo utilizador.
     */
    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
    /**
     * Atualiza o filtro atualmente selecionado.
     *
     * @param filtro Novo filtro escolhido pelo utilizador.
     */
    fun onFiltroChange(filtro: FiltroBOArmario) {
        _state.update { it.copy(filtroAtual = filtro) }
    }
    /**
     * Converte um erro da camada de rede numa mensagem legível
     * para apresentar ao utilizador.
     *
     * @param erro Erro devolvido pela API.
     * @return Mensagem correspondente ao erro.
     */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}