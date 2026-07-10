package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.armarios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pfc.a50727a50799.smarttool_cabinet.ui.ArmarioCard
import pfc.a50727a50799.smarttool_cabinet.ui.BarraPesquisa
import pfc.a50727a50799.smarttool_cabinet.ui.FilterChip
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
/**
 * Parte visual do ecrã de gestão de armários do Backoffice.
 *
 * Apenas apresenta a informação recebida através do estado e encaminha
 * as ações do utilizador, como a pesquisa, alteração de filtros e abertura
 * do menu lateral.
 *
 * @param state Estado atual do ecrã.
 * @param onSearchChange Chamado quando o texto da pesquisa é alterado.
 * @param onFiltroChange Chamado quando o filtro selecionado muda.
 * @param onMenuClick Chamado quando o utilizador abre o menu lateral.
 */
@Composable
private fun BOArmariosScreenContent(
    state: BOArmariosUiState,
    onSearchChange: (String) -> Unit,
    onFiltroChange: (FiltroBOArmario) -> Unit,
    onMenuClick: () -> Unit
) {
    when {
        state.isLoading ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        state.error != null ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error, color = MaterialTheme.colorScheme.error)
            }

        else ->
            Column(
                modifier = Modifier.fillMaxSize().background(ScreenBg)
            ) {
                TopBar(
                    titulo = "Armários do Sistema",
                    alertasAtivos = state.alertasAtivos,
                    mostrarAlertas = false,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Armários",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 28.sp,
                            )
                            Text(
                                text = "Gestão e monitorização dos armários.",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    item {
                        BarraPesquisa(
                            label = "Pesquisar armários...",
                            query = state.searchQuery,
                            onQueryChange = onSearchChange
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FiltroBOArmario.entries.forEach { filtro ->
                                FilterChip(
                                    label = filtro.label,
                                    isSelected = state.filtroAtual == filtro,
                                    onClick = { onFiltroChange(filtro) }
                                )
                            }
                        }
                    }

                    items(state.armariosFiltrados) { armario ->
                        ArmarioCard(armario)
                    }
                }
            }
    }
}
/**
 * Liga o [BOArmariosViewModel] ao [BOArmariosScreenContent].
 *
 * Observa o estado do ViewModel e encaminha os eventos da interface,
 * como a pesquisa de armários e a alteração dos filtros.
 *
 * @param viewModel ViewModel responsável pela gestão do estado do ecrã.
 * @param onMenuClick Chamado quando o utilizador abre o menu lateral.
 */
@Composable
fun BOArmariosScreen(
    viewModel: BOArmariosViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    BOArmariosScreenContent(
        state = state,
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange,
        onMenuClick = onMenuClick
    )
}