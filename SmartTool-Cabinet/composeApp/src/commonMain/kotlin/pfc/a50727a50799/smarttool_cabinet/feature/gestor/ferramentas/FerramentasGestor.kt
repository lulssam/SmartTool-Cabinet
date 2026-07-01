package pfc.a50727a50799.smarttool_cabinet.feature.gestor.ferramentas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.ui.BarraPesquisa
import pfc.a50727a50799.smarttool_cabinet.ui.FilterChip
import pfc.a50727a50799.smarttool_cabinet.ui.TopBar
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AzulReservou
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AzulRetirou
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapAlert
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapRedText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextTitle
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.tool

/**
 * Parte visual do ecrã Ferramentas.
 *
 * Não sabe nada sobre a lógica da aplicação — apenas mostra o que recebe
 * e avisa quando o utilizador faz algo. Fácil de testar e de pré-visualizar.
 *
 * @param state Tudo o que o ecrã precisa para se mostrar corretamente.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FerramentasGestorScreenContent(
    state: FerramentasUiState,
    onMenuClick: () -> Unit = {},
    onSearchChange: (String) -> Unit = {},
    onFiltroChange: (FiltroFerramenta) -> Unit = {},
    onAdicionarClick: () -> Unit = {}
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
            Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
                TopBar(
                    titulo = "Ferramentas",
                    alertasAtivos = state.alertasAtivos,
                    mostrarAlertas = true,
                    onMenu = onMenuClick
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // titulo + subtitulo + botão
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Ferramentas",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Inventário e rastreabilidade",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }

                            Button(
                                onClick = onAdicionarClick,
                                colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "+ Adicionar",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    // barra de pesquisa
                    item {
                        BarraPesquisa(
                            label = "Pesquisar por nome ou código...",
                            query = state.searchQuery,
                            onQueryChange = onSearchChange
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(2.dp))
                    }

                    // chips de filtro
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FiltroFerramenta.entries.forEach { filtro ->
                                FilterChip(
                                    label = filtro.label,
                                    isSelected = state.filtroAtual == filtro,
                                    onClick = { onFiltroChange(filtro) }
                                )
                            }
                        }
                    }

                    // lista agrupada por categoria
                    if (state.seccoes.isEmpty()) {
                        item {
                            Text(
                                text = "Sem ferramentas para mostrar",
                                fontSize = 14.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    } else {
                        state.seccoes.forEach { seccao ->
                            stickyHeader(
                                key = "header_${seccao.categoria}"
                            ) {
                                CategoriaHeader(seccao.categoria, seccao.ferramentas.size)
                            }
                            items(seccao.ferramentas, key = { it.idFerramenta }) { ferramenta ->
                                FerramentaGestorCard(ferramenta)
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * Um card de ferramenta. A cor e o texto da pill (e a cor do nome do detentor)
 * vêm todos da disponibilidade da ferramenta.
 */

@Composable
private fun FerramentaGestorCard(ferramenta: FerramentaUi) {
    val (corFundo, corTexto, label) = when (ferramenta.disponibilidade) {
        DisponibilidadeFerramenta.DISPONIVEL ->
            Triple(TapLightGreen.copy(alpha = 0.2f), TapBrandDark, "Disponível")

        DisponibilidadeFerramenta.REQUISITADA ->
            Triple(AlertOrange.copy(alpha = 0.2f), AlertOrangeText, "Em Uso")

        DisponibilidadeFerramenta.EM_FALTA ->
            Triple(TapAlert.copy(alpha = 0.2f), TapRedText, "Em Falta")

        DisponibilidadeFerramenta.EM_MANUTENCAO ->
            Triple(FieldBg, TextSecondary, "Manutenção")

        DisponibilidadeFerramenta.RESERVADA ->
            Triple(AzulReservou.copy(alpha = 0.2f), AzulReservou, "Reservada")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ícone
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(FieldBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.tool),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            // nome + detalhe (+ detentor, se houver)
            Column(Modifier.weight(1f)) {
                Text(
                    text = ferramenta.nome,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "${ferramenta.codigo} · ${ferramenta.localizacao}",
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    color = TextSecondary
                )
                ferramenta.funcionario?.let { nome ->
                    Text(
                        text = nome,
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        color = corTexto,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            // pill de estado
            Text(
                text = label,
                color = corTexto,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(PillShape)
                    .background(corFundo)
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            )
        }
    }
}

/**
 * O cabeçalho fixo de cada categoria. Tem fundo igual ao do ecrã para "tapar"
 * os cards que passam por baixo enquanto ele fica colado no topo.
 */
@Composable
private fun CategoriaHeader(categoria: String, total: Int) {
    Text(
        text = "$categoria ($total)",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = TextTitle,
        modifier = Modifier
            .fillMaxWidth()
            .background(ScreenBg)
            .padding(vertical = 6.dp)
    )
}

/**
 * Liga o [FerramentasGestorViewModel] ao [FerramentasGestorScreenContent].
 *
 * Observa o estado do ViewModel e passa-o para o ecrã.
 * Não contém lógica de UI — apenas faz a ligação.
 *
 * @param viewModel O ViewModel que gere o estado deste ecrã.
 *                  É criado automaticamente pelo Compose se não for fornecido.
 */
@Composable
fun FerramentasGestorScreen(
    viewModel: FerramentasGestorViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    FerramentasGestorScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange,
        onAdicionarClick = {} // todo: adicionar no viewmodel esta função
    )
}

/**
 * Pré-visualização do ecrã Ferramentas para usar durante o desenvolvimento.
 *
 * Usa dados fictícios para simular como o ecrã ficará com informação real.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        FerramentasGestorScreenContent(
            state = FerramentasUiState(
                alertasAtivos = 4,
                seccoes = listOf(
                    SecaoFerramentas(
                        categoria = "Chaves",
                        ferramentas = listOf(
                            FerramentaUi(
                                idFerramenta = 100001, codigo = "0001-00001",
                                nome = "Chave de Caixa 10mm", categoria = "Chaves",
                                localizacao = "Arm. 1",
                                disponibilidade = DisponibilidadeFerramenta.DISPONIVEL,
                                estado = EstadoFerramenta.OPERACIONAL
                            ),
                            FerramentaUi(
                                idFerramenta = 100004, codigo = "0001-00004",
                                nome = "Chave de Parafusos", categoria = "Chaves",
                                localizacao = "Arm. 1",
                                disponibilidade = DisponibilidadeFerramenta.EM_FALTA,
                                estado = EstadoFerramenta.OPERACIONAL,
                                funcionario = "Carlos Gonçalves"
                            )
                        )
                    ),
                    SecaoFerramentas(
                        categoria = "Alicates",
                        ferramentas = listOf(
                            FerramentaUi(
                                idFerramenta = 200002, codigo = "0002-00002",
                                nome = "Alicate de Bico", categoria = "Alicates",
                                localizacao = "Arm. 2",
                                disponibilidade = DisponibilidadeFerramenta.REQUISITADA,
                                estado = EstadoFerramenta.OPERACIONAL,
                                funcionario = "Luísa Sampaio"
                            )
                        )
                    )
                )
            ),
            onMenuClick = {},
            onSearchChange = {},
            onFiltroChange = {},
            onAdicionarClick = {}
        )
    }
}