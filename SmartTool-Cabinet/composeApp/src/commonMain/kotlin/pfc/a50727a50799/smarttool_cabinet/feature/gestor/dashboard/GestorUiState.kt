package pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard

import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaDto

/**
 * Tudo o que o ecrã do Gestor precisa para se mostrar.
 *
 * @property ferramentas A lista de ferramentas vinda do backend.
 * @property isLoading True enquanto esperamos pelos dados.
 * @property isRefreshing True enquanto o utilizador "puxa para atualizar"; a lista continua visível e mostra-se apenas o indicador do gesto no topo.
 * @property error Mensagem a mostrar se algo correr mal. Null = sem erro.
 */

//#my_code
data class GestorUiState(
    val ferramentas: List<FerramentaDto> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val estatisticas: EstatisticasFerramentas = EstatisticasFerramentas(
        0, 0, 0, 0
    ),
    val armarios: List<ArmarioUi> = emptyList(),
    val alertas: List<AlertaUi> = emptyList(),
    val nomeGestor: String = "",
    val turno: String = ""
)
/**
 * Representa as estatísticas das ferramentas apresentadas no dashboard.
 */
data class EstatisticasFerramentas(
    val disponiveis: Int,
    val requisitada: Int,
    val indisponivel: Int,
    val manutencao: Int
) {
    val total get() = disponiveis + requisitada + indisponivel + manutencao
}
/**
 * Estados possíveis de um armário.
 */
enum class EstadoArmario { OPERACIONAL, AVARIADO, ALERTA }
/**
 * Representa a informação resumida de um armário.
 */
data class ArmarioUi(
    val nome: String,
    val slotsOcupados: Int,
    val slotsTotal: Int,
    val trancado: Boolean,
    val emFalta: Int,
    val estadoArmario: EstadoArmario
)
/**
 * Níveis de gravidade de um alerta.
 */
enum class Gravidade { CRITICO, AVISO, }
/**
 * Representa um alerta apresentado no dashboard.
 */
data class AlertaUi(
    val titulo: String,
    val descricao: String,
    val hora: String,
    val gravidade: Gravidade
)