package pfc.a50727a50799.smarttool_cabinet.feature.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthError
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthRepository
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthResult

/**
 * Trata da lógica do login. Fala com o [AuthRepository] (a interface),
 * nunca diretamente com o Firebase.
 */
class WelcomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WelcomeUiState())
    val state: StateFlow<WelcomeUiState> = _state.asStateFlow()

    fun onEmailChange(novo: String) = _state.update { it.copy(email = novo) }
    fun onPasswordChange(nova: String) = _state.update { it.copy(password = nova) }

    /** Chamado quando o user carrega em "Entrar". */
    fun onLoginClick() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, sessao = null) }
            val atual = _state.value
            when (val r = authRepository.loginEmail(atual.email, atual.password)) {
                is AuthResult.Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        sessao = r.data
                    )
                }

                is AuthResult.Error -> _state.update {
                    it.copy(
                        isLoading = false,
                        error = mensagem(r.error)
                    )
                }
            }
        }
    }

    /** Chamado quando o user carrega em "Entrar Com Google". */
    fun onGoogleToken(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, sessao = null) }

            when (val r = authRepository.loginGoogle(idToken = idToken)) {
                is AuthResult.Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        sessao = r.data
                    )
                }
                is AuthResult.Error -> _state.update {
                    it.copy(
                        isLoading = false,
                        error = mensagem(r.error)
                    )
                }
            }
        }
    }

    /** Transforma o erro tipado numa frase legível para o user. */
    private fun mensagem(erro: AuthError): String = when (erro) {
        AuthError.InvalidCredentials -> "Email ou password errados"
        AuthError.RoleNotFound -> "Email não registado no sistema"
        AuthError.NetworkError -> "Não foi possível contactar o servidor"
        is AuthError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}