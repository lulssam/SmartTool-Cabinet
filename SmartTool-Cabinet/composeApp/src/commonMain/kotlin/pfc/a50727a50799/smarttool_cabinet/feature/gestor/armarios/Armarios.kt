package pfc.a50727a50799.smarttool_cabinet.feature.gestor.armarios

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.ArmarioUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.EstadoArmario
import pfc.a50727a50799.smarttool_cabinet.ui.ArmarioCard
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import pfc.a50727a50799.smarttool_cabinet.ui.BarraPesquisa
import pfc.a50727a50799.smarttool_cabinet.ui.FilterChip
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
/**
 * Mostra o conteúdo do ecrã de Armários.
 *
 * Dependendo do estado atual, apresenta um indicador de carregamento,
 * uma mensagem de erro ou a lista de armários, permitindo pesquisar
 * e filtrar os resultados apresentados.
 *
 * @param state Informação necessária para mostrar o estado atual do ecrã.
 * @param onSearchChange Função chamada quando o texto da pesquisa é alterado.
 * @param onFiltroChange Função chamada quando o utilizador seleciona um filtro.
 * @param onMenuClick Função chamada quando o utilizador abre o menu lateral.
 * @param onRefresh Função chamada quando o utilizador "puxa para atualizar" a lista.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArmariosScreenContent(
    state: ArmariosUiState,
    onSearchChange: (String) -> Unit,
    onFiltroChange: (FiltroArmario) -> Unit,
    onMenuClick: () -> Unit,
    onRefresh: () -> Unit = {}
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
                    titulo = "Armários",
                    alertasAtivos = state.alertasAtivos,
                    mostrarAlertas = true,
                    onMenu = onMenuClick
                )


                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                    // titulo
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Armários",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 28.sp,
                            )
                            Text(
                                text = "Gestão e monitorização dos armários",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // barra de pesquisa
                    item {
                        BarraPesquisa(
                            label = "Pesquisar armários...",
                            query = state.searchQuery,
                            onQueryChange = onSearchChange
                        )
                    }

                    // coisos dos filtros
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            FiltroArmario.entries.forEach { filtro ->
                                FilterChip(
                                    label = filtro.label,
                                    isSelected = state.filtroAtual == filtro,
                                    onClick = { onFiltroChange(filtro) }
                                )
                            }
                        }
                    }

                    // armarios
                    items(state.armariosFiltrados) { armario ->
                        ArmarioCard(armario)
                    }
                }
                }
            }
    }
}
/**
 * Ecrã principal de gestão de armários.
 *
 * Obtém o estado atual através do ViewModel e liga as ações
 * do utilizador às funções responsáveis por atualizar os dados.
 *
 * @param viewModel ViewModel responsável por fornecer os dados dos armários.
 * @param onMenuClick Função chamada quando o utilizador abre o menu lateral.
 */
@Composable
fun ArmariosScreen(
    viewModel: ArmariosViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {

    val state by viewModel.state.collectAsState()
    ArmariosScreenContent(
        state = state,
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange,
        onMenuClick = onMenuClick,
        onRefresh = viewModel::refresh
    )
}
/**
 * Preview do ecrã Armários para visualização no Android Studio.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        ArmariosScreenContent(
            ArmariosUiState(
                armarios = listOf(
                    ArmarioUi(
                        nome = "Armario 1 - Ferramentas Eletricas",
                        slotsOcupados = 3,
                        slotsTotal = 12,
                        trancado = false,
                        emFalta = 1,
                        estadoArmario = EstadoArmario.OPERACIONAL
                    )
                )
            ),

            onSearchChange = {},
            onFiltroChange = {},
            onMenuClick ={}
        )
    }
}