package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.dashboard

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// Importações dos teus componentes globais
import pfc.a50727a50799.smarttool_cabinet.ui.SectionHeader
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.WelcomeCard

// Importações do Tema
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapSurfaceGrey
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextTitle
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapAlmostGreen

@Composable
private fun BODashboardScreenContent(
    state: BODashboardUiState,
    onMenuClick: () -> Unit,
    onVerTodosUtilizadores: () -> Unit,
    onVerTodosArmarios: () -> Unit
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
                modifier = Modifier
                    .fillMaxSize()
                    .background(ScreenBg)
            ) {
                TopBar(
                    titulo = "Dashboard",
                    mostrarAlertas = false,
                    alertasAtivos = 0,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        WelcomeCard(nomeGestor = state.nomeBackOffice, turno = state.turno, cargo = state.cargo, color = TapAlmostGreen)
                    }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                EstatisticaBOSimples(titulo = "Total Utilizadores", valor = state.estatisticas.totalUtilizadores.toString(), modifier = Modifier.weight(1f))
                                EstatisticaBOSimples(titulo = "Ativos Hoje", valor = state.estatisticas.ativosHoje.toString(), modifier = Modifier.weight(1f))
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                EstatisticaBOSimples(titulo = "Gestores", valor = state.estatisticas.gestores.toString(), modifier = Modifier.weight(1f))
                                EstatisticaBOSimples(titulo = "Técnicos", valor = state.estatisticas.tecnicos.toString(), modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    item {
                        SectionHeader(titulo = "Utilizadores recentes", onVerTodos = onVerTodosUtilizadores)
                    }

                    items(state.utilizadoresRecentes) { user ->
                        UtilizadorRecenteCard(user)
                    }
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        SectionHeader(titulo = "Estado dos Armários", onVerTodos = onVerTodosArmarios)
                    }

                    items(state.armarios) { armario ->
                        ArmarioResumoCard(armario)
                    }
                }
            }
    }
}

@Composable
fun BODashboardScreen(
    viewModel: BODashboardViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    BODashboardScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onVerTodosUtilizadores = {},
        onVerTodosArmarios = {}
    )
}
@Composable
fun EstatisticaBOSimples(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = titulo, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = valor, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextTitle)
        }
    }
}

@Composable
fun UtilizadorRecenteCard(user: UtilizadorRecenteUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo com Iniciais
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TapSurfaceGrey.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = user.iniciais, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TapBrandDark)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextTitle)
                Text(text = user.cargoSubtitulo, fontSize = 11.sp, color = TextSecondary)
            }

            // Tag de Cargo dinâmica
            val (bgColor, textColor) = when (user.cargoTag) {
                "Gestor" -> Pair(AlertOrange.copy(alpha = 0.15f), AlertOrangeText)
                "Técnico" -> Pair(TapLightGreen.copy(alpha = 0.2f), TapBrandDark)
                else -> Pair(TapSurfaceGrey.copy(alpha = 0.3f), TextSecondary) // Back Office
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = user.cargoTag, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ArmarioResumoCard(armario: ArmarioResumoUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = armario.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextTitle)

            val (bgColor, textColor) = when (armario.estado) {
                "Online" -> Pair(TapLightGreen.copy(alpha = 0.2f), TapBrandDark)
                "Alerta" -> Pair(AlertOrange.copy(alpha = 0.15f), AlertOrangeText)
                else -> Pair(TapSurfaceGrey.copy(alpha = 0.3f), TextSecondary) // Offline
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = armario.estado, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun PreviewBODashboard() {
    AppTheme {
        BODashboardScreenContent(
            state = BODashboardUiState(
                isLoading = false,
                error = null,
                nomeBackOffice = "Matilde Valente",
                cargo = "Back Office",
                turno = "8:00-16:00",
                estatisticas = EstatisticasBOUi(6, 5, 1, 4),
                utilizadoresRecentes = listOf(
                    UtilizadorRecenteUi(1, "Carlos Gonçalves", "CG", "Técnico · Turno Manhã", "Técnico"),
                    UtilizadorRecenteUi(2, "Gonçalo Charneca", "GC", "Gestor · Turno Manhã", "Gestor"),
                    UtilizadorRecenteUi(3, "Luísa Sampaio", "LS", "Técnico · Turno Manhã", "Técnico"),
                    UtilizadorRecenteUi(4, "Tiago Dias", "TD", "Técnico · Turno Manhã", "Técnico")
                ),
                armarios = listOf(
                    ArmarioResumoUi(1, "Armário 1 · Ferramentas Gerais", "Online"),
                    ArmarioResumoUi(2, "Armário 2 · Ferramentas de Precisão", "Online"),
                    ArmarioResumoUi(3, "Armário 3 · Ferramentas Elétricas", "Alerta")
                )
            ),
            onMenuClick = {},
            onVerTodosUtilizadores = {},
            onVerTodosArmarios = {}
        )
    }
}