package pfc.a50727a50799.smarttool_cabinet.feature.tecnico.ferramentas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ScreenBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextTitle
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.tool
/**
 * Esta é a parte visual do ecrã de ferramentas.
 * Não sabe nada sobre a lógica da aplicação — apenas mostra o que recebe e avisa-nos
 * quando o utilizador faz alguma coisa, como pesquisar ou tocar num botão.
 *
 * @param state Toda a informação atual que o ecrã precisa para se desenhar (lista de ferramentas, se está a carregar, erros, etc.).
 * @param onMenuClick Chamado quando o utilizador toca no botão do menu principal.
 * @param onSearchChange Chamado sempre que o utilizador escreve uma letra na barra de pesquisa.
 * @param onFiltroChange Chamado quando o utilizador escolhe uma categoria diferente para filtrar as ferramentas.
 * @param onTemplateClick Chamado quando o utilizador toca num conjunto de ferramentas (template) para ver ou esconder os detalhes.
 * @param onDevolverClick Chamado quando o utilizador toca no botão para devolver uma ferramenta.
 * @param onMauEstadoClick Chamado quando o utilizador avisa que uma ferramenta está estragada.
 * @param onRequisitarClick Chamado quando o utilizador pede para começar a usar uma ferramenta.
 * @param onClearError Chamado quando o utilizador fecha a janela de erro.
 */
@Composable
private fun FerramentasScreenContent(
    state: FerramentasUiState,
    onMenuClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    onFiltroChange: (FiltroFerramenta) -> Unit,
    onTemplateClick: (Int) -> Unit,
    onDevolverClick: (Int) -> Unit,
    onMauEstadoClick: (Int, Int) -> Unit,
    onRequisitarClick: (Int, Int) -> Unit,
    onClearError: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize().background(ScreenBg)) {
            TopBar(
                titulo = "Ferramentas",
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
                    Column {
                        Text(
                            text = "Ferramentas",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextTitle
                        )
                        Text(
                            text = "Consulte e gira as suas ferramentas",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }

                item {
                    Text(
                        text = "Templates diários",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextTitle,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(state.templates) { template ->
                    TemplateCard(template = template, onClick = { onTemplateClick(template.id) })
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    BarraPesquisa(
                        label = "Pesquisar ferramentas...",
                        query = state.searchQuery,
                        onQueryChange = onSearchChange
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FiltroFerramenta.entries.forEach { filtro ->
                            FilterChip(
                                label = filtro.label,
                                isSelected = state.filtroAtual == filtro,
                                onClick = { onFiltroChange(filtro) })
                        }
                    }
                }

                items(state.ferramentas) { ferramenta ->
                    FerramentaItemCard(
                        ferramenta = ferramenta,
                        onDevolverClick = {
                            ferramenta.idRequisicao?.let { idReq ->
                                onDevolverClick(
                                    idReq
                                )
                            }
                        },
                        onMauEstadoClick = {
                            ferramenta.idRequisicao?.let { idReq ->
                                onMauEstadoClick(
                                    ferramenta.id,
                                    idReq
                                )
                            }
                        },
                        onRequisitarClick = {
                            onRequisitarClick(
                                ferramenta.codigoTipo,
                                ferramenta.nFerramenta
                            )
                        }
                    )
                }
            }
        }

        if (state.error != null) {
            AlertDialog(
                onDismissRequest = onClearError,
                confirmButton = {
                    TextButton(onClick = onClearError) {
                        Text("OK", color = TapBrandDark, fontWeight = FontWeight.Bold)
                    }
                },
                title = {
                    Text("Aviso", fontWeight = FontWeight.Bold, color = TextTitle)
                },
                text = {
                    Text(
                        text = state.error ?: "",
                        color = TextSecondary,
                        fontSize = 15.sp
                    )
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = TapBrandDark)
            }
        }
    }
}
/**
* Liga o ecrã visual das ferramentas à lógica da aplicação (ViewModel).
* Mantém o ecrã sempre atualizado com as últimas informações e passa as ações do utilizador para o gestor.
*
* @param viewModel O gestor que guarda as ferramentas, pesquisa a informação e comunica com o servidor.
* @param onMenuClick Chamado quando o utilizador quer abrir o menu lateral da aplicação.
*/
@Composable
fun FerramentasScreen(
    viewModel: FerramentasViewModel = viewModel(),
    onMenuClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()

    FerramentasScreenContent(
        state = state,
        onMenuClick = onMenuClick,
        onSearchChange = viewModel::onSearchChange,
        onFiltroChange = viewModel::onFiltroChange,
        onTemplateClick = viewModel::toggleTemplate,
        onDevolverClick = viewModel::devolver,
        onMauEstadoClick = viewModel::marcarMauEstado,
        onRequisitarClick = { codigoTipo, nFerramenta ->
            viewModel.requisitarFerramenta(
                codigoTipo,
                nFerramenta
            )
        },
        onClearError = viewModel::limparErro
    )
}
/**
 * O cartão visual que mostra os detalhes de uma única ferramenta na lista principal.
 * Ele muda a sua aparência para mostrar se a ferramenta está livre, ocupada ou estragada,
 * e mostra apenas os botões que fazem sentido naquele momento.
 *
 * @param ferramenta Toda a informação da ferramenta que queremos mostrar (nome, estado, etc.).
 * @param onDevolverClick Chamado quando o utilizador toca no botão de "Devolver".
 * @param onMauEstadoClick Chamado quando o utilizador toca no botão para reportar que a ferramenta tem defeitos.
 * @param onRequisitarClick Chamado quando o utilizador toca no botão para pedir a ferramenta.
 */
@Composable
fun FerramentaItemCard(
    ferramenta: FerramentaListaUi,
    onDevolverClick: () -> Unit,
    onMauEstadoClick: () -> Unit,
    onRequisitarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(42.dp).clip(RoundedCornerShape(8.dp))
                        .background(FieldBg), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.tool),
                        contentDescription = null,
                        tint = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ferramenta.nome,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextTitle
                    )
                    Text(text = ferramenta.detalhes, fontSize = 11.sp, color = TextSecondary)
                }

                val (bgColor, textColor, label) = when (ferramenta.estado) {
                    EstadoFerramentaLista.DISPONIVEL -> Triple(
                        TapLightGreen.copy(alpha = 0.2f),
                        TapBrandDark,
                        "Disponível"
                    )

                    EstadoFerramentaLista.EM_USO -> Triple(
                        AlertOrange.copy(alpha = 0.15f),
                        AlertOrangeText,
                        "Em Uso"
                    )

                    EstadoFerramentaLista.MANUTENCAO -> Triple(FieldBg, TextSecondary, "Manutenção")

                    EstadoFerramentaLista.RESERVADA -> Triple(
                        AzulReservou.copy(alpha = 0.2f),
                        AzulReservou,
                        "Reservada"
                    )
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(bgColor)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        color = textColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (ferramenta.showDevolverButtons && ferramenta.idRequisicao != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onDevolverClick,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = TapLightGreen.copy(
                                alpha = 0.15f
                            )
                        ),
                        border = BorderStroke(1.dp, TapBrandGreen)
                    ) { Text("Devolver", color = TapBrandDark, fontWeight = FontWeight.Bold) }

                    OutlinedButton(
                        onClick = onMauEstadoClick,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = AlertOrange.copy(
                                alpha = 0.1f
                            )
                        ),
                        border = BorderStroke(1.dp, AlertOrange)
                    ) { Text("Mau estado", color = AlertOrangeText, fontWeight = FontWeight.Bold) }
                }
            }

            if (ferramenta.showRequisitarButton) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRequisitarClick,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TapBrandDark)
                ) {
                    Text(
                        "Requisitar ferramenta",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
/**
 * Mostra um conjunto pré-definido de ferramentas que costumam ser usadas juntas (template).
 * O utilizador pode clicar neste cartão para expandir a lista e ver exatamente que
 * ferramentas estão incluídas neste pacote antes de as pedir todas de uma vez.
 *
 * @param template A informação deste conjunto (como o nome e a lista de ferramentas que inclui).
 * @param onClick Chamado quando o utilizador toca em qualquer parte do cartão para o abrir ou fechar.
 */
@Composable
fun TemplateCard(template: TemplateDiarioUi, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp))
                        .background(TapLightGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) { Text("🔧", fontSize = 16.sp) }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = template.nome,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextTitle
                    )
                    Text(
                        text = "${template.totalFerramentas} ferramentas",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Text(
                    text = if (template.isExpanded) "▲" else "▼",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            AnimatedVisibility(visible = template.isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    template.ferramentas.forEach { nomeFerramenta ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
                                .background(FieldBg).padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.tool),
                                contentDescription = null,
                                tint = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = nomeFerramenta,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextTitle
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TapBrandDark),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Requisitar todas ( ${template.totalFerramentas} )",
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview do ecrã para visualização no Android Studio.
 */
@Preview(showBackground = true)
@Composable
private fun PreviewFerramentas() {
    AppTheme {
        FerramentasScreenContent(
            state = FerramentasUiState(
                isLoading = false,
                error = null,
                templates = emptyList(),
                ferramentas = emptyList()
            ),
            onMenuClick = {},
            onSearchChange = {},
            onFiltroChange = {},
            onTemplateClick = {},
            onDevolverClick = {},
            onMauEstadoClick = { _, _ -> },
            onRequisitarClick = { _, _ -> },
            onClearError = {}
        )
    }
}