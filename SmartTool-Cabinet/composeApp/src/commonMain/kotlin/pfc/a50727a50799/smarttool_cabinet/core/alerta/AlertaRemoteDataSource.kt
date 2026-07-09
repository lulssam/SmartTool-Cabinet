package pfc.a50727a50799.smarttool_cabinet.core.alerta

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
/**
 * Responsável por obter os alertas disponíveis através da API.
 *
 * Esta classe comunica com o servidor e devolve a lista de alertas
 * ou um erro caso não seja possível completar o pedido.
 *
 * @property httpClient Cliente utilizado para comunicar com a API.
 */
class AlertaRemoteDataSource(
    private val httpClient: HttpClient
) {
    /**
     * Obtém todos os alertas disponíveis.
     *
     * @return Um resultado que contém a lista de alertas quando o pedido
     * é bem-sucedido ou um erro caso ocorra algum problema.
     */
    suspend fun getAlertas(): ApiResult<List<AlertaDto>> = try {
        val response = httpClient.get("/api/alertas")
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