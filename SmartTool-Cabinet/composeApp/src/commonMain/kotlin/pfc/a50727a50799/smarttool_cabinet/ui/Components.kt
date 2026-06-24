package pfc.a50727a50799.smarttool_cabinet.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.ArmarioUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.EstadoArmario
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.EstatisticasFerramentas
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.Gravidade
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.InfoBoxBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
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
import smarttoolcabinet.composeapp.generated.resources.unlock

@Composable
fun TopBar(
    titulo: String,
    alertasAtivos: Int = 0,
    mostrarAlertas: Boolean = true, //mostra os alertas
    onMenu: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                )
                .height(80.dp)
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
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )

            if (mostrarAlertas) {
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
                            alertasAtivos > 1 -> "$alertasAtivos alertas ativo"
                            alertasAtivos == 1 -> "$alertasAtivos alerta ativo"
                            else -> "Nenhum alerta ativo"
                        },
                        color = TapAlert,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeCard(
    nomeGestor: String,
    turno: String,
    cargo: String = "Gestor de Armazém"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(CardShape)
            .background(TapBrandDark)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Bem-vindo de volta,", color = TapSurfaceGrey, fontSize = 13.sp)
        Text(text = nomeGestor, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "$cargo - Turno: $turno", color = Color.White, fontSize = 13.sp)
    }
}

private data class Segmento(
    val label: String,
    val valor: Int,
    val cor: Color
)

@Composable
fun EstadoFerramentasCard(
    estatisticas: EstatisticasFerramentas
) {
    val segmentos = listOf(
        Segmento("Disponíveis", estatisticas.disponiveis, TapBrandDark),
        Segmento("Em Uso", estatisticas.requisitada, ToolInUse),
        Segmento("Em Falta", estatisticas.emFalta, TapAlert),
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Donut(segmentos = segmentos, total = total)

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
            color = Color.Black
        )

        Spacer(modifier = Modifier.weight(1f)) // empurrar numeros para a direita

        Text("${segmento.valor}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.width(8.dp))
        Text("$percent%", fontSize = 11.sp, color = TextSecondary)
    }
}

@Composable
private fun Donut(
    segmentos: List<Segmento>, total: Int
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(150.dp)) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 28.dp.toPx()                 // espessura do aro
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
                    val sweep = 360f * seg.valor / total
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
            Text("$total", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("total", fontSize = 11.sp, color = TextSecondary)
        }
    }
}

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
            fontSize = 20.sp
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

@Composable
fun ArmarioCard(
    armario: ArmarioUi
) {

    val corBorda = when (armario.estadoArmario) {
        EstadoArmario.ONLINE -> TapBrandDark.copy(alpha = 0.25f)
        EstadoArmario.ALERTA -> AlertOrange.copy(alpha = 0.25f)
        EstadoArmario.OFFLINE -> TextSecondary
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
                    fontWeight = FontWeight.Medium
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

/**
 * Moldura partilhada das 3 caixas. O conteúdo vem de fora.
 *
 * 'RowScope' é uma função de extensão, só pode ser chaamda
 * dentro de um Row.
 *
 * () -> Unit = é uma função que não recebe nada e não devolve nada.
 *
 * '@Composable' = ... mas que desenha a UI,
 *
 * ColumnScope = ... e que vai correr dentro de uma Coluna
 *
 * @param content Parâmetro que em vez de ser um número ou um texto, é
 * um bocado da UI.
 *
 * */
@Composable
private fun RowScope.InfoBox(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .height(50.dp)
            .background(InfoBoxBg, RoundedCornerShape(9.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content
    )
}

/** pill de estados: verde -> Online ou laranja -> Alerta, cinzento -> Offline*/
@Composable
private fun StatusPill(
    estado: EstadoArmario
) {
    val (fundo, texto, label) = when (estado) {
        EstadoArmario.ONLINE -> Triple(TapLightGreen.copy(alpha = 0.2f), TapBrandDark, "Online")
        EstadoArmario.ALERTA -> Triple(
            AlertOrange.copy(alpha = 0.2f),
            AlertOrangeText,
            "Alerta"
        )

        EstadoArmario.OFFLINE -> Triple(
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

@Preview
@Composable
fun PreviewWelcomeCard() {
    AppTheme {
        WelcomeCard("Luísa Sampaio", "Manhã")
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
                trancado = true, emFalta = 1, estadoArmario = EstadoArmario.ONLINE
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
                trancado = false, emFalta = 0, estadoArmario = EstadoArmario.OFFLINE
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
            EstatisticasFerramentas(disponiveis = 9, requisitada = 3, emFalta = 1, manutencao = 2)
        )
    }
}