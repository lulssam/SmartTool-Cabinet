package pfc.a50727a50799.smarttool_cabinet.core.ferramenta.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaDto
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class FerramentaRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getFerramentas(): ApiResult<List<FerramentaDto>> {
        return try {
            val response = httpClient.get("/api/ferramentas")
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
}