package pfc.a50727a50799.smarttool_cabinet

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthRepository
import pfc.a50727a50799.smarttool_cabinet.feature.login.LoginScreen
import pfc.a50727a50799.smarttool_cabinet.feature.login.LoginViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.BackOfficeRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.GestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.LoginRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.TecnicoRoute
import androidx.navigation.compose.*
import pfc.a50727a50799.smarttool_cabinet.core.auth.UserRole

@Composable

fun App(
    authRepository: AuthRepository,
    googleSignIn: suspend () -> String
) {
    MaterialTheme {

        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = LoginRoute) {
            composable<LoginRoute> {
                val viewModel = viewModel { LoginViewModel(authRepository) }
                val scope = rememberCoroutineScope() // lançar corrotina do click

                val state by viewModel.state.collectAsState()

                LaunchedEffect(state.sessao) {
                    val sessao = state.sessao
                    if (sessao != null) {
                        val destino = when (sessao.role) {
                            UserRole.GESTOR -> GestorRoute
                            UserRole.BACKOFFICE -> BackOfficeRoute
                            UserRole.TECNICO -> TecnicoRoute
                        }
                        navController.navigate(destino) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    }
                }

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

            composable<GestorRoute> {
                // TODO
            }

            composable<BackOfficeRoute> {
                // TODO
            }

            composable<TecnicoRoute> {
                // TODO
            }
        }

    }
}