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

class BOArmariosViewModel(
    private val ferramentas: FerramentaRemoteDataSource,
    private val armarios: ArmarioRemoteDataSource,

) : ViewModel() {

    private val _state = MutableStateFlow(BOArmariosUiState(isLoading = true))
    val state: StateFlow<BOArmariosUiState> = _state.asStateFlow()

    init {
        carregar()
    }

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

    fun onSearchChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onFiltroChange(filtro: FiltroBOArmario) {
        _state.update { it.copy(filtroAtual = filtro) }
    }

    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}