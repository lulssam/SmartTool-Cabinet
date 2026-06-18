package pfc.a50727a50799.smarttool_cabinet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import pfc.a50727a50799.smarttool_cabinet.core.auth.FirebaseAuthRepository
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.FuncionarioRemoteDataSource
import pfc.a50727a50799.smarttool_cabinet.core.auth.obterGoogleIdToken
import pfc.a50727a50799.smarttool_cabinet.di.AppModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(
                authRepository = AppModule.authRepository,
                googleSignIn = { obterGoogleIdToken(this@MainActivity) }
            )
        }
    }
}