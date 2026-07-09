package pfc.a50727a50799.smarttool_cabinet.feature.welcome

import pfc.a50727a50799.smarttool_cabinet.core.auth.Session

//#my_code
/**
 * Representa o estado da interface do ecrã inicial.
 *
 * Armazena os dados introduzidos pelo utilizador, o estado
 * do processo de autenticação e a sessão criada após um
 * login bem-sucedido.
 *
 * @property email Endereço de correio eletrónico introduzido pelo utilizador.
 * @property password Palavra-passe introduzida pelo utilizador.
 * @property isLoading Indica se o processo de autenticação está em execução.
 * @property error Mensagem de erro a apresentar, caso exista.
 * @property sessao Sessão do utilizador autenticado.
 */
data class WelcomeUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val sessao: Session? = null
)
