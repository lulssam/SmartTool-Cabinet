package pfc.a50727a50799.smarttool_cabinet.feature.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthRepository
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthResult

/**
 * Decide, no arranque da aplicação, se já existe alguém com sessão iniciada.
 * Pergunta ao [AuthRepository] assim que é criado e guarda a resposta em [state],
 * para o ecrã principal saber se mostra o login ou salta direto para o ecrã do cargo.
 *
 * @property authRepository Quem sabe responder se há sessão e qual é o cargo da pessoa.
 */
class SessionViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    /** O estado atual do arranque: a carregar, sem sessão, ou com sessão iniciada. */
    private val _state = MutableStateFlow<SessionUiState>(SessionUiState.Loading)
    val state: StateFlow<SessionUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = when (val r = authRepository.getSession()) {
                is AuthResult.Success ->
                    if (r.data != null) SessionUiState.Authenticated(r.data)
                    else SessionUiState.NoSession

                is AuthResult.Error -> SessionUiState.NoSession
            }
        }
    }
}