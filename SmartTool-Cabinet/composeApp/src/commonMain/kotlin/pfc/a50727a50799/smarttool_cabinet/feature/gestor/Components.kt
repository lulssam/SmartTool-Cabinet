package pfc.a50727a50799.smarttool_cabinet.feature.gestor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrange
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AlertOrangeText
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardBorder
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapAlert
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandDark
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapSurfaceGrey
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.alert_triangle
import smarttoolcabinet.composeapp.generated.resources.arrowleft
import smarttoolcabinet.composeapp.generated.resources.menu

@Composable
fun TopBar(
    titulo: String,
    alertasAtivos: Int,
    onMenu: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(
                onClick = onMenu
            ) {
                Icon(
                    painter = painterResource(Res.drawable.menu),
                    contentDescription = "Menu",
                )
            }

            Text(
                text = titulo,
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .clip(PillShape)
                    .background(TapAlert.copy(alpha = 0.15f))
                    .padding(horizontal = 15.dp, vertical = 4.dp)
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

@Composable
fun WelcomeCard(
    nomeGestor: String,
    turno: String
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
        Text(
            text = "Bem-vindo de volta",
            color = TapSurfaceGrey,
            fontSize = 13.sp
        )
        Text(
            text = nomeGestor,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Gestor de Armazém - Turno: $turno",
            color = Color.White,
            fontSize = 13.sp
        )

    }
}

@Composable
fun EstadoFerramentasCard(
    estatisticas: EstatisticasFerramentas
) {
}

@Composable
fun SectionHeader(
    titulo: String,
    onVerTodos: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
            }
        }
    }
}

/** pill de estados: verde -> Online ou laranja -> Alerta, cinzento -> Offline*/
@Composable
private fun StatusPill(
    estado: EstadoArmario
) {
    val (fundo, texto, label) = when (estado) {
        EstadoArmario.ONLINE -> Triple(TapLightGreen.copy(alpha = 0.2f), TapBrandDark, "Online")
        EstadoArmario.ALERTA -> Triple(AlertOrange.copy(alpha = 0.2f), AlertOrangeText, "Alerta")
        EstadoArmario.OFFLINE -> Triple(TextSecondary.copy(alpha = 0.2f), TextSecondary, "Offline")
    }
}

@Preview
@Composable
fun PreviewWelcomeCard() {
    AppTheme {
        WelcomeCard("Luis", "Manhã")
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
fun PreviewArmarioCard() {
    AppTheme {
        ArmarioCard(
            armario = ArmarioUi(
                nome = "Arm.101 - Ferramentas Gerais",
                slotsOcupados = 10,
                slotsTotal = 11,
                trancado = false,
                emFalta = 0,
                online = EstadoArmario.ONLINE
            )
        )
    }

}