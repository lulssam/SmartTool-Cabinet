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
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.SessionManager

/**
 * Gere a lógica de autenticação da aplicação.
 *
 * Este ViewModel mantém o estado do ecrã inicial e comunica
 * com o repositório responsável pelos processos de autenticação
 * por email e autenticação federada.
 *
 * @property authRepository Repositório utilizado para autenticar o utilizador.
 */
class WelcomeViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WelcomeUiState())
    /**
     * Estado observado pela interface.
     */
    val state: StateFlow<WelcomeUiState> = _state.asStateFlow()
    /**
     * Atualiza o endereço de correio eletrónico introduzido pelo utilizador.
     *
     * @param novo Novo endereço de correio eletrónico.
     */
    fun onEmailChange(novo: String) = _state.update { it.copy(email = novo) }
    /**
     * Atualiza a palavra-passe introduzida pelo utilizador.
     *
     * @param nova Nova palavra-passe.
     */
    fun onPasswordChange(nova: String) = _state.update { it.copy(password = nova) }

    /**
     * Inicia o processo de autenticação por email.
     *
     * Caso a autenticação seja bem-sucedida, a sessão é armazenada
     * e o estado da interface é atualizado. Em caso de erro, é
     * apresentada uma mensagem adequada ao utilizador.
     */
    fun onLoginClick() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, sessao = null) }
            val atual = _state.value
            when (val r = authRepository.loginEmail(atual.email, atual.password)) {
                is AuthResult.Success -> {
                    SessionManager.atual = r.data
                    println("LOGIN OK -> idFunc=${r.data.idFunc}, nome=${r.data.nome}, role=${r.data.role}")
                    _state.update { it.copy(isLoading = false, sessao = r.data) }
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

    /**
     * Inicia o processo de autenticação federada através
     * de um token de identidade.
     *
     * Caso a autenticação seja bem-sucedida, a sessão é armazenada
     * e o estado da interface é atualizado.
     *
     * @param idToken Token de autenticação devolvido pelo fornecedor de identidade.
     */
    fun onGoogleToken(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, sessao = null) }

            when (val r = authRepository.loginGoogle(idToken = idToken)) {
                is AuthResult.Success -> {
                    SessionManager.atual = r.data
                    _state.update { it.copy(isLoading = false, sessao = r.data) }
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

    /**
     * Converte um erro de autenticação numa mensagem
     * adequada para apresentar ao utilizador.
     *
     * @param erro Erro devolvido durante o processo de autenticação.
     * @return Mensagem correspondente ao erro.
     */
    private fun mensagem(erro: AuthError): String = when (erro) {
        AuthError.InvalidCredentials -> "Email ou password errados"
        AuthError.RoleNotFound -> "Email não registado no sistema"
        AuthError.NetworkError -> "Não foi possível contactar o servidor"
        is AuthError.Unknown -> erro.message ?: "Erro desconhecido"
    }
}