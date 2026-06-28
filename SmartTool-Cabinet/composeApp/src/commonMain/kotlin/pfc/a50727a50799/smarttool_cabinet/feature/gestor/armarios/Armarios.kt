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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
private fun ArmariosScreenContent(
    state: ArmariosUiState,
    onSearchChange: (String) -> Unit,
    onFiltroChange: (FiltroArmario) -> Unit
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
                    onMenu = {}
                )


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // titulo
                    item {
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

@Composable
fun ArmariosScreen(
    viewModel: ArmariosViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()
    ArmariosScreenContent(
        state = state,
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange
    )
}

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
            onFiltroChange = {}

        )
    }
}