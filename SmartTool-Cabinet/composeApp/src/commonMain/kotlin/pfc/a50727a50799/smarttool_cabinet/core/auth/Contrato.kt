package pfc.a50727a50799.smarttool_cabinet.core.auth

/**
 * Os 3 tipos de users que existem no sistema.
 * Vem do campo "cargo" que o backend devolve depois do login.*/
enum class UserRole {
    GESTOR, BACKOFFICE, TECNICO
}

/**
 * Representa um user com sessão ativa na aplicação
 *
 * @property uid Identificador único do user, dado pelo Firebase.
 * @property email Email do user.
 * @property role Cargo do user obtido a partir do nosso backend.*/
data class Session(
    val uid: String,
    val email: String,
    val role: UserRole
)

/**
 * Lista das coisas que podem correr mal durante o login ou a obter a sessão*/
sealed interface AuthError {
    /** email ou pass estão erradas*/
    data object InvalidCredentials : AuthError

    /**o login no firebase funcionou mas o email não está registado*/
    data object RoleNotFound : AuthError

    /**não foi possível contactar o nosso backend*/
    data object NetworkError : AuthError

    /**qualquer outro erro*/
    data class Unknown(val message: String?) : AuthError
}

/**
 * Resultado de uma operção de autenticação: ou correu bem e trazemos [data]
 * ou correu mal e trazemos o motivo em [AuthError]*/
sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val error: AuthError) : AuthResult<Nothing>()
}

/**
 * descreve o que a app precisa de quem trata da autenticação.
 * o resto da app só conhece esta interface, não sabe se por trás está o firebase,
 * um server proprio ou qualque coisa*/
interface AuthRepository {
    /**
     * Tenta fazer login com email e pass
     *
     * @param email O email que o user meteu
     * @param password A pass que o user meteu
     * @return [AuthResult.Success] se correu bem e trazemos a [Session]
     * ou [AuthResult.Error] se correu mal e trazemos o motivo em [AuthError]
     * */
    suspend fun loginEmail(email: String, password: String): AuthResult<Session>

    /**
     * Tenta fazer login com o google, depois do sistema (Android/iOS)
     * já ter o token de identidade da google.
     *
     * @param idToken O token de identidade da google depois que o user escolher a conta
     * @return [AuthResult.Success] se correu bem e trazemos a [Session]
     * ou [AuthResult.Error] se correu mal e trazemos o motivo em [AuthError]*/
    suspend fun loginGoogle(idToken: String): AuthResult<Session>

    /**
     * Verifica se já existe alguem com sessão iniciada (de uma vez anterior),
     * e se sim, vai buscar o cargo dessa pessoa.
     *
     * @return [AuthResult.Success] com `null` se não há ninguém com sessão iniciada,
     * com a [Session] se há e foi possível confirmar o cargo, ou [AuthResult.Error]
     * se há sessão mas não foi possível confirmar o papel (ex: backend em baixo)
     * NÃO significa fazer login outra vez.
     * */
    suspend fun getSession(): AuthResult<Session?>


    /**
     * termina sessão atual*/
    fun logout()
}