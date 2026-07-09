package pfc.a50727a50799.smarttool_cabinet.core.network

/**
 * Representa o resultado de uma operação efetuada sobre a API.
 *
 * Uma operação pode terminar com sucesso, devolvendo os dados pedidos,
 * ou terminar com um erro.
 *
 * @param T Tipo de dados devolvido quando a operação é bem-sucedida.
 */

sealed class ApiResult<out T> {
    /**
     * Representa uma operação concluída com sucesso.
     *
     * @property data Informação devolvida pela operação.
     */
    data class Success<T>(val data: T) : ApiResult<T>()
    /**
     * Representa uma operação que terminou com um erro.
     *
     * @property error Informação sobre o erro ocorrido.
     */
    data class Error(val error: ApiError) : ApiResult<Nothing>()
}
/**
 * Representa os diferentes tipos de erro que podem ocorrer
 * durante a comunicação com a API.
 */

sealed interface ApiError {
    /**
     * Indica que não foi possível comunicar com o servidor,
     * normalmente devido a um problema de ligação à rede.
     */
    data object NetworkError : ApiError
    /**
     * Representa um erro que não pertence a uma categoria específica.
     *
     * @property message Descrição do erro, quando disponível.
     */
    data class Unknown(val message: String?) : ApiError
}