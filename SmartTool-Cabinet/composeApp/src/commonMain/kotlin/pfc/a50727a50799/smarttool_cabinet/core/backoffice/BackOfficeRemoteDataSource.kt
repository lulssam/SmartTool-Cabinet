package pfc.a50727a50799.smarttool_cabinet.core.backoffice

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
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiError
import pfc.a50727a50799.smarttool_cabinet.core.network.ApiResult
/**
 * Responsável por comunicar com a API para executar operações
 * relacionadas com a gestão de funcionários e armários.
 *
 * Esta classe permite obter informação, criar funcionários
 * e atualizar os seus dados através dos serviços disponibilizados
 * pelo servidor.
 *
 * @property httpClient Cliente utilizado para comunicar com a API.
 */
class BackOfficeRemoteDataSource(private val httpClient: HttpClient) {
    /**
     * Obtém a lista de funcionários.
     *
     * @return Um resultado que contém a lista de funcionários quando o
     * pedido é bem-sucedido ou um erro caso não seja possível concluí-lo.
     */
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
    /**
     * Altera o cargo de um funcionário.
     *
     * @param id Identificador do funcionário.
     * @param cargo Novo cargo a atribuir.
     * @return Um resultado que indica se a operação foi concluída com sucesso.
     */
    suspend fun mudarCargo(id: Int, cargo: String): ApiResult<Unit> = try {
        val response = httpClient.patch("/api/funcionarios/$id/cargo") {
            contentType(ContentType.Application.Json)
            setBody(NovoCargoDTO(cargo))
        }
        if (response.status == HttpStatusCode.OK) ApiResult.Success(Unit)
        else ApiResult.Error(ApiError.Unknown("Erro ao mudar cargo"))
    } catch (e: Exception) {
        ApiResult.Error(ApiError.NetworkError)
    }
    /**
     * Altera o turno de um funcionário.
     *
     * @param id Identificador do funcionário.
     * @param turno Novo turno a atribuir.
     * @return Um resultado que indica se a operação foi concluída com sucesso.
     */
    suspend fun mudarTurno(id: Int, turno: String): ApiResult<Unit> = try {
        val response = httpClient.patch("/api/funcionarios/$id/turno") {
            contentType(ContentType.Application.Json)
            setBody(NovoTurnoDTO(turno))
        }
        if (response.status == HttpStatusCode.OK) ApiResult.Success(Unit)
        else ApiResult.Error(ApiError.Unknown("Erro ao mudar turno"))
    } catch (e: Exception) {
        ApiResult.Error(ApiError.NetworkError)
    }
    /**
     * Desativa um funcionário.
     *
     * O funcionário deixa de estar disponível para utilização,
     * mas mantém o seu histórico na aplicação.
     *
     * @param id Identificador do funcionário.
     * @return Um resultado que indica se a operação foi concluída com sucesso.
     */
    suspend fun desativarFuncionario(id: Int): ApiResult<Unit> = try {
        val response = httpClient.patch("/api/funcionarios/$id/desativar")
        if (response.status == HttpStatusCode.OK) ApiResult.Success(Unit)
        else ApiResult.Error(ApiError.Unknown("Erro ao desativar"))
    } catch (e: Exception) {
        ApiResult.Error(ApiError.NetworkError)
    }
    /**
     * Obtém a lista de armários.
     *
     * @return Um resultado que contém a lista de armários ou um erro
     * caso o pedido não possa ser concluído.
     */
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
    /**
     * Cria um novo funcionário.
     *
     * @param nome Nome completo do funcionário.
     * @param email Endereço de correio eletrónico do funcionário.
     * @param cargo Cargo a atribuir ao funcionário.
     * @param turno Turno de trabalho do funcionário.
     * @return Um resultado que indica se o funcionário foi criado com sucesso.
     */
    suspend fun criarFuncionario(
        nome: String,
        email: String,
        cargo: String,
        turno: String
    ): ApiResult<Unit> = try {
        val response = httpClient.post("/api/funcionarios") {
            contentType(ContentType.Application.Json)
            setBody(
                CriarFuncionarioRequest(
                    nomeCompleto = nome,
                    email = email,
                    cargo = cargo,
                    turno = turno
                )
            )
        }
        if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
            ApiResult.Success(Unit)
        } else {
            ApiResult.Error(ApiError.Unknown("Erro ao criar utilizador no servidor"))
        }
    } catch (e: Exception) {
        ApiResult.Error(ApiError.NetworkError)
    }
}