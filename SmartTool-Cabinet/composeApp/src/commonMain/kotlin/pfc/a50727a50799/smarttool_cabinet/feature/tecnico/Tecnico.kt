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

// Importações dos teus componentes partilhados
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.SectionHeader
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.TopBar
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.WelcomeCard

// Importações oficiais do teu Tema!
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapAlert
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue

@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    TecnicoScreenContent(
        state = state,
        onMenuClick = { },
        onVerTodosClick = { }
    )
}

@Composable
fun TecnicoScreenContent(
    state: TecnicoUiState,
    onMenuClick: () -> Unit,
    onVerTodosClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
    ) {
        TopBar(
            titulo = "Dashboard",
            mostrarAlertas = false, // Esconde a pill de alertas para o Técnico
            onMenu = onMenuClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                Box(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    WelcomeCard(
                        nomeGestor = state.nomeTecnico,
                        cargo = state.cargo,
                        turno = state.turno
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Cartão "Ferramentas em Uso" (Laranja)
                    EstatisticaCard(
                        titulo = "Ferramentas em uso",
                        valor = state.ferramentasEmUso.toString(),
                        rodapeTexto = "Minhas",
                        rodapeBgColor = AlertOrange.copy(alpha = 0.15f), // Usa as cores do teu tema com opacidade
                        rodapeTextColor = AlertOrangeText,
                        modifier = Modifier.weight(1f)
                    )
                    // Cartão "Para Devolver" (Vermelho/Rosa)
                    EstatisticaCard(
                        titulo = "Para devolver",
                        valor = state.ferramentasParaDevolver.toString(),
                        rodapeTexto = "Prazo hoje",
                        rodapeBgColor = TapAlert.copy(alpha = 0.12f), // TapAlert com opacidade fica o rosa clarinho
                        rodapeTextColor = TapAlert,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                SectionHeader(
                    titulo = "As minhas ferramentas",
                    onVerTodos = onVerTodosClick
                )
            }

            items(state.minhasFerramentas) { ferramenta ->
                Box(Modifier.padding(horizontal = 20.dp)) {
                    FerramentaTecnicoCard(ferramenta)
                }
            }
        }
    }
}

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
                    .clip(RoundedCornerShape(6.dp))
                    .background(rodapeBgColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
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
            // Quadrado de fundo do ícone (Usa o verde da TAP com opacidade muito baixa)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TapGreenishBlue.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🔧", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = ferramenta.nome, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Text(text = ferramenta.detalhes, fontSize = 11.sp, color = Color.Gray)
            }

            // Etiqueta "Em Uso" com as tuas cores do tema
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(AlertOrange.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = ferramenta.estado, color = AlertOrangeText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTecnicoFigma() {
    AppTheme {
        TecnicoScreenContent(
            state = TecnicoUiState(
                minhasFerramentas = listOf(
                    FerramentaTecnicoUi(
                        id = 1,
                        nome = "Chave de Caixa 10mm",
                        detalhes = "Arm. 1 · Chaves",
                        estado = "Em Uso"
                    )
                )
            ),
            onMenuClick = {},
            onVerTodosClick = {}
        )
    }
}