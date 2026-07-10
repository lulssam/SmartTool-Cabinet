package pfc.a50727a50799.smarttool_cabinet

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.coroutines.suspendCancellableCoroutine
import pfc.a50727a50799.smarttool_cabinet.core.auth.FirebaseAuthRepositoryIos
import pfc.a50727a50799.smarttool_cabinet.di.AppModule
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Ponto de entrada da interface no iOS.
 *
 * @param nativeGoogleSignIn Função dada pelo lado Swift que abre o ecrã de login da Google.
 *  Devolve o resultado por callback: o idToken se correr bem, ou `null` se o utilizador
 *  cancelar ou algo falhar.
 * @return O controlador de ecrã que o iOS mostra, já com a app Compose lá dentro.
 */
fun MainViewController(
    nativeGoogleSignIn: (onResult: (String?, String?) -> Unit) -> Unit
) = ComposeUIViewController {
    App(
        authRepository = AppModule.authRepository,
        googleSignIn = {
            suspendCancellableCoroutine { cont ->
                nativeGoogleSignIn { idToken, accessToken ->
                    if (idToken != null) {
                        // No iOS o login Google precisa dos dois tokens. Guardamos o accessToken
                        // no repositório para o loginGoogle o poder usar a seguir.
                        (AppModule.authRepository as? FirebaseAuthRepositoryIos)?.googleAccessToken = accessToken
                        cont.resume(idToken)
                    } else {
                        cont.resumeWithException(
                            IllegalStateException("Login Google cancelado ou falhou")
                        )
                    }
                }
            }
        }
    )
}