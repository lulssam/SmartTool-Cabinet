package pfc.a50727a50799.smarttool_cabinet.core.alerta

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class AlertaRemoteDataSource(
    private val httpClient: HttpClient
) {
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