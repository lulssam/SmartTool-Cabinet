package pfc.a50727a50799.smarttool_cabinet.feature.gestor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.alerta.AlertaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.alerta.toUi
import pfc.a50727a50799.smarttool_cabinet.core.armario.ArmarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.armario.toUi
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

/**
 * Trata da lógica do ecrã do Gestor: vai buscar ao backend as ferramentas,
 * os armários e os alertas, e guarda tudo no estado para o ecrã mostrar.
 *
 * @param ferramentas Fonte das ferramentas (estatísticas).
 * @param armarios Fonte dos armários.
 * @param alertas Fonte dos alertas.
 * @param nomeGestor Nome de quem tem sessão (vem do SessionManager).
 * @param turno Turno de quem tem sessão (vem do SessionManager).
 */
class GestorViewModel(
    private val ferramentas: FerramentaRemoteDataSource,
    private val armarios: ArmarioRemoteDataSource,
    private val alertas: AlertaRemoteDataSource,
    nomeGestor: String,
    turno: String
) : ViewModel() {

    private val _state = MutableStateFlow(GestorUiState(nomeGestor = nomeGestor, turno = turno))
    val state: StateFlow<GestorUiState> = _state.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            // ferramentas
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

            val stats = EstatisticasFerramentas(
                disponiveis = lista.count { it.disponibilidade == "Disponivel" },
                requisitada = lista.count { it.disponibilidade == "Requisitada" },
                manutencao = lista.count { it.disponibilidade == "Em Manutencao" },
                indisponivel = lista.count { it.disponibilidade == "Indisponivel" }
            )
            _state.update { it.copy(ferramentas = lista, estatisticas = stats) }

            val ferramentasPorArmario = lista.groupBy { it.localizacao }

            // armários
            when (val r = armarios.getArmarios()) {
                is ApiResult.Success ->
                    _state.update { it.copy(armarios = r.data.map { dto ->
                        val doArmario = ferramentasPorArmario["Arm. ${dto.nArmario}"].orEmpty()
                        dto.toUi(
                            slotsOcupados = doArmario.count {f -> f.disponibilidade == "Disponivel"},
                            emFalta = doArmario.count {f -> f.disponibilidade == "Requisitada"}
                        ) }) }

                is ApiResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = mensagem(r.error)) }
                    return@launch
                }
            }

            // alertas
            when (val r = alertas.getAlertas()) {
                is ApiResult.Success ->
                    _state.update { it.copy(alertas = r.data.map { dto -> dto.toUi() }) }

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