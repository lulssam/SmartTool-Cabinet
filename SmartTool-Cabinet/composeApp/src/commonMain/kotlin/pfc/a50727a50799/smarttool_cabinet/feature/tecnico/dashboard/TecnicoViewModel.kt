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
 * Trata da lógica do ecrã do Técnico.
 */
class TecnicoViewModel(
    private val ferramentas: FerramentaRemoteDataSource,
    private val idTecnico: Int,
    private val nomeTecnico: String,
    private val turno: String
) : ViewModel() {

    private val _state = MutableStateFlow(TecnicoUiState())
    val state: StateFlow<TecnicoUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // ferramentas já levantadas
            val listaEmUso = when (val r = ferramentas.getFerramentaTecnico(idTecnico)) {
                is ApiResult.Success -> r.data.map { it.toTecnicoUi() }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            // ferramentas reservadas para as tarefas do técnico
            val listaReservadas = when (val r = ferramentas.getReservadasTecnico(idTecnico)) {
                is ApiResult.Success -> r.data
                    .filter { it.disponibilidade == "Reservada" } // só as que ainda não foram levantadas
                    .map { it.toTecnicoUi() }
                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            _state.update {
                it.copy(
                    isLoading = false,
                    nomeTecnico = nomeTecnico,
                    cargo = "Técnico",
                    turno = turno,
                    minhasFerramentas = listaReservadas + listaEmUso, // reservadas aparecem primeiro
                    ferramentasEmUso = listaEmUso.size,
                    ferramentasReservadas = listaReservadas.size,
                    ferramentasParaDevolver = listaEmUso.size
                )
            }
        }
    }

    /** Transforma o erro tipado numa frase legível para o utilizador. */
    private fun mensagem(erro: ApiError): String = when (erro) {
        ApiError.NetworkError -> "Não foi possível contactar o servidor"
        is ApiError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}