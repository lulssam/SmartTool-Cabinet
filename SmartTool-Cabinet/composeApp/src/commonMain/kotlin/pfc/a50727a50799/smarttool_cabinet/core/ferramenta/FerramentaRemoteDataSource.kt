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

/**
 * Representa um pedido para alterar o estado de uma ferramenta.
 *
 * @property estado Novo estado a atribuir à ferramenta.
 */
@Serializable
data class EstadoFerramentaDTO(val estado: String)

/**
 * Responsável por comunicar com a API para executar operações
 * relacionadas com ferramentas.
 *
 * Esta classe permite obter informação sobre ferramentas,
 * criar requisições, devolver ferramentas e atualizar o seu estado.
 *
 * @property httpClient Cliente utilizado para comunicar com a API.
 */
class FerramentaRemoteDataSource(
    private val httpClient: HttpClient
) {
    /**
     * Obtém a lista de todas as ferramentas.
     *
     * @return Um resultado que contém a lista de ferramentas ou um erro
     * caso não seja possível concluir o pedido.
     */
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

    /**
     * Obtém a lista de ferramentas que ainda não foram devolvidas.
     *
     * @return Um resultado que contém a lista de ferramentas em falta
     * ou um erro caso ocorra algum problema.
     */

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

    /**
     * Obtém as ferramentas atualmente requisitadas por um técnico.
     *
     * @param id Identificador do técnico.
     * @return Um resultado que contém a lista de ferramentas requisitadas.
     */

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

    /**
     * Regista a devolução de uma ferramenta.
     *
     * @param idRequisicao Identificador da requisição a devolver.
     * @return Um resultado que indica se a operação foi concluída com sucesso.
     */

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
    /**
     * Altera o estado de uma ferramenta.
     *
     * @param idFerramenta Identificador da ferramenta.
     * @param novoEstado Novo estado a atribuir.
     * @return Um resultado que indica se a operação foi concluída com sucesso.
     */
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
    /**
     * Cria uma nova requisição de ferramenta.
     *
     * @param idTecnico Identificador do técnico que faz a requisição.
     * @param codigoTipo Código do tipo da ferramenta.
     * @param nFerramenta Número da ferramenta.
     * @return Um resultado que indica se a requisição foi criada com sucesso.
     */
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
    /**
     * Obtém as ferramentas reservadas para um técnico.
     *
     * @param id Identificador do técnico.
     * @return Um resultado que contém a lista de ferramentas reservadas.
     */
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