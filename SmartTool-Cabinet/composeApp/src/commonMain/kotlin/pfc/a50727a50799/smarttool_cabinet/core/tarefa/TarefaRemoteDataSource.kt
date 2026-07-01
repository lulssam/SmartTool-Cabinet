package pfc.a50727a50799.smarttool_cabinet.core.tarefa

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.IOException
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

class TarefaRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun getTarefas(): ApiResult<List<TarefaDto>> = try {
        val response = httpClient.get("/api/tarefas")
        when (response.status) {
            HttpStatusCode.OK -> ApiResult.Success(response.body())
            else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
        }
    } catch (e: IOException) {
        ApiResult.Error(ApiError.NetworkError)
    } catch (e: Exception) {
        ApiResult.Error(ApiError.Unknown(e.message))
    }

    suspend fun criarTarefa(dto: NovaTarefaDto): ApiResult<Unit> = try {
        val post = httpClient.post("/api/tarefas") {
            contentType(ContentType.Application.Json); setBody(dto)
        }
        when (post.status) {
            HttpStatusCode.Created, HttpStatusCode.OK -> ApiResult.Success(Unit)
            else -> {
                println("criarTarefa falhou (${post.status}): ${post.bodyAsText()}")
                ApiResult.Error(ApiError.Unknown(post.status.toString()))
            }
        }
    } catch (e: IOException) {
        ApiResult.Error(ApiError.NetworkError)
    } catch (e: Exception) {
        ApiResult.Error(ApiError.Unknown(e.message))
    }
}

