package pfc.a50727a50799.smarttool_cabinet.core.tecnico

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class TecnicoRemoteDataSource(private val httpClient: HttpClient) {
    suspend fun getTecnicos(): ApiResult<List<TecnicoDto>> = try {
        val response = httpClient.get("/api/tecnicos")
        when (response.status) {
            HttpStatusCode.OK -> ApiResult.Success(response.body())
            else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
        }
    } catch (e: IOException) { ApiResult.Error(ApiError.NetworkError) }
    catch (e: Exception) { ApiResult.Error(ApiError.Unknown(e.message)) }
}