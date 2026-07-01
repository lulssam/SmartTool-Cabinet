package pfc.a50727a50799.smarttool_cabinet.core.ferramenta

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

@Serializable
data class EstadoFerramentaDTO(val estado: String)

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

    suspend fun getEmFalta(): ApiResult<List<FerramentaEmFaltaDto>> = try {
        val response = httpClient.get("/api/ferramentas/em-falta")
        when (response.status) {
            HttpStatusCode.OK -> ApiResult.Success(response.body())
            else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
        }
    } catch (e: IOException) {
        ApiResult.Error(ApiError.NetworkError)
    } catch (e: Exception) {
        ApiResult.Error(ApiError.Unknown(e.message))
    }

    suspend fun getFerramentaTecnico(id: Int): ApiResult<List<FerramentaDto>> {
        return try {
            val response = httpClient.get("/api/tecnicos/${id}/ferramentas")
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

    suspend fun devolverFerramenta(idRequisicao: Int): ApiResult<Unit> {
        return try {
            val response = httpClient.patch("/api/requisicoes/$idRequisicao/devolver")
            when (response.status) {
                HttpStatusCode.OK -> ApiResult.Success(Unit)
                else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
            }
        } catch (e: IOException) {
            ApiResult.Error(ApiError.NetworkError)
        } catch (e: Exception) {
            ApiResult.Error(ApiError.Unknown(e.message))
        }
    }

    suspend fun mudarEstadoFerramenta(idFerramenta: Int, novoEstado: String): ApiResult<Unit> {
        return try {
            val response = httpClient.patch("/api/ferramentas/$idFerramenta/estado") {
                contentType(ContentType.Application.Json)
                setBody(EstadoFerramentaDTO(novoEstado))
            }
            when (response.status) {
                HttpStatusCode.OK -> ApiResult.Success(Unit)
                else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
            }
        } catch (e: IOException) {
            ApiResult.Error(ApiError.NetworkError)
        } catch (e: Exception) {
            ApiResult.Error(ApiError.Unknown(e.message))
        }
    }

    suspend fun requisitarFerramenta(
        idTecnico: Int,
        codigoTipo: Int,
        nFerramenta: Int
    ): ApiResult<Unit> {
        return try {

            val response = httpClient.post("/api/requisicoes") {
                contentType(ContentType.Application.Json)
                setBody(NovaRequisicaoDTO.NovaRequisicaoDTO(idTecnico, codigoTipo, nFerramenta))
            }
            when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.Created -> ApiResult.Success(Unit)
                else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
            }
        } catch (e: IOException) {
            ApiResult.Error(ApiError.NetworkError)
        } catch (e: Exception) {
            ApiResult.Error(ApiError.Unknown(e.message))
        }
    }

    suspend fun getReservadasTecnico(id: Int): ApiResult<List<FerramentaDto>> {
        return try {
            val response = httpClient.get("/api/tecnicos/$id/reservadas")
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