package pfc.a50727a50799.smarttool_cabinet.core.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario.FuncionarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario.toUserRole

/**
 * Implementação real da autenticação no iOS, usando o Firebase (através do GitLive SDK)
 * para confirmar quem é o utilizador, e o nosso backend para saber o cargo dele.
 * É o equivalente iOS do [FirebaseAuthRepository] do Android.
 *
 * @property funcionarioDataSource usado para perguntar ao backend o papel do utilizador.
 */
class FirebaseAuthRepositoryIos(
    private val funcionarioDataSource: FuncionarioRemoteDataSource
) : AuthRepository {

    /** Ponto de entrada do Firebase Auth. Usa a app Firebase configurada no arranque (Swift). */
    private val auth = Firebase.auth

    /**
     * Usado só para o logout: no GitLive, terminar sessão é uma operação "suspensa"
     * (pode demorar), mas a nossa interface pede um logout simples. Este âmbito
     * deixa-nos disparar essa operação sem prender quem chamou.
     */
    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * O accessToken da Google, guardado a partir do ecrã de login (lado Swift).
     * No iOS o Firebase precisa do idToken E do accessToken para o login com Google;
     * no Android bastava o idToken.
     */
    var googleAccessToken: String? = null

    /**
     * Tenta iniciar sessão com email e password no Firebase e, se resultar,
     * vai ao backend buscar o cargo para montar a [Session].
     *
     * @param email O email que o utilizador escreveu.
     * @param password A password que o utilizador escreveu.
     * @return [AuthResult.Success] com a [Session] se tudo correu bem,
     * ou [AuthResult.Error] a explicar o que falhou.
     */
    override suspend fun loginEmail(email: String, password: String): AuthResult<Session> {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).user
            buildSession(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthError.InvalidCredentials)
        }
    }

    /**
     * Inicia sessão com uma conta Google, a partir de um token que o iOS já obteve.
     *
     * @param idToken O token de identidade da Google, depois de o utilizador escolher a conta.
     * @return [AuthResult.Success] com a [Session] se resultou, ou [AuthResult.Error] caso contrário.
     */
    override suspend fun loginGoogle(idToken: String): AuthResult<Session> {
        return try {
            val credential = GoogleAuthProvider.credential(idToken, googleAccessToken)
            val user = auth.signInWithCredential(credential).user
            buildSession(user)
        } catch (e: Exception) {
            AuthResult.Error(AuthError.Unknown(e.message))
        }
    }

    /**
     * Verifica se já há alguém com sessão iniciada de uma vez anterior e, se sim,
     * confirma o cargo dessa pessoa no backend.
     *
     * @return [AuthResult.Success] com `null` se não há sessão, com a [Session] se há,
     * ou [AuthResult.Error] se há sessão mas não foi possível confirmar o cargo.
     */
    override suspend fun getSession(): AuthResult<Session?> {
        val user = auth.currentUser ?: return AuthResult.Success(null)
        return when (val result = buildSession(user)) {
            is AuthResult.Success -> AuthResult.Success(result.data)
            is AuthResult.Error -> AuthResult.Error(result.error)
        }
    }

    /** Termina a sessão atual no Firebase. */
    override fun logout() {
        scope.launch { auth.signOut() }
    }

    /**
     * A partir de um utilizador já autenticado no Firebase, vai buscar o cargo dele
     * ao nosso backend e junta tudo numa [Session].
     *
     * @param user O utilizador que o Firebase confirmou (ou null se algo correu mal).
     * @return [AuthResult.Success] com a [Session] montada, ou [AuthResult.Error] com o motivo.
     */
    private suspend fun buildSession(user: FirebaseUser?): AuthResult<Session> {
        val email = user?.email
            ?: return AuthResult.Error(AuthError.Unknown("Utilizador Firebase sem email"))

        return when (val result = funcionarioDataSource.getFuncionario(email)) {
            is AuthResult.Success -> {
                val role = result.data.toUserRole()
                    ?: return AuthResult.Error(AuthError.Unknown("tipo desconhecido: ${result.data.cargo}"))
                AuthResult.Success(
                    Session(
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