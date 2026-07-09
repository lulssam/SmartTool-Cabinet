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
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.armarios.BOArmariosScreen
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.armarios.BOArmariosViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.dashboard.BODashboardScreen
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.dashboard.BODashboardViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.historico.BOHistoricoScreen
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.historico.BOHistoricoViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.menu.BackOfficeScaffold
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.utilizadores.BOUtilizadoresScreen
import pfc.a50727a50799.smarttool_cabinet.feature.backoffice.utilizadores.BOUtilizadoresViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.email.emailScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.alertas.AlertasScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.alertas.AlertasViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.armarios.ArmariosScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.armarios.ArmariosViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.GestorScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.GestorViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.FerramentasGestorScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas.FerramentasGestorViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.historico.HistoricoGestorScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.historico.HistoricoGestorViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.menu.GestorScaffold
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.TarefasGestorScreen
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.TarefasGestorViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.AlertasGestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.ArmariosGestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.BackOfficeArmariosRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.BackOfficeHistoricoRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.BackOfficeUtilizadoresRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.FerramentasGestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.FerramentasTecnicoRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.HistoricoGestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.HistoricoTecnicoRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.LoginEmailRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.SSORoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.TarefasGestorRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.TarefasTecnicoRoute
import pfc.a50727a50799.smarttool_cabinet.feature.navigation.routeForRole
import pfc.a50727a50799.smarttool_cabinet.feature.session.SessionUiState
import pfc.a50727a50799.smarttool_cabinet.feature.session.SessionViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.session.SplashScreen
import pfc.a50727a50799.smarttool_cabinet.feature.sso.SSOScreen
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.ferramentas.FerramentasScreen
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.ferramentas.FerramentasViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.historico.HistoricoScreen
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.historico.HistoricoViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.dashboard.TecnicoScreen
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.dashboard.TecnicoViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.menu.TecnicoScaffold
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.tarefas.TarefasTecnicoScreen
import pfc.a50727a50799.smarttool_cabinet.feature.tecnico.tarefas.TarefasTecnicoViewModel
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
//#my_code
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

        // =============== GESTOR ===============

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
                onLogout = { fazerLogout(navController, authRepository) },
            ) { abrirMenu ->
                GestorScreen(
                    viewModel = viewModel,
                    onVerArmarios = { navController.navigate(ArmariosGestorRoute) },
                    onVerAlertas = { navController.navigate(AlertasGestorRoute) },
                    onMenuClick = abrirMenu
                )
            }

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
                onLogout = { fazerLogout(navController, authRepository) },
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
                onLogout = { fazerLogout(navController, authRepository) },
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
                onLogout = { fazerLogout(navController, authRepository) },
            ) { abrirMenu ->
                FerramentasGestorScreen(viewModel = viewModel, onMenuClick = abrirMenu)
            }


        }

        composable<HistoricoGestorRoute> {
            val viewModel = viewModel {
                HistoricoGestorViewModel(
                    historico = AppModule.historicoRemoteDataSource,
                    alertas = AppModule.alertaRemoteDataSource
                )
            }

            val state by viewModel.state.collectAsState()

            GestorScaffold(
                itemSelecionado = "historico",
                alertasAtivos = state.alertasAtivos,
                nomeGestor = SessionManager.atual?.nome ?: "",
                cargo = "Gestor de Armazém",
                onNavegar = { id -> navegarGestor(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) },
            ) { abrirMenu ->
                HistoricoGestorScreen(viewModel = viewModel, onMenuClick = abrirMenu)
            }
        }

        composable<TarefasGestorRoute> {
            val viewModel = viewModel {
                TarefasGestorViewModel(
                    tarefas = AppModule.tarefaRemoteDataSource,
                    alertas = AppModule.alertaRemoteDataSource,
                    ferramentas = AppModule.ferramentaRemoteDataSource,
                    tecnicos = AppModule.tecnicoRemoteDataSource,
                    idGestor = SessionManager.atual?.idFunc ?: -1
                )
            }
            val state by viewModel.state.collectAsState()

            GestorScaffold(
                itemSelecionado = "tarefas",
                alertasAtivos = state.alertasAtivos,
                nomeGestor = SessionManager.atual?.nome ?: "",
                cargo = "Gestor de Armazém",
                onNavegar = { id -> navegarGestor(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) },
            ) { abrirMenu ->
                TarefasGestorScreen(viewModel = viewModel, onMenuClick = abrirMenu)
            }
        }

        // =============== BACK OFFICE ===============
        composable<BackOfficeRoute> {
            val viewModel = viewModel {
                BODashboardViewModel(
                    backOfficeDataSource = AppModule.backOfficeRemoteDataSource
                )
            }

            BackOfficeScaffold(
                itemSelecionado = "dashboard",
                nomeBO = SessionManager.atual?.nome ?: "",
                cargo = "Back Office",
                onNavegar = { id -> navegarBO(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) },
            ) { abrirMenu ->
                BODashboardScreen(
                    viewModel = viewModel,
                    onMenuClick = abrirMenu,
                    onVerTodosArmarios = { navController.navigate(BackOfficeArmariosRoute) },
                    onVerTodosUtilizadores = { navController.navigate(BackOfficeUtilizadoresRoute) }
                )
            }
        }

        composable<BackOfficeUtilizadoresRoute> {
            val viewModel = viewModel {
                BOUtilizadoresViewModel(
                    backOfficeDataSource = AppModule.backOfficeRemoteDataSource
                )
            }
            BackOfficeScaffold(
                itemSelecionado = "utilizadores",
                nomeBO = SessionManager.atual?.nome ?: "",
                cargo = "Back Office",
                onNavegar = { id -> navegarBO(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) },
            ) { abrirMenu ->
                BOUtilizadoresScreen(
                    viewModel = viewModel,
                    onMenuClick = abrirMenu
                )
            }
        }
        composable<BackOfficeArmariosRoute> {
            val viewModel = viewModel {
                BOArmariosViewModel(
                    ferramentas = AppModule.ferramentaRemoteDataSource,
                    armarios = AppModule.armarioRemoteDataSource,

                    )
            }

            BackOfficeScaffold(
                itemSelecionado = "armarios",
                nomeBO = SessionManager.atual?.nome ?: "",
                cargo = "Back Office",
                onNavegar = { id -> navegarBO(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) },
            ) { abrirMenu ->
                BOArmariosScreen(
                    viewModel = viewModel,
                    onMenuClick = abrirMenu
                )
            }
        }

        composable<BackOfficeHistoricoRoute> {
            val viewModel = viewModel {
                BOHistoricoViewModel(
                    historico = AppModule.historicoRemoteDataSource,
                )
            }
            BackOfficeScaffold(
                itemSelecionado = "historico",
                nomeBO = SessionManager.atual?.nome ?: "",
                cargo = "Back Office",
                onNavegar = { id -> navegarBO(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) },
            ) { abrirMenu ->
                BOHistoricoScreen(
                    viewModel = viewModel,
                    onMenuClick = abrirMenu
                )
            }
        }


        // =============== TECNICO ===============

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

            TecnicoScaffold(
                itemSelecionado = "dashboard",
                nomeTecnico = SessionManager.atual?.nome ?: "",
                cargo = "Técnico",
                onNavegar = { id -> navegarTecnico(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) }
            ) { abrirMenu ->
                TecnicoScreen(
                    viewModel = viewModel,
                    onVerTodosClick = { navController.navigate(FerramentasTecnicoRoute) },
                    onMenuClick = abrirMenu
                )
            }
        }

        composable<FerramentasTecnicoRoute> {
            val session = SessionManager.atual
            val viewModel = viewModel {
                FerramentasViewModel(
                    ferramentasDataSource = AppModule.ferramentaRemoteDataSource,
                    idTecnico = session?.idFunc ?: -1
                )
            }

            TecnicoScaffold(
                itemSelecionado = "ferramentas",
                nomeTecnico = SessionManager.atual?.nome ?: "",
                cargo = "Técnico",
                onNavegar = { id -> navegarTecnico(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) }
            ) { abrirMenu ->
                FerramentasScreen(viewModel = viewModel, onMenuClick = abrirMenu)
            }
        }

        composable<HistoricoTecnicoRoute> {
            val session = SessionManager.atual
            val viewModel = viewModel {
                HistoricoViewModel(
                    historicoDataSource = AppModule.historicoRemoteDataSource,
                    idTecnico = session?.idFunc ?: -1
                )
            }

            TecnicoScaffold(
                itemSelecionado = "historico",
                nomeTecnico = SessionManager.atual?.nome ?: "",
                cargo = "Técnico",
                onNavegar = { id -> navegarTecnico(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) }
            ) { abrirMenu ->
                HistoricoScreen(viewModel = viewModel, onMenuClick = abrirMenu)
            }
        }

        composable<TarefasTecnicoRoute> {
            val session = SessionManager.atual
            val viewModel = viewModel {
                TarefasTecnicoViewModel(
                    tarefas = AppModule.tarefaRemoteDataSource,
                    idTecnico = session?.idFunc ?: -1,
                    turno = session?.turno ?: ""
                )
            }

            TecnicoScaffold(
                itemSelecionado = "tarefas",
                nomeTecnico = SessionManager.atual?.nome ?: "",
                cargo = "Técnico",
                onNavegar = { id -> navegarTecnico(navController, id) },
                onLogout = { fazerLogout(navController, authRepository) }
            ) {
                abrirMenu ->
                TarefasTecnicoScreen(viewModel = viewModel, onMenuClick = abrirMenu)
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
        "historico" -> HistoricoGestorRoute
        "tarefas" -> TarefasGestorRoute
        else -> null
    }
    rota?.let {
        navController.navigate(it) { launchSingleTop = true }  // evita empilhar o mesmo ecrã
    }
}

private fun navegarTecnico(navController: NavController, id: String) {
    val rota: Any? = when (id) {
        "dashboard" -> TecnicoRoute
        "ferramentas" -> FerramentasTecnicoRoute
        "historico" -> HistoricoTecnicoRoute
        "tarefas" -> TarefasTecnicoRoute
        else -> null
    }
    rota?.let {
        navController.navigate(it) { launchSingleTop = true }  // evita empilhar o mesmo ecrã
    }
}


private fun navegarBO(navController: NavController, id: String) {
    val rota: Any? = when (id) {
        "dashboard" -> BackOfficeRoute
        "armarios" -> BackOfficeArmariosRoute
        "utilizadores" -> BackOfficeUtilizadoresRoute
        "historico" -> BackOfficeHistoricoRoute
        else -> null
    }
    rota?.let {
        navController.navigate(it) { launchSingleTop = true }  // evita empilhar o mesmo ecrã
    }
}

private fun fazerLogout(
    navController: NavController,
    authRepository: AuthRepository
) {
    authRepository.logout() // terminar sessão na firebase
    SessionManager.atual = null // esquecer o user atual na memória
    navController.navigate(WelcomeRoute) {
        popUpTo(navController.graph.id) {
            inclusive = true
        } // limpar stack para não dar para voltar atrás
    }
}
//#my_code end