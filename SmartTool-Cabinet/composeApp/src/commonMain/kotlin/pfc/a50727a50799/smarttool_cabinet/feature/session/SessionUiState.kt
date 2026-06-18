package pfc.a50727a50799.smarttool_cabinet.feature.session

import pfc.a50727a50799.smarttool_cabinet.core.auth.Session


/**
 * Os estados possíveis enquanto a app arranca e tenta descobrir
 * se já existe alguém com sessão iniciada
 * */
sealed interface SessionUiState {
    /** Ainda estamos à espera da resposta do getSession() —> mostra-se o splash. */
    data object Loading : SessionUiState

    /** Não há sessão guardada —> o utilizador tem de fazer login. */
    data object NoSession : SessionUiState

    /**
     * Já existe sessão — vamos direto ao ecrã do cargo desta pessoa.
     *
     * @property session Os dados de quem tem a sessão (id, email e cargo),
     *                   usados para escolher o ecrã certo.
     */
    data class Authenticated(val session: Session) : SessionUiState
}