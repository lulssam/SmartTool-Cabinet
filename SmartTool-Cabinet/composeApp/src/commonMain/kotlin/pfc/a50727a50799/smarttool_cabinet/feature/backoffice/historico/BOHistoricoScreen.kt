package pfc.a50727a50799.smarttool_cabinet.feature.backoffice.historico

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.DrawableResource
import pfc.a50727a50799.smarttool_cabinet.ui.MovimentoCard
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
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
 * Mostra o conteúdo do ecrã de histórico do BackOffice.
 *
 * Dependendo do estado atual, apresenta um indicador de carregamento,
 * uma mensagem de erro ou a lista dos movimentos organizados por data.
 *
 * @param state Informação necessária para mostrar o estado atual do ecrã.
 * @param onMenuClick Função chamada quando o utilizador abre o menu lateral.
 */
@Composable
private fun BOHistoricoScreenContent(
    state: BOHistoricoUiState,
    onMenuClick: () -> Unit,
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
                    titulo = "Histórico",
                    mostrarAlertas = false,
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
                                "Histórico",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                "Últimos 30 dias de movimentos",
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
                                BOHistoricoItemCard(movimento)
                            }
                        }
                    }
                }
            }
        }
    }
}
/**
 * Mostra um cartão com a informação de um movimento do histórico.
 *
 * O aspeto do cartão muda automaticamente consoante o tipo de movimento,
 * mostrando o ícone, as cores e o texto mais adequados.
 *
 * @param item Movimento que será apresentado no cartão.
 */
@Composable
private fun BOHistoricoItemCard(item: BOHistoricoItemUi) {
    /**
     * Guarda toda a informação visual utilizada para representar
     * um determinado tipo de movimento.
     *
     * @property icone Ícone apresentado no cartão.
     * @property cor Cor utilizada no ícone e na ação.
     * @property fundo Cor de fundo do ícone.
     * @property label Texto que identifica o tipo de movimento.
     */
    data class Estilo(
        val icone: DrawableResource,
        val cor: Color,
        val fundo: Color,
        val label: String
    )

    val e = when (item.tipo) {
        TipoMovimentoBO.RETIROU ->
            Estilo(Res.drawable.tool, AzulRetirou, FundoAzulRetirou, "Retirou")

        TipoMovimentoBO.DEVOLVEU ->
            Estilo(Res.drawable.check, TapBrandDark, TapLightGreen.copy(alpha = 0.2f), "Devolveu")

        TipoMovimentoBO.MARCOU_AVARIA ->
            Estilo(Res.drawable.alert_triangle, AlertOrangeText, AlertOrange.copy(alpha = 0.2f), "Marcou avaria")
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
 * Ecrã principal do histórico do BackOffice.
 *
 * Obtém o estado atual através do ViewModel e entrega essa informação
 * ao conteúdo visual do ecrã.
 *
 * @param viewModel ViewModel responsável por fornecer os dados do histórico.
 * @param onMenuClick Função chamada quando o utilizador abre o menu lateral.
 */
@Composable
fun BOHistoricoScreen(
    viewModel: BOHistoricoViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    BOHistoricoScreenContent(
        state = state,
        onMenuClick = onMenuClick
    )
}