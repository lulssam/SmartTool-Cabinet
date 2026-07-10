package pfc.a50727a50799.smarttool_cabinet.core.auth

import pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario.FuncionarioRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario.toUserRole

/**
 * Trata do login e logout dos utilizadores.
 * Primeiro confirma a identidade da pessoa com o Firebase, e depois pergunta
 * ao nosso backend qual é o cargo dela (por exemplo, técnico ou gestor),
 * para sabermos o que ela pode ver e fazer na app.
 *
 * @property funcionarioDataSource Usado para perguntar ao backend qual é o cargo do utilizador.
 */
class FirebaseAuthRepository(
    private val funcionarioDataSource: FuncionarioRemoteDataSource
) : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    /**
     * Tenta fazer login com um email e uma password.
     *
     * @param email O email que o utilizador escreveu.
     * @param password A password que o utilizador escreveu.
     * @return Se correr bem, a sessão do utilizador já com o seu cargo.
     *         Se o email ou a password estiverem errados, um erro a dizer isso.
     */
    override suspend fun loginEmail(email: String, password: String): AuthResult<Session> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            buildSession(result.user)
        } catch (e: Exception) {
            println("loginEmail falhou: ${e::class.simpleName} - ${e.message}")
            AuthResult.Error(AuthError.InvalidCredentials)
        }
    }

    /**
     * Tenta fazer login usando uma conta Google.
     *
     * @param idToken O código que o Google nos dá depois do utilizador escolher a sua conta.
     * @return Se correr bem, a sessão do utilizador já com o seu cargo.
     *         Se algo correr mal, um erro com mais detalhe do que aconteceu.
     */
    override suspend fun loginGoogle(idToken: String): AuthResult<Session> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            buildSession(result.user)
        } catch (e: Exception) {
            AuthResult.Error(AuthError.Unknown(e.message))
        }
    }

    /**
     * Verifica se já existe alguém com sessão iniciada neste dispositivo.
     *
     * @return A sessão da pessoa, se ela já tiver feito login antes.
     *         Nulo (dentro de um sucesso) se ninguém tiver sessão iniciada.
     *         Um erro se não conseguirmos confirmar o cargo dessa pessoa.
     */
    override suspend fun getSession(): AuthResult<Session?> {
        val user = auth.currentUser ?: return AuthResult.Success(null)
        return when (val result = buildSession(user)) {
            is AuthResult.Success -> AuthResult.Success(result.data)
            is AuthResult.Error -> AuthResult.Error(result.error)
        }
    }

    /** Termina a sessão do utilizador atual. */
    override fun logout() = auth.signOut()

    /**
     * Depois de o Firebase confirmar quem é a pessoa, vai buscar o cargo dela
     * ao nosso backend e junta tudo numa única [Session], pronta a ser usada
     * pelo resto da app.
     *
     * @param user O utilizador já confirmado pelo Firebase. Pode ser nulo se o login falhou.
     * @return A sessão completa, com o cargo incluído, se tudo correr bem.
     *         Um erro se o utilizador não tiver email, se o backend não o conhecer,
     *         ou se o cargo devolvido pelo backend não for um cargo válido.
     */
    private suspend fun buildSession(user: FirebaseUser?): AuthResult<Session> {
        val email = user?.email
            ?: return AuthResult.Error(AuthError.Unknown("Utilizador Firebase sem email"))

        return when (val result = funcionarioDataSource.getFuncionario(email)) {
            is AuthResult.Success -> {
                val role = result.data.toUserRole()
                    ?: return AuthResult.Error(AuthError.Unknown("tipo desconhecido: ${result.data.cargo}"))
                AuthResult.Success(
                    data = Session(
                        uid = user.uid,
                        email = email,
                        nome = result.data.nome,
                        turno = result.data.turno,
                        role = role,
                        idFunc = result.data.idFunc
                    )
                )
            }

            is AuthResult.Error -> AuthResult.Error(result.error)
        }
    }
}