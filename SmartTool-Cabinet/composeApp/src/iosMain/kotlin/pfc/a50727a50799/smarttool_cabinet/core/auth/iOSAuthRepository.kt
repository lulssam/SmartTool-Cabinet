package pfc.a50727a50799.smarttool_cabinet.core.auth

import pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario.FuncionarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario.toUserRole

class iOSAuthRepository(
    private val funcionarioDataSource: FuncionarioRemoteDataSource
) : AuthRepository {

    /**
     * Faz login apenas com o email, sem passar pelo Firebase — é um stub para o iOS.
     * Vai ao backend buscar o cargo de quem tem este email e, se existir, dá-lhe sessão.
     * A password é ignorada de propósito: isto NÃO é seguro, serve só para
     * conseguir testar a aplicação no iOS enquanto não há Firebase nativo.
     *
     * @param email O email que o utilizador escreveu.
     * @param password Ignorada neste stub (existe só para cumprir a interface).
     * @return [AuthResult.Success] com a [Session] se o email existir no backend,
     * ou [AuthResult.Error] a explicar o que correu mal.
     */
    override suspend fun loginEmail(
        email: String,
        password: String
    ): AuthResult<Session> {
        return when (val resultado = funcionarioDataSource.getFuncionario(email)) {
            is AuthResult.Success -> {
                val role = resultado.data.toUserRole()
                    ?: return AuthResult.Error(
                        AuthError.Unknown("cargo desconhecido: ${resultado.data.cargo}")
                    )
                Session(
                    uid = email,
                    email = email,
                    nome = resultado.data.nome,
                    turno = resultado.data.turno,
                    role = role,
                    idFunc = resultado.data.idFunc
                ).let { AuthResult.Success(it) }
            }

            is AuthResult.Error -> resultado
        }
    }

    /**
     * Ainda não temos fluxo Google no iOS*/
    override suspend fun loginGoogle(idToken: String): AuthResult<Session> {
        return AuthResult.Error(AuthError.Unknown("Não implementado no iOS"))
    }

    /**
     * Ainda não temos sessão persistida no stub
     * */
    override suspend fun getSession(): AuthResult<Session?> {
        return AuthResult.Success(null)
    }

    override fun logout() {}

}