package pfc.a50727a50799.smarttool_cabinet

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import pfc.a50727a50799.smarttool_cabinet.core.auth.AuthRepository
import pfc.a50727a50799.smarttool_cabinet.feature.welcome.WelcomeScreen
import pfc.a50727a50799.smarttool_cabinet.feature.welcome.WelcomeViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.BackOfficeRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.GestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.WelcomeRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.TecnicoRoute
import androidx.navigation.compose.*
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.core.auth.UserRole
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.LoginEmailRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.SSORoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.routeForRole
import pfc.a50727a50799.smarttool_cabinet.feature.session.SessionUiState
import pfc.a50727a50799.smarttool_cabinet.feature.session.SessionViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.session.SplashScreen
import pfc.a50727a50799.smarttool_cabinet.feature.sso.SSOScreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme


/**
 * Ponto de entrada visual da aplicação.
 * Ao arrancar, pergunta ao [SessionViewModel] se já existe alguém com sessão iniciada
 * e decide o que mostrar: o ecrã de "a carregar" enquanto não sabe, o login se não houver
 * sessão, ou diretamente o ecrã do cargo da pessoa se já houver.
 *
 * @param authRepository Quem trata da autenticação. É entregue aos ecrãs que precisam dele.
 * @param googleSignIn Função que abre o seletor de contas Google e devolve o token dessa conta.
 *                     Cada plataforma (Android/iOS) fornece a sua.
 */
@Composable
fun App(
    authRepository: AuthRepository,
    googleSignIn: suspend () -> String
) {
    AppTheme {
        val sessionViewModel = viewModel { SessionViewModel(authRepository) }
        val sessionState by sessionViewModel.state.collectAsState()

        when (val state = sessionState) {
            SessionUiState.Loading -> SplashScreen()
            SessionUiState.NoSession ->
                AppNavHost(startDestination = WelcomeRoute, authRepository, googleSignIn)

            is SessionUiState.Authenticated ->
                AppNavHost(
                    startDestination = routeForRole(state.session.role),
                    authRepository,
                    googleSignIn
                )

        }

    }
}

/**
 * Monta o mapa de navegação da app (todos os ecrãs e como se passa de um para o outro).
 * É aqui que o login, depois de correr bem, encaminha o utilizador para o ecrã do seu cargo.
 *
 * @param startDestination O ecrã por onde a navegação começa: o login quando ninguém tem
 *                         sessão, ou o ecrã do cargo quando já existe uma sessão iniciada.
 * @param authRepository Quem trata da autenticação, entregue aos ecrãs que precisam.
 * @param googleSignIn Função que abre o seletor de contas Google e devolve o token dessa conta.
 */
@Composable
private fun AppNavHost(
    startDestination: Any,
    authRepository: AuthRepository,
    googleSignIn: suspend () -> String
) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable<WelcomeRoute> {
            val viewModel = viewModel { WelcomeViewModel(authRepository) }
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
                        popUpTo(WelcomeRoute) { inclusive = true }
                    }
                }
            }

            WelcomeScreen(
                onLoginEmailClick = { navController.navigate(LoginEmailRoute) },
                onSSOClick = { navController.navigate(SSORoute) }
            )

        }

        composable<LoginEmailRoute> {
            // TODO
            // ecrã de email/password
            // chamar loginviewmodel + formulario + redireção pos login
        }

        composable<SSORoute> {

            val viewModel = viewModel { WelcomeViewModel(authRepository) }
            val scope = rememberCoroutineScope()
            val state by viewModel.state.collectAsState()

            // quando o login google funciona, vai para o ecrã do cargo suposto
            LaunchedEffect(state.sessao) {
                val sessao = state.sessao
                if (sessao != null) {
                    navController.navigate(routeForRole(sessao.role)) {
                        popUpTo(WelcomeRoute) { inclusive = true }
                    }
                }
            }

            SSOScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
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