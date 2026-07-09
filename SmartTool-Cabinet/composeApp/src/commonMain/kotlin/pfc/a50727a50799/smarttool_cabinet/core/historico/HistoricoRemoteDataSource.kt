package pfc.a50727a50799.smarttool_cabinet.core.historico

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult

/**
 * Responsável por obter o histórico de requisições através da API.
 *
 * Esta classe comunica com o servidor para obter o histórico
 * geral de requisições ou o histórico associado a um técnico.
 *
 * @property httpClient Cliente utilizado para comunicar com a API.
 */
class HistoricoRemoteDataSource(
    private val httpClient: HttpClient
) {
    /**
     * Obtém o histórico geral de requisições.
     *
     * @return Um resultado que contém a lista de registos do histórico
     * ou um erro caso não seja possível concluir o pedido.
     */
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
    /**
     * Obtém o histórico de requisições de um técnico.
     *
     * @param idTecnico Identificador do técnico.
     * @return Um resultado que contém a lista de requisições do técnico
     * ou um erro caso ocorra algum problema.
     */
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