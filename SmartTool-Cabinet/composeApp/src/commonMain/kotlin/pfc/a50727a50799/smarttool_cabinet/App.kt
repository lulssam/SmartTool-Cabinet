package pfc.a50727a50799.smarttool_cabinet

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
import pfc.a50727a50799.smarttool_cabinet.core.auth.data.SessionManager
import pfc.a50727a50799.smarttool_cabinet.di.AppModule
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.BODashboardScreen
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.BODashboardViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.email.emailScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.alertas.AlertasScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.alertas.AlertasViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.armarios.ArmariosScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.armarios.ArmariosViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.GestorScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.GestorViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.FerramentasGestorScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.FerramentasGestorViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.menu.GestorScaffold
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.AlertasGestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.ArmariosGestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.FerramentasGestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.FerramentasTecnicoRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.HistoricoTecnicoRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.LoginEmailRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.SSORoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.routeForRole
import pfc.a50727a50799.smarttool_cabinet.feature.session.SessionUiState
import pfc.a50727a50799.smarttool_cabinet.feature.session.SessionViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.session.SplashScreen
import pfc.a50727a50799.smarttool_cabinet.feature.sso.SSOScreen
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.FerramentasScreen
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.FerramentasViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.HistoricoScreen
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.HistoricoViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.TecnicoScreen
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.TecnicoViewModel
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
            val viewModel = viewModel { WelcomeViewModel(authRepository) }
            emailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onAuthenticated = { role ->
                    navController.navigate(routeForRole(role)) {
                        popUpTo(WelcomeRoute) { inclusive = true }
                    }
                }
            )
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
            val viewModel = viewModel {
                GestorViewModel(
                    ferramentas = AppModule.ferramentaRemoteDataSource,
                    armarios = AppModule.armarioRemoteDataSource,
                    alertas = AppModule.alertaRemoteDataSource,
                    nomeGestor = SessionManager.atual?.nome ?: "",
                    turno = SessionManager.atual?.turno ?: ""

                )
            }

            val scope by viewModel.state.collectAsState()

            GestorScaffold(
                itemSelecionado = "dashboard",
                alertasAtivos = scope.alertas.count(),
                nomeGestor = SessionManager.atual?.nome ?: "",
                cargo = "Gestor de Armazém",
                onNavegar = { id -> navegarGestor(navController, id) },
                onLogout = {/*todo: terminar sessão*/ },
            ) { abrirMenu ->
                GestorScreen(
                    viewModel = viewModel,
                    onVerArmarios = { navController.navigate(ArmariosGestorRoute) },
                    onVerAlertas = { navController.navigate(AlertasGestorRoute) },
                    onMenuClick = abrirMenu
                )
            }

        }

        composable<BackOfficeRoute> {
            val viewModel = viewModel {
                BODashboardViewModel()
            }
            BODashboardScreen(viewModel = viewModel)
        }


        composable<TecnicoRoute> {
            val session = SessionManager.atual
            val viewModel = viewModel {
                TecnicoViewModel(
                    ferramentas = AppModule.ferramentaRemoteDataSource,
                    idTecnico = session?.idFunc ?: -1,
                    nomeTecnico = session?.nome ?: "Técnico",
                    turno = session?.turno ?: "Manhã"
                )
            }
            TecnicoScreen(
                viewModel = viewModel,
                onVerTodosClick = {
                    navController.navigate(FerramentasTecnicoRoute)
                }
            )
        }

        composable<FerramentasTecnicoRoute> {
            val session = SessionManager.atual
            val viewModel = viewModel {
                FerramentasViewModel(
                    ferramentasDataSource = AppModule.ferramentaRemoteDataSource,
                    idTecnico = session?.idFunc ?: -1
                )
            }
            FerramentasScreen(viewModel = viewModel)
        }

        composable<HistoricoTecnicoRoute> {
            val viewModel = viewModel {

                HistoricoViewModel()

            }
            HistoricoScreen(viewModel = viewModel)
        }

        composable<ArmariosGestorRoute> {
            val viewModel = viewModel {
                ArmariosViewModel(
                    ferramentas = AppModule.ferramentaRemoteDataSource,
                    armarios = AppModule.armarioRemoteDataSource,
                    alertas = AppModule.alertaRemoteDataSource
                )
            }

            val state by viewModel.state.collectAsState()

            GestorScaffold(
                itemSelecionado = "armarios",
                alertasAtivos = state.alertasAtivos,
                nomeGestor = SessionManager.atual?.nome ?: "",
                cargo = "Gestor de Armazém",
                onNavegar = { id -> navegarGestor(navController, id) },
                onLogout = {/*todo: terminar sessão*/ },
            ) { abrirMenu -> ArmariosScreen(viewModel = viewModel, onMenuClick = abrirMenu) }


        }

        composable<AlertasGestorRoute> {
            val viewModel = viewModel {
                AlertasViewModel(
                    alertas = AppModule.alertaRemoteDataSource
                )
            }

            val state by viewModel.state.collectAsState()

            GestorScaffold(
                itemSelecionado = "alertas",
                alertasAtivos = state.alertasAtivos,
                nomeGestor = SessionManager.atual?.nome ?: "",
                cargo = "Gestor de Armazém",
                onNavegar = { id -> navegarGestor(navController, id) },
                onLogout = {/*todo: terminar sessão*/ },
            ) { abrirMenu ->
                AlertasScreen(viewModel = viewModel, onMenuClick = abrirMenu)
            }
        }

        composable<FerramentasGestorRoute> {
            val viewModel = viewModel {
                FerramentasGestorViewModel(
                    ferramentas = AppModule.ferramentaRemoteDataSource,
                    alertas = AppModule.alertaRemoteDataSource
                )
            }
            val state by viewModel.state.collectAsState()

            GestorScaffold(
                itemSelecionado = "ferramentas",
                alertasAtivos = state.alertasAtivos,
                nomeGestor = SessionManager.atual?.nome ?: "",
                cargo = "Gestor de Armazém",
                onNavegar = { id -> navegarGestor(navController, id) },
                onLogout = {/*todo: terminar sessão*/ },
            ) { abrirMenu ->
                FerramentasGestorScreen(viewModel = viewModel, onMenuClick = abrirMenu)
            }


        }
    }
}

private fun navegarGestor(navController: NavController, id: String) {
    val rota: Any? = when (id) {
        "dashboard" -> GestorRoute
        "armarios" -> ArmariosGestorRoute
        "ferramentas" -> FerramentasGestorRoute
        "alertas" -> AlertasGestorRoute
        else -> null   // todo: "tarefas"/"historico" que ainda não têm ecrã
    }
    rota?.let {
        navController.navigate(it) { launchSingleTop = true }  // evita empilhar o mesmo ecrã
    }
}