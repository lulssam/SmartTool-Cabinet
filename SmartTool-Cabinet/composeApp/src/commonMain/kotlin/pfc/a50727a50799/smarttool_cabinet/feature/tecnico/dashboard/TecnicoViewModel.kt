package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.toTecnicoUi
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
/**
 * Gere toda a informação apresentada no dashboard do técnico.
 *
 * É responsável por obter as ferramentas associadas ao técnico,
 * preparar os dados necessários para o ecrã e informar quando
 * existe algum problema durante o carregamento.
 *
 * @param ferramentas Fonte de dados utilizada para obter as ferramentas.
 * @param idTecnico Identificador do técnico autenticado.
 * @param nomeTecnico Nome do técnico apresentado no dashboard.
 * @param turno Turno de trabalho do técnico.
 */
class TecnicoViewModel(
    private val ferramentas: FerramentaRemoteDataSource,
    private val idTecnico: Int,
    private val nomeTecnico: String,
    private val turno: String
) : ViewModel() {
    /**
     * Guarda o estado atual do dashboard do técnico.
     * Sempre que este valor é atualizado, o ecrã apresenta
     * automaticamente a informação mais recente.
     */
    private val _state = MutableStateFlow(TecnicoUiState())
    /**
     * Estado que pode ser observado pelo ecrã para mostrar
     * a informação atual ao utilizador.
     */
    val state: StateFlow<TecnicoUiState> = _state.asStateFlow()

    init {
        carregar()
    }
    /**
     * Obtém as ferramentas do técnico e atualiza a informação
     * apresentada no dashboard.
     *
     * Enquanto os dados estão a ser carregados, o estado indica
     * que o ecrã deve mostrar um indicador de carregamento.
     * Caso aconteça algum erro, é guardada uma mensagem para apresentar.
     */
    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val r = ferramentas.getFerramentaTecnico(idTecnico)) {
                is ApiResult.Success -> {
                    val lista = r.data.map { it.toTecnicoUi() }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            nomeTecnico = nomeTecnico,
                            cargo = "Técnico",
                            turno = turno,
                            minhasFerramentas = lista,
                            ferramentasEmUso = lista.size,
                            ferramentasParaDevolver = lista.size
                        )
                    }
                }

                is ApiResult.Error -> _state.update {
                    it.copy(isLoading = false, error = mensagem(r.error))
                }
            }
        }
    }

    /**
     * Converte um erro recebido durante a comunicação com o servidor
     * numa mensagem simples para apresentar ao utilizador.
     *
     * @param erro Erro que ocorreu durante o pedido.
     * @return Mensagem que será apresentada no ecrã.
     */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}