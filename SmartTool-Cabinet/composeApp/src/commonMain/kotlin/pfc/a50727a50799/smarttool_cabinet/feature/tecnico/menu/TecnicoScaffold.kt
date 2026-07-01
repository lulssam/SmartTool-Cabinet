package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.menu

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import pfc.a50727a50799.smarttool_cabinet.ui.DrawerSheet


/**
 * Invólucro partilhado por todos os ecrãs do técnico. Mete um menu lateral
 * (drawer) à volta do conteúdo: trata de o abrir/fechar e de navegar quando se
 * toca numa opção. O ecrã lá dentro recebe a função `abrirMenu` para ligar ao
 * botão de menu da sua TopBar.
 *
 * @param itemSelecionado O `id` da secção atual (fica destacada no menu).
 * @param nomeTecnico Nome mostrado no rodapé do menu.
 * @param cargo Cargo mostrado por baixo do nome.
 * @param onNavegar Chamado com o `id` da opção tocada (quem navega é o NavHost).
 * @param onLogout Chamado quando se toca em sair.
 * @param content O ecrã a mostrar. Recebe `abrirMenu` para abrir o menu.
 */
@Composable
fun TecnicoScaffold(
    itemSelecionado: String,
    nomeTecnico: String,
    cargo: String,
    onNavegar: (String) -> Unit,
    onLogout: () -> Unit,
    content: @Composable (abrirMenu: () -> Unit) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,

        // só permite gesto quando já está aberto -> não choca com o scroll
        // horizontal dos chips. Para abrir, usa-se o botão de menu.
        gesturesEnabled = drawerState.isOpen,

        drawerContent = {
            DrawerSheet(
                opcoes = opcoesTecnico(),
                idSelecionado = itemSelecionado,
                nomeUtilizador = nomeTecnico,
                cargo = cargo,
                onOpcaoClick = { opcao ->
                    scope.launch { drawerState.close() }
                    if (opcao.id != itemSelecionado) onNavegar(opcao.id) // não renavega para o mesmo sitio
                },
                onLogoutClick = {
                    scope.launch { drawerState.close() }
                    onLogout()
                }
            )
        }
    ) {
        content({ scope.launch { drawerState.open() } })
    }
}