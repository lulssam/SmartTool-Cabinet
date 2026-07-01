package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.Historico

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.DrawableResource
import pfc.a50727a50799.smarttool_cabinet.ui.MovimentoCard
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AzulRetirou
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FundoAzulRetirou
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.alert_triangle
import smarttoolcabinet.composeapp.generated.resources.check
import smarttoolcabinet.composeapp.generated.resources.tool

@Composable
private fun HistoricoScreenContent(
    state: HistoricoUiState,
    onMenuClick: () -> Unit
) {
    when {
        state.isLoading ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TapBrandDark)
            }

        state.error != null ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error, color = MaterialTheme.colorScheme.error)
            }

        else -> {
            Column(
                modifier = Modifier.fillMaxSize().background(ScreenBg)
            ) {
                TopBar(
                    titulo = "O Meu Histórico",
                    mostrarAlertas = false,
                    alertasAtivos = 0,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Column {
                            Text(
                                "O Meu Histórico",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                "Últimos movimentos efetuados",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    if (state.grupos.isEmpty()) {
                        item {
                            Text(
                                text = "Sem movimentos recentes.",
                                fontSize = 14.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    } else {
                        state.grupos.forEach { grupo ->
                            item {
                                Text(
                                    text = grupo.data,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            items(grupo.movimentos, key = { it.id }) { movimento ->
                                HistoricoItemCardTecnico(movimento)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoricoItemCardTecnico(item: HistoricoItemUi) {
    data class Estilo(
        val icone: DrawableResource,
        val cor: Color,
        val fundo: Color,
        val label: String
    )

    val e = when (item.tipo) {
        TipoMovimento.RETIROU ->
            Estilo(Res.drawable.tool, AzulRetirou, FundoAzulRetirou, "Retirou")
        TipoMovimento.DEVOLVEU ->
            Estilo(Res.drawable.check, TapBrandDark, TapLightGreen.copy(alpha = 0.2f), "Devolveu")
        TipoMovimento.MARCOU_AVARIA ->
            Estilo(Res.drawable.alert_triangle, AlertOrangeText, AlertOrange.copy(alpha = 0.2f), "Marcou avaria")
    }

    MovimentoCard(
        icone = e.icone,
        corAcao = e.cor,
        corFundoIcone = e.fundo,
        nome = item.nomeFerramenta,
        label = e.label,
        subtitulo = item.detalhe,
        hora = item.hora
    )
}

@Composable
fun HistoricoScreen(
    viewModel: HistoricoViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    HistoricoScreenContent(
        state = state,
        onMenuClick = onMenuClick
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewHistorico() {
    AppTheme {
        HistoricoScreenContent(
            state = HistoricoUiState(
                isLoading = false,
                grupos = listOf(
                    GrupoHistoricoUi(
                        data = "HOJE",
                        movimentos = listOf(
                            HistoricoItemUi("1", "Chave de Caixa 10mm", "Manutenção Motor", "09:45", TipoMovimento.RETIROU),
                            HistoricoItemUi("2", "Multímetro Digital", "Manutenção Motor", "10:43", TipoMovimento.DEVOLVEU)
                        )
                    ),
                    GrupoHistoricoUi(
                        data = "ONTEM",
                        movimentos = listOf(
                            HistoricoItemUi("3", "Pistola de Calor", "Reparação Cabine", "09:32", TipoMovimento.MARCOU_AVARIA)
                        )
                    )
                )
            ),
            onMenuClick = {}
        )
    }
}