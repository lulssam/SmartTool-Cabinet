package pfc.a50727a50799.smarttool_cabinet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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