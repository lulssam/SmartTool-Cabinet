package pfc.a50727a50799.smarttool_cabinet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthRepository
import pfc.a50727a50799.smarttool_cabinet.feature.login.LoginScreen
import pfc.a50727a50799.smarttool_cabinet.feature.login.LoginViewModel

import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.compose_multiplatform

@Composable

fun App(
    authRepository: AuthRepository,
    googleSignIn: suspend () -> String
) {
    MaterialTheme {
        val viewModel = viewModel { LoginViewModel(authRepository) }
        val scope = rememberCoroutineScope() // lançar corrotina do click

        LoginScreen(
            viewModel = viewModel,
            onGoogleClick = {
                scope.launch {
                    val idToken = googleSignIn()
                    viewModel.onGoogleToken(idToken)
                }
            }
        )
    }
}