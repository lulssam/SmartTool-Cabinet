package pfc.a50727a50799.smarttool_cabinet

import androidx.compose.ui.uikit.OnFocusBehavior
import androidx.compose.ui.window.ComposeUIViewController
import pfc.a50727a50799.smarttool_cabinet.di.AppModule

fun MainViewController() = ComposeUIViewController {
    App(
        authRepository = AppModule.authRepository,
        googleSignIn = { throw NotImplementedError("Google Sign-In indisponível no iOS") }
    )
}