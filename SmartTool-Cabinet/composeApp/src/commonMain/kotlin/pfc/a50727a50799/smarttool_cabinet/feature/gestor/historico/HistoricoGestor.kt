package pfc.a50727a50799.smarttool_cabinet.feature.gestor.historico

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
import androidx.compose.runtime.key
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

/**
 * Parte visual do ecrã HistoricoGestor.
 *
 * Não sabe nada sobre a lógica da aplicação — apenas mostra o que recebe
 * e avisa quando o utilizador faz algo. Fácil de testar e de pré-visualizar.
 *
 * @param state Tudo o que o ecrã precisa para se mostrar corretamente.
 */
@Composable
private fun HistoricoGestorScreenContent(
    state: HistoricoGestorUiState,
    onMenuClick: () -> Unit,
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

        else -> {
            Column(
                modifier = Modifier.fillMaxSize().background(ScreenBg)
            ) {
                TopBar(
                    titulo = "Histórico",
                    mostrarAlertas = true,
                    alertasAtivos = state.alertasAtivos,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // titulo
                    item {
                        Column {
                            Text(
                                "Histórico",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                "Últimos 7 dias de movimentos",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }

                    }
                    if (state.secoes.isEmpty()) {
                        item {
                            Text(
                                text = "Sem movimentos nos últimos 7 dias.",
                                fontSize = 14.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    } else {
                        state.secoes.forEach { secao ->

                            // etiqueta da data
                            item {
                                Text(
                                    text = secao.data,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            items(secao.movimentos, key = { it.id }) { movimento ->
                                HistoricoItemCardGestor(movimento)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoricoItemCardGestor(item: HistoricoItemUi) {
    // estilo por tipo: ícone, cor da ação, fundo do ícone, texto
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
            Estilo(
                Res.drawable.alert_triangle,
                AlertOrangeText,
                AlertOrange.copy(alpha = 0.2f),
                "Marcou avaria"
            )
    }

    MovimentoCard(
        icone = e.icone,
        corAcao = e.cor,
        corFundoIcone = e.fundo,
        nome = item.nomeFerramenta,
        label = e.label,
        subtitulo = item.funcionario,
        hora = item.hora
    )
}

/**
 * Liga o [HistoricoGestorViewModel] ao [HistoricoGestorScreenContent].
 *
 * Observa o estado do ViewModel e passa-o para o ecrã.
 * Não contém lógica de UI — apenas faz a ligação.
 *
 * @param viewModel O ViewModel que gere o estado deste ecrã.
 *                  É criado automaticamente pelo Compose se não for fornecido.
 */
@Composable
fun HistoricoGestorScreen(
    viewModel: HistoricoGestorViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    HistoricoGestorScreenContent(
        state = state,
        onMenuClick = onMenuClick
    )
}

/**
 * Pré-visualização do ecrã HistoricoGestor para usar durante o desenvolvimento.
 *
 * Usa dados fictícios para simular como o ecrã ficará com informação real.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoricoGestorPreview() {
    AppTheme {
        HistoricoGestorScreenContent(
            state = HistoricoGestorUiState(
                alertasAtivos = 4,
                secoes = listOf(
                    SecaoHistoricoUi(
                        "HOJE", listOf(
                            HistoricoItemUi(
                                1,
                                "Chave de Caixa 10mm",
                                "Tiago Dias",
                                "09:45",
                                TipoMovimento.RETIROU
                            ),
                            HistoricoItemUi(
                                2,
                                "Alicate de Bico",
                                "Pedro Fazenda",
                                "10:43",
                                TipoMovimento.DEVOLVEU
                            )
                        )
                    ),
                    SecaoHistoricoUi(
                        "ONTEM", listOf(
                            HistoricoItemUi(
                                3,
                                "Torquímetro 60Nm",
                                "Miguel Azenha",
                                "09:32",
                                TipoMovimento.MARCOU_AVARIA
                            )
                        )
                    )
                )
            ),
            onMenuClick = {}
        )
    }
}