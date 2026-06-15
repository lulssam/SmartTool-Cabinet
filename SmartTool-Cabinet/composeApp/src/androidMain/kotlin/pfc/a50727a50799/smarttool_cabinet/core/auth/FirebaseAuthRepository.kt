package pfc.a50727a50799.smarttool_cabinet.core.auth

import pfc.a50727a50799.smarttool_cabinet.core.auth.data.FuncionarioRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.toUserRole

/**
 *  Implementação real da autenticação, usando o firebase para confirmar quem é o user,
 *  e o nosso backend o cargo dele
 *  @property funcionarioDataSource usado para perguntar ao backend o papel do user
 *  */
class FirebaseAuthRepository(
    private val funcionarioDataSource: FuncionarioRemoteDataSource
) : AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    override suspend fun loginEmail(email: String, password: String): AuthResult<Session> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            buildSession(result.user)
        } catch (e: Exception) {
            AuthResult.Error(AuthError.InvalidCredentials)
        }
    }

    override suspend fun loginGoogle(idToken: String): AuthResult<Session> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            buildSession(result.user)
        } catch (e: Exception) {
            AuthResult.Error(AuthError.Unknown(e.message))
        }
    }

    override suspend fun getSession(): AuthResult<Session?> {
        val user = auth.currentUser ?: return AuthResult.Success(null)
        return when (val result = buildSession(user)) {
            is AuthResult.Success -> AuthResult.Success(result.data)
            is AuthResult.Error -> AuthResult.Error(result.error)
        }
    }

    override fun logout() = auth.signOut()

    /**
     * A partir de um user já autenticado no firebase, vai procurar o cargo dele no nosso backend
     * e junta tudo numa [Session]*/
    private suspend fun buildSession(user: FirebaseUser?): AuthResult<Session> {
        val email = user?.email
            ?: return AuthResult.Error(AuthError.Unknown("Utilizador Firebase sem email"))

        return when (val result = funcionarioDataSource.getFuncionario(email)) {
            is AuthResult.Success -> {
                val role = result.data.toUserRole()
                    ?: return AuthResult.Error(AuthError.Unknown("tipo desconhecido: ${result.data.cargo}"))
                AuthResult.Success(Session(uid = user.uid, email = email, role = role))
            }
            is AuthResult.Error -> AuthResult.Error(result.error)
        }
    }
}