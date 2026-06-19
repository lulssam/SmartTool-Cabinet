package pfc.a50727a50799.smarttool_cabinet.core.auth.data.funcionario

import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthResult

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthError

/**
 * Vai ao nosso backend ver qual é que é o cargo de um funcionario dado o seu mail
 *
 * @property httpClient O cliente HTTP (ktor) já configurado com o endereço do backend
 * @return os dados do funcionário, ou um erro a explicar o que correu mal
 * */
class FuncionarioRemoteDataSource(
    private val httpClient: HttpClient
) {

    /**
     * @param email do funcionario a procurar
     * @return os dados do funcionário ou um erro a explicar o que correu mal*/
    suspend fun getFuncionario(email: String): AuthResult<FuncionarioDto> {
        return try {
            val response = httpClient.get("/api/funcionarios/$email")
            when (response.status) {
                HttpStatusCode.OK -> AuthResult.Success(response.body())
                HttpStatusCode.NotFound -> AuthResult.Error(AuthError.RoleNotFound)
                else -> AuthResult.Error(AuthError.Unknown(response.status.toString()))
            }
        } catch (e: IOException) {
            AuthResult.Error(AuthError.NetworkError)
        } catch (e: Exception) {
            AuthResult.Error(AuthError.Unknown(e.message))
        }
    }
}