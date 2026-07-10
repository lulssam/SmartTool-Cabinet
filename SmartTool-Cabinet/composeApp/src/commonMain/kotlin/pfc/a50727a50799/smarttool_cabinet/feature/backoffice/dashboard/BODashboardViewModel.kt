package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.SessionManager
import pfc.a50727a50799.smarttool_cabinet.core.backoffice.BackOfficeRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
/**
 * Responsável pela lógica do dashboard do Back Office.
 *
 * Obtém os dados necessários ao dashboard, calcula as
 * estatísticas e atualiza o estado observado pela interface.
 */
class BODashboardViewModel(
    private val backOfficeDataSource: BackOfficeRemoteDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(BODashboardUiState())
    val state: StateFlow<BODashboardUiState> = _state.asStateFlow()

    init {
        carregar()
    }
    /**
     * Carrega os dados do dashboard.
     *
     * Obtém a sessão atual, os funcionários e os armários,
     * calcula as estatísticas e atualiza o estado da interface.
     */
    fun carregar() {
        val sessao = SessionManager.atual

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    nomeBackOffice = sessao?.nome ?: "Administrador",
                    cargo = "Back Office",
                    turno = sessao?.turno?.lowercase()?.replaceFirstChar { c -> c.uppercase() } ?: "Manhã"
                )
            }


            val deferredFuncionarios = async { backOfficeDataSource.getFuncionarios() }
            val deferredArmarios = async { backOfficeDataSource.getArmarios() }

            val rFuncionarios = deferredFuncionarios.await()
            val rArmarios = deferredArmarios.await()

            if (rFuncionarios is ApiResult.Success && rArmarios is ApiResult.Success) {
                val funcs = rFuncionarios.data
                val armarios = rArmarios.data
                val total = funcs.size
                val ativos = funcs.count { it.ativo }
                val gestores = funcs.count { it.cargo?.uppercase() == "GESTOR" }
                val tecnicos = funcs.count { it.cargo?.uppercase() == "TECNICO" }
                val recentesUi = funcs.takeLast(4).reversed().map { dto ->
                    UtilizadorRecenteUi(
                        id = dto.idFunc,
                        nome = dto.nome,
                        iniciais = getIniciais(dto.nome),
                        cargoSubtitulo = "${formatarCargo(dto.cargo)} · Turno ${formatarTurno(dto.turno)}",
                        cargoTag = formatarCargo(dto.cargo)
                    )
                }
                val armariosUi = armarios.map { dto ->
                    ArmarioResumoUi(
                        id = dto.nArmario,
                        nome = "Armário ${dto.nArmario}",
                        estado = dto.estado
                    )
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                        estatisticas = EstatisticasBOUi(total, ativos, gestores, tecnicos),
                        utilizadoresRecentes = recentesUi,
                        armarios = armariosUi
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false, error = "Erro ao carregar os dados do Dashboard.") }
            }
        }
    }
    /**
     * Gera as iniciais de um utilizador a partir do nome.
     */
    private fun getIniciais(nome: String): String {
        val partes = nome.trim().split(" ")
        return if (partes.size >= 2) "${partes.first().first()}${partes.last().first()}".uppercase()
        else nome.take(2).uppercase()
    }
    /**
     * Converte o cargo recebido da base de dados
     * para um formato legível pela interface.
     */
    private fun formatarCargo(cargoDB: String?): String = when (cargoDB?.uppercase()) {
        "GESTOR" -> "Gestor"
        "TECNICO" -> "Técnico"
        "BACKOFFICE" -> "Back Office"
        else -> "Sem Cargo"
    }
    /**
     * Formata o nome do turno para apresentação
     * na interface.
     */

    private fun formatarTurno(turnoDB: String?): String {
        if (turnoDB == null) return "Sem Turno"
        return turnoDB.lowercase().replaceFirstChar { it.uppercase() }
    }
}