package pfc.a50727a50799.smarttool_cabinet.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapAlert
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.alert_triangle
import smarttoolcabinet.composeapp.generated.resources.armarios_icon
import smarttoolcabinet.composeapp.generated.resources.box
import smarttoolcabinet.composeapp.generated.resources.clipboard
import smarttoolcabinet.composeapp.generated.resources.clock
import smarttoolcabinet.composeapp.generated.resources.dashboard_icon
import smarttoolcabinet.composeapp.generated.resources.lock
import smarttoolcabinet.composeapp.generated.resources.log_out
import smarttoolcabinet.composeapp.generated.resources.menu
import smarttoolcabinet.composeapp.generated.resources.search
import smarttoolcabinet.composeapp.generated.resources.tool


/**
 * Uma opção do menu lateral. É igual para qualquer perfil, o que muda é a
 * lista de opções que cada perfil entrega.
 *
 * @property id Identificador único. Serve para saber qual está selecionada e para navegar.
 * @property label Texto mostrado na linha.
 * @property icone Ícone à esquerda do texto.
 * @property badge Número no badge vermelho (ex: alertas). 0 = sem badge.
 */
//#my_code
data class MenuOpcao(
    val id: String,
    val label: String,
    val icone: DrawableResource,
    val badge: Int = 0
)
//#my_code end


@Composable
fun DrawerSheet(
    opcoes: List<MenuOpcao>,
    idSelecionado: String,
    nomeUtilizador: String,
    cargo: String,
    onOpcaoClick: (MenuOpcao) -> Unit,
    onLogoutClick: () -> Unit
) {
    BoxWithConstraints {
        val largura = (maxWidth * 0.85f).coerceAtMost(360.dp)
        Column(
            modifier = Modifier
                .width(largura)
                .fillMaxHeight()
                .background(TapGreenishBlue)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical)
                )
                .padding(vertical = 24.dp)
        ) {
            // logo + nome aplicação
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.box),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = TapGreenishBlue
                    )
                }

                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "SmartTool",
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Cabinet Management",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

            Spacer(Modifier.height(8.dp))

            // opções que vão variar por perfil
            opcoes.forEach { opcao ->
                DrawerItem(
                    opcao = opcao,
                    selecionado = opcao.id == idSelecionado,
                    onClick = { onOpcaoClick(opcao) }
                )
            }

            Spacer(Modifier.weight(1f)) // empurrar rodapé para o fundo do ecrã

            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // rodapé
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iniciais(nomeUtilizador),
                        color = TapGreenishBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = nomeUtilizador,
                        color = Color.White,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = cargo,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.log_out),
                        contentDescription = "Terminar sessão",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

/** Iniciais: primeira letra do primeiro nome + primeira letra do último nome. */
private fun iniciais(nome: String): String {
    val partes = nome.trim().split(" ").filter { it.isNotBlank() }
    return when {
        partes.isEmpty() -> ""
        partes.size == 1 -> partes.first().take(1)
        else -> "${partes.first().first()}${partes.last().first()}"
    }.uppercase()
}

//#my_code
@Composable
private fun DrawerItem(
    opcao: MenuOpcao,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = if (selecionado) Color.White.copy(alpha = 0.1f)
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(opcao.icone),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(14.dp))

        Text(
            text = opcao.label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (opcao.badge > 0) {
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(TapAlert)
                    .padding(horizontal = 15.dp, vertical = 1.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${opcao.badge}",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
//#my_code end
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DrawerSheetPreview() {
    AppTheme {
        DrawerSheet(
            opcoes = listOf(
                MenuOpcao("dashboard", "Dashboard", Res.drawable.dashboard_icon),
                MenuOpcao("armarios", "Armários", Res.drawable.armarios_icon),
                MenuOpcao("tarefas", "Tarefas", Res.drawable.clipboard),
                MenuOpcao("ferramentas", "Ferramentas", Res.drawable.tool),
                MenuOpcao("alertas", "Alertas", Res.drawable.alert_triangle, badge = 4),
                MenuOpcao("historico", "Histórico", Res.drawable.clock)
            ),
            idSelecionado = "dashboard",
            nomeUtilizador = "Carlos Gonçalves",
            cargo = "Gestor de Armazém",
            onOpcaoClick = {},
            onLogoutClick = {}
        )
    }
}