package pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard

import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaDto

/**
 * Tudo o que o ecrã do Gestor precisa para se mostrar.
 *
 * @property ferramentas A lista de ferramentas vinda do backend.
 * @property isLoading True enquanto esperamos pelos dados.
 * @property error Mensagem a mostrar se algo correr mal. Null = sem erro.
 */

//#my_code
data class GestorUiState(
    val ferramentas: List<FerramentaDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val estatisticas: EstatisticasFerramentas = EstatisticasFerramentas(
        0, 0, 0, 0
    ),
    val armarios: List<ArmarioUi> = emptyList(),
    val alertas: List<AlertaUi> = emptyList(),
    val nomeGestor: String = "",
    val turno: String = ""
)

data class EstatisticasFerramentas(
    val disponiveis: Int,
    val requisitada: Int,
    val indisponivel: Int,
    val manutencao: Int
) {
    val total get() = disponiveis + requisitada + indisponivel + manutencao
}

enum class EstadoArmario { OPERACIONAL, AVARIADO, ALERTA }

data class ArmarioUi(
    val nome: String,
    val slotsOcupados: Int,
    val slotsTotal: Int,
    val trancado: Boolean,
    val emFalta: Int,
    val estadoArmario: EstadoArmario
)

enum class Gravidade { CRITICO, AVISO, }

data class AlertaUi(
    val titulo: String,
    val descricao: String,
    val hora: String,
    val gravidade: Gravidade
)