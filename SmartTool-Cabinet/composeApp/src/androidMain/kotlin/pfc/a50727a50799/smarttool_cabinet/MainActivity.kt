package pfc.a50727a50799.smarttool_cabinet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pfc.a50727a50799.smarttool_cabinet.core.auth.obterGoogleIdToken
import pfc.a50727a50799.smarttool_cabinet.di.AppModule

/**
 * O ponto de entrada da app no Android — é o primeiro ecrã que abre quando
 * a pessoa toca no ícone da aplicação.
 * A única coisa que faz é montar a parte visual da app (o [App]) e entregar-lhe
 * as ferramentas de que precisa: quem trata do login e como pedir uma conta Google.
 */
class MainActivity : ComponentActivity() {

    /**
     * Chamado automaticamente pelo Android quando este ecrã é criado.
     * É aqui que dizemos ao Android o que mostrar e ligamos a app às suas
     * dependências.
     *
     * @param savedInstanceState Dados que o Android guardou de uma vez anterior
     *        (por exemplo, se o ecrã tiver rodado). Pode ser nulo na primeira vez.
     */
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