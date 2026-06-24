package pfc.a50727a50799.smarttool_cabinet.feature.login

import pfc.a50727a50799.smarttool_cabinet.core.auth.Session

/**
 * Tudo o que o ecrã de login precisa de saber para se mostrar.
 *
 * @property email O que o user já escreveu no campo do email.
 * @property password O que o user já escreveu no campo da password.
 * @property isLoading True enquanto esperamos a resposta do login.
 * @property error Mensagem a mostrar se algo correr mal. Null = sem erro.
 * @property sessao Preenchida quando o login corre bem (para confirmarmos que funcionou).
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessao: Session? = null
)
