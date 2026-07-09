package pfc.a50727a50799.smarttool_cabinet.core.armario

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
/**
 * Responsável por obter a informação dos armários através da API.
 *
 * Esta classe comunica com o servidor para obter a lista de armários
 * disponíveis na aplicação.
 *
 * @property httpClient Cliente utilizado para comunicar com a API.
 */
class ArmarioRemoteDataSource(private val httpClient: HttpClient) {
    /**
     * Obtém todos os armários disponíveis.
     *
     * @return Um resultado que contém a lista de armários quando o pedido
     * é bem-sucedido ou um erro caso não seja possível concluir a operação.
     */
    suspend fun getArmarios(): ApiResult<List<ArmarioDto>> = try {
        val response = httpClient.get("/api/armarios")
        when (response.status) {
            HttpStatusCode.OK -> ApiResult.Success(response.body())
            else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
        }
    } catch (e: IOException) {
        ApiResult.Error(ApiError.NetworkError)
    } catch (e: Exception) {
        ApiResult.Error(ApiError.Unknown(e.message))
    }
}