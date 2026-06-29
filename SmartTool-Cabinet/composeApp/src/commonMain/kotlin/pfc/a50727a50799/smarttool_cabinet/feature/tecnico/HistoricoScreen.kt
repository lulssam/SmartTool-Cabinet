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

// Importação do Componente Partilhado a partir da pasta UI
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar

// Importações do Tema
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextTitle

private val AzulRetirou = Color(0xFF2563EB)
private val FundoAzulRetirou = Color(0xFFEFF6FF)

@Composable
private fun HistoricoScreenContent(
    state: HistoricoUiState,
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
                modifier = Modifier
                    .fillMaxSize()
                    .background(ScreenBg)
            ) {
                TopBar(
                    titulo = "Histórico",
                    mostrarAlertas = false,
                    alertasAtivos = 0,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(text = "Histórico", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextTitle)
                            Text(text = "Últimos 7 dias de movimentos", fontSize = 14.sp, color = TextSecondary)
                        }
                    }

                    state.secoes.forEach { secao ->
                        item {
                            Text(
                                text = secao.data,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        items(secao.movimentos) { movimento ->
                            HistoricoItemCard(item = movimento)
                        }
                    }
                }
            }
    }
}

@Composable
fun HistoricoScreen(
    viewModel: HistoricoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    HistoricoScreenContent(
        state = state,
        onMenuClick = {}
    )
}

@Composable
fun HistoricoItemCard(item: HistoricoItemUi) {
    val isRetirou = item.tipo == TipoMovimento.RETIROU

    val icone = if (isRetirou) "🔧" else "✓"
    val corFundoIcone = if (isRetirou) FundoAzulRetirou else TapLightGreen.copy(alpha = 0.2f)
    val corTextoStatus = if (isRetirou) AzulRetirou else TapBrandDark
    val textoStatus = if (isRetirou) "Retirou" else "Devolveu"

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
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(corFundoIcone),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icone,
                    fontSize = 18.sp,
                    color = if (isRetirou) Color.Unspecified else TapBrandDark,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = item.nomeFerramenta,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextTitle,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = textoStatus,
                color = corTextoStatus,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHistorico() {
    AppTheme {
        HistoricoScreenContent(
            state = HistoricoUiState(
                isLoading = false,
                error = null,
                secoes = listOf(
                    SecaoHistoricoUi(
                        data = "HOJE",
                        movimentos = listOf(
                            HistoricoItemUi(1, "Chave de Caixa 10mm", TipoMovimento.RETIROU),
                            HistoricoItemUi(2, "Multímetro Digital", TipoMovimento.RETIROU)
                        )
                    ),
                    SecaoHistoricoUi(
                        data = "ONTEM",
                        movimentos = listOf(
                            HistoricoItemUi(3, "Pistola de Calor", TipoMovimento.DEVOLVEU),
                            HistoricoItemUi(4, "Chave de Fendas", TipoMovimento.DEVOLVEU)
                        )
                    ),
                    SecaoHistoricoUi(
                        data = "15 MAIO",
                        movimentos = listOf(
                            HistoricoItemUi(5, "Chave de Fendas", TipoMovimento.DEVOLVEU),
                            HistoricoItemUi(6, "Chave Spline", TipoMovimento.DEVOLVEU),
                            HistoricoItemUi(7, "Martelo", TipoMovimento.DEVOLVEU)
                        )
                    )
                )
            ),
            onMenuClick = {}
        )
    }
}