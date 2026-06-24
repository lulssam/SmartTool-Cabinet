package pfc.a50727a50799.smarttool_cabinet.feature.gestor

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
 * Trata da lógica do ecrã do Gestor: vai buscar as ferramentas ao backend
 * e guarda o resultado no estado para o ecrã mostrar.
 *
 * @param ferramentas A fonte que sabe ir buscar as ferramentas ao backend.
 */
class GestorViewModel(
    private val ferramentas: FerramentaRemoteDataSource,
    private val armarios: ArmarioRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(GestorUiState())
    val state: StateFlow<GestorUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val lista = when (val r = ferramentas.getFerramentas()) {
                is ApiResult.Success -> r.data
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = mensagem(r.error)
                        )
                    }; return@launch
                }
            }

            val idsEmFalta = when (val r = ferramentas.getEmFalta()) {
                is ApiResult.Success -> r.data.map { it.idFerramenta }.toSet()
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = mensagem(r.error)
                        )
                    }; return@launch
                }
            }

            val stats = EstatisticasFerramentas(
                disponiveis = lista.count { it.disponibilidade == "Disponivel" },
                requisitada = lista.count { it.disponibilidade == "Requisitada" && it.idFerramenta !in idsEmFalta },
                emFalta = lista.count { it.disponibilidade == "Requisitada" && it.idFerramenta in idsEmFalta },
                manutencao = lista.count { it.disponibilidade == "Em Manutencao" }
            )
            _state.update { it.copy(ferramentas = lista, estatisticas = stats) }

            // armários
            when (val r = armarios.getArmarios()) {
                is ApiResult.Success ->
                    _state.update { it.copy(armarios = r.data.map { dto -> dto.toUi() }) }

                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }


    /** Transforma o erro tipado numa frase legível para o utilizador. */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }

}