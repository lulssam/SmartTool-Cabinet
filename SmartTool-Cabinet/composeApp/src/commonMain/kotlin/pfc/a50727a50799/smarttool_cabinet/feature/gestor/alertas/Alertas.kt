package pfc.a50727a50799.smarttool_cabinet.feature.gestor.alertas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.armarios.ArmariosViewModel
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.AlertaUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.Gravidade
import pfc.a50727a50799.smarttool_cabinet.ui.AlertaCard
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary


// #mycode
/**
 * Mostra o conteúdo do ecrã de alertas.
 *
 * Dependendo do estado atual, apresenta um indicador de carregamento,
 * uma mensagem de erro ou a lista de alertas disponíveis.
 *
 * @param state Informação necessária para apresentar o estado atual do ecrã.
 * @param onMenuClick Função chamada quando o utilizador abre o menu lateral.
 * @param onRefresh Função chamada quando o utilizador "puxa para atualizar" a lista.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlertasScreenContent(
    state: AlertasUiState,
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
                    titulo = "Alertas",
                    alertasAtivos = state.alertasAtivos,
                    mostrarAlertas = false,
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
                        Text(
                            text = "Alertas",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 28.sp,
                        )

                        Text(
                            text = "Ocorrências que requerem atenção.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }

                    // alertas
                    items(state.alertas) {
                        AlertaCard(
                            gravidade = it.gravidade,
                            titulo = it.titulo,
                            descricao = it.descricao,
                            horas = it.hora
                        )
                    }
                }
                }
            }

    }
}
/**
 * Ecrã principal dos alertas do gestor.
 *
 * Obtém o estado atual através do ViewModel e entrega os dados
 * ao conteúdo visual do ecrã.
 *
 * @param viewModel ViewModel responsável por fornecer os dados dos alertas.
 * @param onMenuClick Função chamada quando o utilizador abre o menu lateral.
 */
@Composable
fun AlertasScreen(
    viewModel: AlertasViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    AlertasScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onRefresh = viewModel::refresh
    )
}
/**
 * Preview do ecrã de alertas para visualização no Android Studio.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        AlertasScreenContent(
            AlertasUiState(
                alertas = listOf(
                    AlertaUi(
                        "Ferramentas em falta",
                        "F-004 Chave de Fendas não devolvida",
                        "16:34",
                        Gravidade.CRITICO
                    ),
                    AlertaUi(
                        "Ferramenta em mau estado",
                        "F-005 Chave Inglesa para manutenção",
                        "9:30",
                        Gravidade.AVISO
                    )
                ),
            ),
            onMenuClick = {}
        )
    }
}