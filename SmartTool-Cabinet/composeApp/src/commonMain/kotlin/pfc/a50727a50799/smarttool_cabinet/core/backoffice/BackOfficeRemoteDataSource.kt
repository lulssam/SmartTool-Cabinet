package pfc.a50727a50799.smarttool_cabinet.core.backoffice

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.IOException
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class BackOfficeRemoteDataSource(private val httpClient: HttpClient) {

    suspend fun getFuncionarios(): ApiResult<List<FuncionariosDTO>> = try {
        val response = httpClient.get("/api/funcionarios")
        when (response.status) {
            HttpStatusCode.OK -> ApiResult.Success(response.body())
            else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
        }
    } catch (e: IOException) {
        ApiResult.Error(ApiError.NetworkError)
    } catch (e: Exception) {
        ApiResult.Error(ApiError.Unknown(e.message))
    }

    suspend fun mudarCargo(id: Int, cargo: String): ApiResult<Unit> = try {
        val response = httpClient.patch("/api/funcionarios/$id/cargo") {
            contentType(ContentType.Application.Json)
            setBody(NovoCargoDTO(cargo))
        }
        if (response.status == HttpStatusCode.OK) ApiResult.Success(Unit)
        else ApiResult.Error(ApiError.Unknown("Erro ao mudar cargo"))
    } catch (e: Exception) { ApiResult.Error(ApiError.NetworkError) }

    suspend fun mudarTurno(id: Int, turno: String): ApiResult<Unit> = try {
        val response = httpClient.patch("/api/funcionarios/$id/turno") {
            contentType(ContentType.Application.Json)
            setBody(NovoTurnoDTO(turno))
        }
        if (response.status == HttpStatusCode.OK) ApiResult.Success(Unit)
        else ApiResult.Error(ApiError.Unknown("Erro ao mudar turno"))
    } catch (e: Exception) { ApiResult.Error(ApiError.NetworkError) }

    suspend fun desativarFuncionario(id: Int): ApiResult<Unit> = try {
        val response = httpClient.patch("/api/funcionarios/$id/desativar")
        if (response.status == HttpStatusCode.OK) ApiResult.Success(Unit)
        else ApiResult.Error(ApiError.Unknown("Erro ao desativar"))
    } catch (e: Exception) { ApiResult.Error(ApiError.NetworkError) }

    suspend fun getArmarios(): ApiResult<List<BOArmarioDTO>> = try {
        val response = httpClient.get("/api/armarios")
        if (response.status == HttpStatusCode.OK) {
            ApiResult.Success(response.body())
        } else {
            ApiResult.Error(ApiError.Unknown(response.status.toString()))
        }
    } catch (e: Exception) {
        ApiResult.Error(ApiError.NetworkError)
    }
}