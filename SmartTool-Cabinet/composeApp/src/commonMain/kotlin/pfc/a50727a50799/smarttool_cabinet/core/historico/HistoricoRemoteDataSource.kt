package pfc.a50727a50799.smarttool_cabinet.core.historico

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class HistoricoRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getHistorico(): ApiResult<List<HistoricoDto>> = try {
        val response = httpClient.get("/api/historico")
        when (response.status) {
            HttpStatusCode.OK -> ApiResult.Success(response.body())
            else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
        }
    } catch (e: IOException) {
        ApiResult.Error(ApiError.NetworkError)
    } catch (e: Exception) {
        ApiResult.Error(ApiError.Unknown(e.message))
    }
    suspend fun getHistoricoTecnico(idTecnico: Int): ApiResult<List<HistoricoDto>> = try {
        val response = httpClient.get("/api/historico/tecnico/$idTecnico")
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