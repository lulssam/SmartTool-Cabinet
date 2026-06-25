package pfc.a50727a50799.smarttool_cabinet.feature.gestor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pfc.a50727a50799.smarttool_cabinet.ui.AlertaCard
import pfc.a50727a50799.smarttool_cabinet.ui.ArmarioCard
import pfc.a50727a50799.smarttool_cabinet.ui.EstadoFerramentasCard
import pfc.a50727a50799.smarttool_cabinet.ui.SectionHeader
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.WelcomeCard
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg


@Composable
private fun GestorScreenContent(
    state: GestorUiState
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
            Column(Modifier.fillMaxSize().background(ScreenBg)) {
                TopBar(
                    titulo = "Dashboard",
                    alertasAtivos = state.alertas.size,
                    mostrarAlertas = state.alertas.isNotEmpty(),
                    onMenu = {}
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item { WelcomeCard(nomeGestor = state.nomeGestor, turno = state.turno, cargo = "Gestor de Armazém") }
                    item { EstadoFerramentasCard(state.estatisticas) }
                    item { SectionHeader("Estado dos Armários", onVerTodos = {}) }
                    items(state.armarios) { ArmarioCard(it) }
                    item { SectionHeader("Alertas Recentes", onVerTodos = {}) }
                    items(state.alertas) {
                        AlertaCard(
                            it.gravidade,
                            it.titulo,
                            it.descricao,
                            it.hora
                        )
                    }
                }
            }
    }
}


@Composable
fun GestorScreen(
    viewModel: GestorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    GestorScreenContent(
        state = state
    )
}

/**
 * Preview do ecrã Gestor para visualização no Android Studio.
 *
 * Utiliza o tema do projeto e um estado por defeito para
 * permitir a pré-visualização durante o desenvolvimento.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    AppTheme {
        GestorScreenContent(
            GestorUiState(
                estatisticas = EstatisticasFerramentas(9, 3, 1, 2),
                nomeGestor = "Gonçalo Charneca",
                turno = "8:00-16:00",
                armarios = listOf(
                    ArmarioUi(
                        "Armário 1 - Ferramentas Gerais",
                        11,
                        12,
                        true,
                        1,
                        EstadoArmario.AVARIADO
                    ),
                    ArmarioUi(
                        "Armário 3 - Ferramentas Elétricas",
                        10,
                        12,
                        false,
                        2,
                        EstadoArmario.OPERACIONAL
                    )
                ),
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
                )
            )
        )
    }
}
