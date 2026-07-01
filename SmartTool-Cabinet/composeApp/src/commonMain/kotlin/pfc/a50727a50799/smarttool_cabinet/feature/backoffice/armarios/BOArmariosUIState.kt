package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.armarios

import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.ArmarioUi

enum class FiltroBOArmario(val label: String) {
    TODOS("Todos"),
    ONLINE("Online"),
    ALERTA("Alerta"),
    OFFLINE("Offline")
}

data class BOArmariosUiState(
    val armarios: List<ArmarioUi> = emptyList(),
    val searchQuery: String = "",
    val filtroAtual: FiltroBOArmario = FiltroBOArmario.TODOS,
    val alertasAtivos: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val armariosFiltrados: List<ArmarioUi>
        get() {
            var lista = armarios

            if (searchQuery.isNotBlank()) {
                lista = lista.filter { it.nome.contains(searchQuery, ignoreCase = true) }
            }

            return when (filtroAtual) {
                FiltroBOArmario.TODOS -> lista
                FiltroBOArmario.ONLINE -> lista.filter { it.estadoArmario.name == "OPERACIONAL" }
                FiltroBOArmario.ALERTA -> lista.filter { it.estadoArmario.name == "ALERTA" }
                FiltroBOArmario.OFFLINE -> lista.filter { it.estadoArmario.name == "AVARIADO" }
            }
        }
}