package pfc.a50727a50799.smarttool_cabinet.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.ArmarioUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.EstadoArmario
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.EstatisticasFerramentas
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.Gravidade
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.EstadoTarefa
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.PrioridadeTarefa
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AzulRetirou
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FundoAzulRetirou
import pfc.a50727a50799.smarttool_cabinet.ui.theme.InfoBoxBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.SearchFieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.SearchFieldText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapAlert
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapRedText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapSurfaceGrey
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ToolInUse
import pfc.a50727a50799.smarttool_cabinet.ui.theme.ToolMaintenance
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.alert_triangle
import smarttoolcabinet.composeapp.generated.resources.arrowleft
import smarttoolcabinet.composeapp.generated.resources.lock
import smarttoolcabinet.composeapp.generated.resources.menu
import smarttoolcabinet.composeapp.generated.resources.search
import smarttoolcabinet.composeapp.generated.resources.tool
import smarttoolcabinet.composeapp.generated.resources.unlock
import kotlin.math.roundToInt

//#my_code
/**
 * Barra que aparece no topo do ecrã.
 * Mostra um botão de menu à esquerda, o título do ecrã no meio e, se quisermos,
 * uma etiqueta à direita com o número de alertas por resolver.
 *
 * @param titulo O texto que aparece no meio da barra (ex: "Dashboard").
 * @param alertasAtivos Quantos alertas estão neste momento por resolver. Se for
 *                      0, a etiqueta diz que não há nenhum.
 * @param mostrarAlertas Se for true, mostra a etiqueta dos alertas à direita; se
 *                       for false, esconde-a por completo.
 * @param onMenu Chamada quando o utilizador carrega no botão de menu.
 */
@Composable
fun TopBar(
    titulo: String,
    alertasAtivos: Int = 0,
    mostrarAlertas: Boolean = true,
    onMenu: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                )
                .heightIn(min = 80.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onMenu) {
                Icon(painter = painterResource(Res.drawable.menu), contentDescription = "Menu")
            }

            Text(
                text = titulo,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )

            if (mostrarAlertas) {
                val contagemAlertas by animateIntAsState(
                    alertasAtivos,
                    tween(600),
                    label = "alertas"
                )
                Row(
                    modifier = Modifier
                        .clip(PillShape)
                        .background(TapAlert.copy(alpha = 0.15f))
                        .padding(horizontal = 15.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.alert_triangle),
                        contentDescription = "Alertas",
                        tint = TapAlert
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            alertasAtivos > 1 -> "$contagemAlertas alertas ativo"
                            alertasAtivos == 1 -> "$contagemAlertas alerta ativo"
                            else -> "Nenhum alerta ativo"
                        },
                        color = TapAlert,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Cartão de boas-vindas que aparece no topo do ecrã principal.
 * Cumprimenta o funcionário pelo nome e mostra o cargo e o turno em que está.
 *
 * @param nomeFuncionario O nome do funcionário que iniciou sessão.
 * @param turno O turno em que o funcionário está (ex: "Manhã", "Tarde").
 * @param cargo A função do funcionário (ex: "Técnico"). Se estiver vazio, essa
 *              parte não aparece.
 * @param color A cor de fundo do cartão.
 */
@Composable
fun WelcomeCard(
    nomeFuncionario: String,
    turno: String,
    cargo: String = "",
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 110.dp)
            .clip(CardShape)
            .background(color)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Bem-vindo de volta,", color = TapSurfaceGrey, fontSize = 13.sp)
        Text(
            text = nomeFuncionario,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "$cargo - Turno: $turno", color = Color.White, fontSize = 13.sp)
    }
}

//#my_code end
/**
 * Uma fatia do gráfico circular (donut) do estado das ferramentas.
 * Cada fatia junta um nome, um número e a cor com que vai ser desenhada.
 *
 * @property label O nome da fatia (ex: "Disponíveis", "Em Uso").
 * @property valor Quantas ferramentas estão neste estado.
 * @property cor A cor com que esta fatia é pintada no gráfico.
 */
private data class Segmento(
    val label: String,
    val valor: Int,
    val cor: Color
)

/**
 * Cartão que mostra, num gráfico circular (donut) com legenda ao lado, quantas
 * ferramentas estão disponíveis, em uso, indisponíveis ou em manutenção.
 *
 * @param estatisticas Os números de ferramentas em cada estado, usados para
 *                     desenhar as fatias do gráfico e a legenda.
 */
@Composable
fun EstadoFerramentasCard(
    estatisticas: EstatisticasFerramentas
) {
    val segmentos = listOf(
        Segmento("Disponíveis", estatisticas.disponiveis, TapBrandDark),
        Segmento("Em Uso", estatisticas.requisitada, ToolInUse),
        Segmento("Indisponivel", estatisticas.indisponivel, TapAlert),
        Segmento("Manutenção", estatisticas.manutencao, ToolMaintenance)
    )

    val total = estatisticas.total

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Disponibilidade das Ferramentas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            BoxWithConstraints {
                val donutSize = (maxWidth * 0.38f).coerceIn(110.dp, 150.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Donut(
                        segmentos = segmentos,
                        total = total,
                        tamanho = donutSize
                    )

                    Spacer(Modifier.width(16.dp))

                    // legenda
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        segmentos.forEach { LegendaRow(it, total) }
                    }
                }
            }
        }

    }
}

//#my_code
/**
 * Uma linha da legenda que aparece ao lado do gráfico circular.
 * Mostra um quadradinho colorido, o nome da fatia, o número de ferramentas e a
 * percentagem que essa fatia representa do total.
 *
 * @param segmento A fatia do gráfico que esta linha descreve (nome, valor e cor).
 * @param total O número total de ferramentas, usado para calcular a percentagem.
 */
@Composable
private fun LegendaRow(
    segmento: Segmento,
    total: Int
) {
    val percent =
        if (total > 0) segmento.valor * 100 / total
        else 0
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(15.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(segmento.cor)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = segmento.label,
            fontSize = 11.sp,
            color = Color.Black,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${segmento.valor}",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        Spacer(Modifier.width(8.dp))
        Text("$percent%", fontSize = 11.sp, color = TextSecondary)
    }
}

//#my_code end
/**
 * Desenha o gráfico circular (o aro colorido) com o total no meio.
 * Cada fatia ocupa uma parte do aro conforme o seu valor. Quando o ecrã abre, o
 * aro cresce com uma pequena animação; se ainda não houver dados, mostra só um
 * aro cinzento vazio.
 *
 * @param segmentos As fatias a desenhar, cada uma com o seu valor e cor.
 * @param total A soma de todas as fatias. É o número que aparece no centro.
 * @param tamanho A largura e altura do gráfico.
 */
@Composable
private fun Donut(
    segmentos: List<Segmento>,
    total: Int,
    tamanho: Dp = 150.dp
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(tamanho)) {

        val emPreview = LocalInspectionMode.current
        val progress = remember { Animatable(if (emPreview) 1f else 0f) }
        LaunchedEffect(total > 0) {
            if (!emPreview && total > 0) {
                progress.snapTo(0f)
                progress.animateTo(1f, tween(900, easing = FastOutSlowInEasing))
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val p = progress.value
            val stroke = size.minDimension * 0.18f               // espessura do aro
            val arcSize = Size(size.width - stroke, size.height - stroke)
            val topLeft = Offset(stroke / 2, stroke / 2)   // encolhe para o aro não sair do canvas

            if (total == 0) {
                // a app arranca com (0,0,0,0) → desenha só um aro cinza vazio
                drawArc(
                    color = ToolMaintenance.copy(alpha = 0.3f),
                    startAngle = 0f, sweepAngle = 360f, useCenter = false,
                    topLeft = topLeft, size = arcSize, style = Stroke(width = stroke)
                )
            } else {
                var startAngle = -90f                  // começa no topo
                segmentos.forEach { seg ->
                    val sweep = 360f * seg.valor / total * p
                    drawArc(
                        color = seg.cor,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,             // aro, não pizza
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = stroke)
                    )
                    startAngle += sweep                // próxima fatia começa aqui
                }
            }
        }

        // texto central, sobreposto ao Canvas (estão os dois dentro do Box)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val totalAnimado = (total * progress.value).roundToInt()
            Text(
                "$totalAnimado",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text("total", fontSize = 11.sp, color = TextSecondary)
        }
    }
}

//#my_code
/**
 * Cabeçalho de uma secção do ecrã.
 * Mostra o título à esquerda e um atalho "Ver todos" à direita que leva o
 * utilizador à lista completa dessa secção.
 *
 * @param titulo O título da secção (ex: "Estado dos Armários").
 * @param onVerTodos Chamada quando o utilizador carrega em "Ver todos".
 */
@Composable
fun SectionHeader(
    titulo: String,
    onVerTodos: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Text(
            text = titulo,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier.clickable(onClick = onVerTodos),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Ver todos",
                color = TapBrandDark,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                painter = painterResource(Res.drawable.arrowleft),
                contentDescription = null,
                tint = TapBrandDark,
                modifier = Modifier.graphicsLayer(scaleX = -1f)
            )

        }

    }
}

//#my_code end
/**
 * Cartão que mostra o estado de um armário de ferramentas.
 * Apresenta o nome do armário, uma etiqueta com o estado (online, alerta ou
 * offline) e três caixas com: quantos lugares estão ocupados, se está trancado
 * e quantas ferramentas estão em falta.
 *
 * @param armario Os dados do armário a mostrar (nome, estado, lugares, etc.).
 */
@Composable
fun ArmarioCard(
    armario: ArmarioUi
) {

    val corBorda = when (armario.estadoArmario) {
        EstadoArmario.OPERACIONAL -> TapBrandDark.copy(alpha = 0.25f)
        EstadoArmario.ALERTA -> AlertOrange.copy(alpha = 0.25f)
        EstadoArmario.AVARIADO -> TextSecondary.copy(alpha = 0.25f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, corBorda)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // nome do armario + estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = armario.nome,
                    fontSize = 15.sp,
                    color = Color.Black,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                StatusPill(estado = armario.estadoArmario)

            }

            // as 3 caixas
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // slots
                InfoBox {
                    Text(
                        text = "${armario.slotsOcupados}/${armario.slotsTotal}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TapBrandDark
                    )

                    Text(
                        text = "Slots",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }

                // trancado?
                InfoBox {
                    Icon(
                        painter = painterResource(
                            if (armario.trancado) Res.drawable.lock
                            else Res.drawable.unlock
                        ),
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        text = if (armario.trancado) "Trancado" else "Aberto",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }

                // em falta
                InfoBox {
                    val cor = if (armario.emFalta > 0) TapAlert else TapBrandDark
                    Text(
                        text = "${armario.emFalta}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = cor
                    )

                    Text(
                        text = "Em Falta",
                        fontSize = 10.sp,
                        color = cor
                    )
                }
            }
        }
    }
}

//#my_code
/**
 * Cartão que mostra um alerta ao gestor.
 * A cor da borda, do fundo e do texto mudam conforme a gravidade: vermelho para
 * alertas críticos e laranja para avisos.
 *
 * @param gravidade O grau do alerta (crítico ou aviso), que decide as cores.
 * @param titulo O título curto do alerta (ex: "Ferramenta 1 em falta").
 * @param descricao Uma explicação mais longa do que se passou.
 * @param horas A que horas o alerta aconteceu (ex: "16:32").
 */
@Composable
fun AlertaCard(
    gravidade: Gravidade,
    titulo: String,
    descricao: String,
    horas: String
) {
    val corBorda = when (gravidade) {
        Gravidade.CRITICO -> TapAlert.copy(alpha = 0.25f)
        Gravidade.AVISO -> AlertOrange.copy(alpha = 0.25f)
    }

    val corFundo = when (gravidade) {
        Gravidade.CRITICO -> TapAlert.copy(alpha = 0.1f)
        Gravidade.AVISO -> AlertOrange.copy(alpha = 0.1f)
    }

    val corTexto = when (gravidade) {
        Gravidade.CRITICO -> TapRedText
        Gravidade.AVISO -> AlertOrangeText
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = corFundo),
        border = BorderStroke(1.dp, corBorda)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Icon(
                    painter = painterResource(Res.drawable.alert_triangle),
                    contentDescription = null,
                    tint = corTexto,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = titulo,
                    modifier = Modifier.weight(1f),
                    fontSize = 15.sp,
                    color = corTexto,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = horas,
                    fontSize = 15.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Normal
                )
            }

            Text(
                text = descricao,
                fontSize = 15.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
//#my_code end

/**
 * Campo de pesquisa com uma lupa à esquerda.
 * O texto escrito não fica guardado aqui — é enviado para fora através de
 * onQueryChange, e quem chama esta função decide o que fazer com ele.
 *
 * @param label O texto de ajuda que aparece quando o campo está vazio
 *              (ex: "Pesquisar armários...").
 * @param query O texto que está neste momento escrito no campo.
 * @param onQueryChange Chamada de cada vez que o utilizador escreve ou apaga uma letra.
 */
@Composable
fun BarraPesquisa(
    label: String,
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(label, color = SearchFieldText) },
        leadingIcon = {
            Icon(
                painterResource(Res.drawable.search),
                contentDescription = null,
                tint = SearchFieldText,
                modifier = Modifier.size(20.dp)
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SearchFieldBg,
            unfocusedContainerColor = SearchFieldBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = FieldShape,
        modifier = Modifier.fillMaxWidth()
    )
}

//#my_code
/**
 * Botão em forma de etiqueta (chip) usado para filtrar listas.
 * Quando está selecionado fica com as cores da marca; quando não está, fica
 * apagado. As cores mudam com uma pequena animação ao ligar/desligar.
 *
 * @param label O texto do filtro (ex: "Todos", "Offline").
 * @param isSelected True se este filtro está neste momento escolhido.
 * @param onClick Chamada quando o utilizador carrega no chip.
 */
@Composable
fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor by animateColorAsState(
        if (isSelected) TapLightGreen.copy(alpha = 0.15f) else Color.White,
        label = "chipBg"
    )
    val borderColor by animateColorAsState(
        if (isSelected) TapBrandDark else CardBorder,
        label = "chipBorder"
    )
    val textColor by animateColorAsState(
        if (isSelected) TapBrandDark else TextSecondary,
        label = "chipText"
    )

    Box(
        modifier = Modifier
            .clip(PillShape)
            .background(bgColor)
            .border(1.dp, borderColor, PillShape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 3.dp),
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Etiqueta pequena e arredondada com texto lá dentro.
 * É a base reutilizada pelas outras etiquetas (estado, prioridade), por isso
 * recebe já as cores prontas em vez de as decidir sozinha.
 *
 * @param fundo A cor de fundo da etiqueta.
 * @param texto A cor do texto.
 * @param label O texto a mostrar dentro da etiqueta.
 */
@Composable
fun Pill(fundo: Color, texto: Color, label: String) {
    Text(
        text = label,
        color = texto,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.clip(PillShape).background(fundo)
            .padding(horizontal = 10.dp, vertical = 2.dp)
    )
}

/**
 * Etiqueta que mostra em que ponto está uma tarefa.
 * Traduz o estado da tarefa nas cores e no texto certos e desenha uma [Pill].
 *
 * @param estado O estado da tarefa (em curso, pendente ou concluída).
 */
@Composable
fun PillEstado(estado: EstadoTarefa) {
    val (fundo, texto, label) = when (estado) {
        EstadoTarefa.EM_CURSO -> Triple(TapLightGreen.copy(alpha = 0.2f), TapBrandDark, "Em Curso")
        EstadoTarefa.PENDENTE -> Triple(AlertOrange.copy(alpha = 0.2f), AlertOrangeText, "Pendente")
        EstadoTarefa.CONCLUIDA -> Triple(
            TextSecondary.copy(alpha = 0.2f),
            TextSecondary,
            "Concluída"
        )
    }
    Pill(fundo, texto, label)
}

/**
 * Etiqueta que mostra a prioridade de uma tarefa.
 * Traduz a prioridade nas cores e no texto certos e desenha uma [Pill].
 *
 * @param prioridade A prioridade da tarefa (alta, normal ou baixa).
 */
@Composable
fun PillPrioridade(prioridade: PrioridadeTarefa) {
    val (fundo, texto, label) = when (prioridade) {
        PrioridadeTarefa.ALTA -> Triple(TapAlert.copy(alpha = 0.2f), TapRedText, "Alta")
        PrioridadeTarefa.NORMAL -> Triple(TapBrandDark.copy(alpha = 0.2f), TapBrandDark, "Normal")
        PrioridadeTarefa.BAIXA -> Triple(TextSecondary.copy(alpha = 0.2f), TextSecondary, "Baixa")
    }
    Pill(fundo, texto, label)
}
//#my_code end

/**
 * Card de um movimento de histórico, partilhado entre perfis. É "burro": não
 * conhece tipos de movimento, recebe já a cor, o ícone e os textos prontos.
 * Cada ecrã traduz o seu próprio enum nestes valores.
 *
 * @param icone Ícone à esquerda (ex: chave, check, triângulo de aviso).
 * @param corAcao Cor do ícone e do texto da ação (azul/verde/laranja).
 * @param corFundoIcone Cor de fundo do quadrado do ícone.
 * @param nome Nome da ferramenta.
 * @param label Texto da ação ("Retirou", "Devolveu", "Marcou avaria").
 * @param subtitulo Linha por baixo do nome (ex: funcionário). Null = não aparece.
 * @param hora Hora do movimento à direita (ex: "09:45"). Null = não aparece.
 */
@Composable
fun MovimentoCard(
    icone: DrawableResource,
    corAcao: Color,
    corFundoIcone: Color,
    nome: String,
    label: String,
    subtitulo: String? = null,
    hora: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // icone
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(corFundoIcone),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icone),
                    contentDescription = null,
                    tint = corAcao,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            // nome + subtitulo
            Column(Modifier.weight(1f)) {
                Text(
                    text = nome,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (subtitulo != null) {
                    Text(
                        text = subtitulo,
                        fontSize = 10.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            // direita: ação + hora
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = corAcao
                )

                if (hora != null) {
                    Text(
                        text = hora,
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

/**
 * Moldura partilhada das três caixas de informação do [ArmarioCard] (lugares,
 * trancado e em falta). Só desenha a caixa cinzenta arredondada; o que aparece
 * lá dentro é decidido por quem a chama.
 *
 * É uma extensão de 'RowScope', ou seja, só pode ser usada dentro de um Row.
 * Assim garante-se que as três caixas ficam lado a lado e dividem o espaço por
 * igual.
 *
 * @param content O bocado de interface a mostrar dentro da caixa (por exemplo um
 *                número e a sua legenda), desenhado numa coluna centrada.
 */
@Composable
private fun RowScope.InfoBox(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .heightIn(min = 50.dp)
            .background(InfoBoxBg, RoundedCornerShape(9.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content
    )
}
//#my_code
/**
 * Etiqueta com o estado de funcionamento de um armário.
 * Verde diz "Online", laranja diz "Alerta" e cinzento diz "Offline".
 *
 * @param estado O estado do armário, que decide a cor e o texto da etiqueta.
 */
@Composable
private fun StatusPill(
    estado: EstadoArmario
) {
    val (fundo, texto, label) = when (estado) {
        EstadoArmario.OPERACIONAL -> Triple(
            TapLightGreen.copy(alpha = 0.2f),
            TapBrandDark,
            "Online"
        )

        EstadoArmario.ALERTA -> Triple(
            AlertOrange.copy(alpha = 0.2f),
            AlertOrangeText,
            "Alerta"
        )

        EstadoArmario.AVARIADO -> Triple(
            TextSecondary.copy(alpha = 0.2f),
            TextSecondary,
            "Offline"
        )
    }

    Text(
        text = label,
        color = texto,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .background(fundo, PillShape)
            .padding(horizontal = 15.dp, vertical = 2.dp)
    )
}
//#my_code end

/**
 * Torna um elemento clicável e, enquanto está a ser pressionado, encolhe-o um
 * pouco para dar a sensação de que foi carregado, voltando ao tamanho normal a
 * seguir.
 *
 * @param escalaAoPressionar Quão pequeno o elemento fica enquanto está a ser
 *                           pressionado (0.97 = encolhe 3%; 1 = não encolhe).
 * @param onClick Chamada quando o utilizador carrega no elemento.
 * @return O mesmo Modifier, agora com o comportamento de clique e de encolher.
 */
fun Modifier.clicavelComEscala(
    escalaAoPressionar: Float = 0.97f,
    onClick: () -> Unit
): Modifier = composed {
    val interacao = remember { MutableInteractionSource() }
    val pressionado by interacao.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (pressionado) escalaAoPressionar else 1f,
        animationSpec = tween(120),
        label = "escala"
    )
    graphicsLayer { scaleX = escala; scaleY = escala }
        .clickable(
            interactionSource = interacao,
            indication = LocalIndication.current,
            onClick = onClick
        )
}

/*@Preview
@Composable
fun PreviewWelcomeCard() {
    AppTheme {
        WelcomeCard("Luísa Sampaio", "Manhã", color = TapAlmostGreen)
    }
}

@Preview
@Composable
fun PreviewTopBar() {
    AppTheme {
        TopBar(
            titulo = "Dashboard",
            alertasAtivos = 1,
            onMenu = {}
        )
    }
}

@Preview
@Composable
fun PreviewSectionHeader() {
    AppTheme {
        SectionHeader(
            titulo = "Estado dos Armários",
            onVerTodos = {}
        )
    }
}

@Preview
@Composable
private fun ArmarioCardOnlinePreview() {
    AppTheme {
        ArmarioCard(
            ArmarioUi(
                nome = "Armário 1 - Ferramentas Gerais",
                slotsOcupados = 11, slotsTotal = 12,
                trancado = true, emFalta = 1, estadoArmario = EstadoArmario.OPERACIONAL
            )
        )
    }
}

@Preview
@Composable
private fun ArmarioCardAlertaPreview() {
    AppTheme {
        ArmarioCard(
            ArmarioUi(
                nome = "Armário 2 - Ferramentas Elétricas",
                slotsOcupados = 12, slotsTotal = 12,
                trancado = false, emFalta = 0, estadoArmario = EstadoArmario.ALERTA
            )
        )
    }
}

@Preview
@Composable
private fun ArmarioCardOfflinePreview() {
    AppTheme {
        ArmarioCard(
            ArmarioUi(
                nome = "Armário 3 - Ferramentas Elétricas",
                slotsOcupados = 12, slotsTotal = 12,
                trancado = false, emFalta = 0, estadoArmario = EstadoArmario.AVARIADO
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlertaCardCriticoPreview() {
    AppTheme {
        AlertaCard(
            gravidade = Gravidade.CRITICO,
            titulo = "Ferramenta 1 em falta",
            descricao = "O Gonçalo não arrumou a porcaria da ferramenta antes de bazar",
            horas = "16:32"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlertaCardAvisoPreview() {
    AppTheme {
        AlertaCard(
            gravidade = Gravidade.AVISO,
            titulo = "Ferramenta 2 para manunteção",
            descricao = "A Luísa estragou a ferramenta numa coisa básica",
            horas = "13:10"
        )
    }
}

@Preview
@Composable
private fun PreviewEstadoFerramentasCard() {
    AppTheme {
        EstadoFerramentasCard(
            EstatisticasFerramentas(
                disponiveis = 9,
                requisitada = 3,
                indisponivel = 1,
                manutencao = 2
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBarra() {
    AppTheme {
        BarraPesquisa(
            label = "Pesquisar armários...",
            query = "",
            onQueryChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFiltroChipsOn() {
    AppTheme {
        FilterChip(
            label = "Todos",
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFiltroChipsOff() {
    AppTheme {
        FilterChip(
            label = "Offline",
            isSelected = false,
            onClick = {}
        )
    }
}*/

/*
@Preview(showBackground = true)
@Composable
private fun PreviewMovimentoCard() {
    AppTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MovimentoCard(
                icone = Res.drawable.tool, corAcao = AzulRetirou, corFundoIcone = FundoAzulRetirou,
                nome = "Chave de Caixa 10mm", label = "Retirou",
                subtitulo = "Tiago Dias", hora = "09:45"
            )
            MovimentoCard(
                icone = Res.drawable.alert_triangle,
                corAcao = AlertOrangeText,
                corFundoIcone = AlertOrange.copy(alpha = 0.2f),
                nome = "Torquímetro 60Nm",
                label = "Marcou avaria",
                subtitulo = "Miguel Azenha",
                hora = "09:32"
            )
        }
    }
}*/
