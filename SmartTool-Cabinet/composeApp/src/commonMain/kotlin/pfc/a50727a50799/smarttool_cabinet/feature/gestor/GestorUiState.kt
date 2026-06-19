package pfc.a50727a50799.smarttool_cabinet.feature.gestor

import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaDto

/**
 * Tudo o que o ecrã do Gestor precisa para se mostrar.
 *
 * @property ferramentas A lista de ferramentas vinda do backend.
 * @property isLoading True enquanto esperamos pelos dados.
 * @property error Mensagem a mostrar se algo correr mal. Null = sem erro.
 */
data class GestorUiState(
    val ferramentas: List<FerramentaDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
