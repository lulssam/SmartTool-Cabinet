package pfc.a50727a50799.smarttool_cabinet.core.tarefa

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.io.IOException
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
/**
 * Responsável por comunicar com a API para executar operações
 * relacionadas com tarefas.
 *
 * Esta classe permite obter tarefas, criar novas tarefas
 * e concluir tarefas existentes.
 *
 * @property httpClient Cliente utilizado para comunicar com a API.
 */
class TarefaRemoteDataSource(
    private val httpClient: HttpClient
) {
    /**
     * Obtém todas as tarefas.
     *
     * @return Um resultado que contém a lista de tarefas ou um erro
     * caso não seja possível concluir o pedido.
     */
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
    /**
     * Cria uma nova tarefa.
     *
     * @param dto Informação necessária para criar a tarefa.
     * @return Um resultado que indica se a tarefa foi criada com sucesso.
     */
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
    /**
     * Obtém as tarefas atribuídas a um técnico.
     *
     * @param id Identificador do técnico.
     * @return Um resultado que contém a lista de tarefas do técnico.
     */
    suspend fun getTarefasTecnico(id: Int): ApiResult<List<TarefaDto>> = try {
        val response = httpClient.get("/api/tarefas/tecnico/$id")
        when (response.status) {
            HttpStatusCode.OK -> ApiResult.Success(response.body())
            else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
        }
    } catch (e: IOException) {
        ApiResult.Error(ApiError.NetworkError)
    } catch (e: Exception) {
        ApiResult.Error(ApiError.Unknown(e.message))
    }
    /**
     * Marca uma tarefa como concluída.
     *
     * @param idTarefa Identificador da tarefa.
     * @return Um resultado que indica se a tarefa foi concluída com sucesso
     * ou um erro caso a operação não possa ser realizada.
     */
    suspend fun concluirTarefa(idTarefa: Int): ApiResult<Unit> = try {
        val response = httpClient.patch("/api/tarefas/$idTarefa/concluir")
        when (response.status) {
            HttpStatusCode.OK -> ApiResult.Success(Unit)
            HttpStatusCode.Conflict -> ApiResult.Error(ApiError.Unknown(response.bodyAsText()))
            else -> ApiResult.Error(ApiError.Unknown(response.status.toString()))
        }
    } catch (e: IOException) {
        ApiResult.Error(ApiError.NetworkError)
    } catch (e: Exception) {
        ApiResult.Error(ApiError.Unknown(e.message))
    }
}

