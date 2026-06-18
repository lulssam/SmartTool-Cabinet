package pfc.a50727a50799.smarttool_cabinet.feature.login

import pfc.a50727a50799.smarttool_cabinet.core.auth.Session

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessao: Session? = null
)
