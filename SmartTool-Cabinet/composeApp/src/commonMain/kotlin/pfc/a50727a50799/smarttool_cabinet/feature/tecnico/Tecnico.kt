package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

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

// Importações a partir da tua nova pasta UI global
import pfc.a50727a50799.smarttool_cabinet.ui.SectionHeader
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.WelcomeCard

// Importações do Tema
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapAlert
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapAlmostGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue

@Composable
private fun TecnicoScreenContent(
    state: TecnicoUiState
) {
    // Bloco `when` de Loading, Erro e UI normal, EXATAMENTE igual ao Gestor
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
                // TopBar global
                TopBar(
                    titulo = "Dashboard",
                    alertasAtivos = 0, // Técnico não tem contador na TopBar no Figma
                    mostrarAlertas = false,
                    onMenu = {}
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        WelcomeCard(state.nomeTecnico, state.turno, state.cargo, color = TapGreenishBlue)
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            EstatisticaCard(
                                titulo = "Ferramentas em uso",
                                valor = state.ferramentasEmUso.toString(),
                                rodapeTexto = "Minhas",
                                rodapeBgColor = AlertOrange.copy(alpha = 0.15f),
                                rodapeTextColor = AlertOrangeText,
                                modifier = Modifier.weight(1f)
                            )
                            EstatisticaCard(
                                titulo = "Para devolver",
                                valor = state.ferramentasParaDevolver.toString(),
                                rodapeTexto = "Prazo hoje",
                                rodapeBgColor = TapAlert.copy(alpha = 0.12f),
                                rodapeTextColor = TapAlert,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        SectionHeader("As minhas ferramentas", onVerTodos = {})
                    }

                    items(state.minhasFerramentas) { ferramenta ->
                        FerramentaTecnicoCard(ferramenta)
                    }
                }
            }
    }
}

@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    TecnicoScreenContent(
        state = state
    )
}

// ==========================================
// COMPONENTES EXCLUSIVOS DO ECRÃ DO TÉCNICO
// ==========================================

@Composable
fun EstatisticaCard(
    titulo: String, valor: String, rodapeTexto: String,
    rodapeBgColor: Color, rodapeTextColor: Color, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = titulo, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = valor, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(PillShape)
                    .background(rodapeBgColor)
                    .padding(horizontal = 8.dp, vertical = 1.dp),
            ) {
                Text(text = rodapeTexto, color = rodapeTextColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FerramentaTecnicoCard(ferramenta: FerramentaTecnicoUi) {
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
            Box(
                modifier = Modifier.size(42.dp).clip(RoundedCornerShape(8.dp)).background(TapGreenishBlue.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) { Text("🔧", fontSize = 18.sp) }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = ferramenta.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Text(text = ferramenta.detalhes, fontSize = 11.sp, color = Color.Gray)
            }

            Box(
                modifier = Modifier.clip(PillShape).background(AlertOrange.copy(alpha = 0.15f)).padding(horizontal = 12.dp, vertical = 2.dp)
            ) {
                Text(text = ferramenta.estado, color = AlertOrangeText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Preview do ecrã Técnico para visualização no Android Studio.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        TecnicoScreenContent(
            TecnicoUiState(
                isLoading = false,
                error = null,
                nomeTecnico = "Luísa Sampaio",
                cargo = "Técnica",
                turno = "8:00-16:00",
                ferramentasEmUso = 2,
                ferramentasParaDevolver = 1,
                minhasFerramentas = listOf(
                    FerramentaTecnicoUi(
                        id = 1,
                        nome = "Chave de Caixa 10mm",
                        detalhes = "Arm. 1 · Chaves",
                        estado = "Em Uso"
                    )
                )
            )
        )
    }
}