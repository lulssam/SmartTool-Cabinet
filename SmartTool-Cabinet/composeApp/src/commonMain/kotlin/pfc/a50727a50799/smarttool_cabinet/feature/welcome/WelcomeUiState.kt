package pfc.a50727a50799.smarttool_cabinet.feature.welcome

import pfc.a50727a50799.smarttool_cabinet.core.auth.Session

//#my_code
data class WelcomeUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessao: Session? = null
)
