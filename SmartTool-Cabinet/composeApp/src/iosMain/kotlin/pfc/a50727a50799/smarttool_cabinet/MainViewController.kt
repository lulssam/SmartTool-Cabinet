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
import pfc.a50727a50799.smarttool_cabinet.di.AppModule

fun MainViewController() = ComposeUIViewController {
    App(
        authRepository = AppModule.authRepository,
        googleSignIn = { throw NotImplementedError("Google Sign-In indisponível no iOS") }
    )
}