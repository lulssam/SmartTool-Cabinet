package pfc.a50727a50799.smarttool_cabinet.core.network

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val error: ApiError) : ApiResult<Nothing>()
}

sealed interface ApiError {
    data object NetworkError : ApiError
    data class Unknown(val message: String?) : ApiError
}