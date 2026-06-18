package pfc.a50727a50799.smarttool_cabinet

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.FuncionarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.auth.iOSAuthRepository

fun MainViewController() = ComposeUIViewController {
    val httpClient = remember {
        HttpClient(Darwin) {
            install(ContentNegotiation) { json() }
            defaultRequest { url("http://127.0.0.1:8080") }
        }
    }

    val repo = remember {
        iOSAuthRepository(FuncionarioRemoteDataSource(httpClient))
    }

    App(
        authRepository = repo,
        googleSignIn = { throw NotImplementedError("Google Sign-In indisponível no iOS") }
    )
}