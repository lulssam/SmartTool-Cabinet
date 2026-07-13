package pfc.a50727a50799.smarttool_cabinet.feature.welcome

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.tappableElement
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.core.network.ServerConfig
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.tap_logo
import kotlin.collections.mutableSetOf

/**
 * A parte visual do login. Não sabe nada da lógica — só mostra o que recebe
 * e avisa quando o user faz algo. Totalmente previewável sem ViewModel.
 */
@Composable
fun WelcomeScreenContent(
    onLoginEmailClick: () -> Unit,
    onSSOClick: () -> Unit = {}
) {

    var visivel by remember { mutableStateOf(false) }
    var mostrarDialogServidor by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visivel = true }
    val alpha by animateFloatAsState(
        targetValue = if (visivel) 1f else 0f,
        animationSpec = tween(700),
        label = "fadeIn"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(100.dp))

        // LOGO
        Image(
            painter = painterResource(Res.drawable.tap_logo),
            contentDescription = "TAP Air Portugal",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .widthIn(max = 260.dp)
                .graphicsLayer {
                    this.alpha = alpha
                    translationY = (1f - alpha) * 40f
                }
        )

        Spacer(Modifier.height(28.dp))

        Text(
            text = "SmartTool Cabinet".uppercase(),
            color = TapGreenishBlue,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(6.dp))
        Box(Modifier.widthIn(32.dp).height(2.dp).background(TapLightGreen))

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLoginEmailClick,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text(
                text = "Iniciar sessão com email",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onSSOClick,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = TapLightGreen),
            modifier = Modifier.fillMaxWidth().height(55.dp),
        ) {
            Text(
                text = "Autenticação Federada",
                fontSize = 15.sp
            )

        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = { mostrarDialogServidor = true }
        ) {
            Text(
                text = "Definições de Servidor",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TapGreenishBlue
            )
        }

        Spacer(modifier = Modifier.weight(0.4f))
    }

    if (mostrarDialogServidor) {
        ServidorDialog(onDismiss = { mostrarDialogServidor = false })
    }
}

/**
 * Pop-up para escolher o endereço do backend a partir da interface.
 * Só grava em [ServerConfig.baseUrl] quando o utilizador carrega em "Guardar",
 * por isso o valor é normalizado uma única vez (e não a cada tecla).
 */
@Composable
fun ServidorDialog(onDismiss: () -> Unit) {
    var url by remember { mutableStateOf(ServerConfig.baseUrl) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = CardShape,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .widthIn(max = 480.dp)
                .imePadding()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Servidor",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )

                        Text(
                            "Endereço do backend a que a app se liga",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(TextSecondary.copy(alpha = 0.2f))
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "x",
                            color = TextSecondary,
                            fontSize = 16.sp
                        )
                    }
                }

                HorizontalDivider()

                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "URL do servidor",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    TextField(
                        value = url,
                        onValueChange = { url = it },
                        singleLine = true,
                        placeholder = { Text("ex.: 192.168.1.5:8080") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = FieldBg,
                            unfocusedContainerColor = FieldBg,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Text(
                        "Vai ligar a: ${normalizarUrl(url)}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
                Button(
                    onClick = {
                        ServerConfig.baseUrl = normalizarUrl(url)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
                    shape = CardShape,
                    enabled = url.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(48.dp)
                ) {
                    Text(
                        "Guardar",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Transforma o texto escrito no campo num URL utilizável pelo cliente HTTP.
 * Aceita "192.168.1.5", "192.168.1.5:8080" ou já "http://192.168.1.5:8080" e
 * devolve sempre "http://host:porta", sem barra no fim (os pedidos usam
 * caminhos absolutos como "/api/...").
 *
 * @param url O texto escrito no campo.
 * @return O URL correspondente.
 */
fun normalizarUrl(url: String): String {
    var s = url.trim()
    if (s.isEmpty()) return s

    // Garante o esquema (assume http:// se o utilizador não o escreveu)
    if (!s.startsWith("http://") && !s.startsWith("https://")) {
        s = "http://$s"
    }

    // Separa "http://" do resto para analisar host e porta
    val esquema = s.substringBefore("://") + "://"
    val resto = s.removePrefix(esquema).trimEnd('/')
    val autoridade = resto.substringBefore('/')   // host[:porta]
    val caminho = resto.removePrefix(autoridade)  // normalmente vazio

    // Se faltar a porta, assume a 8080 (a porta do backend Ktor)
    val autoridadeComPorta =
        if (autoridade.contains(':')) autoridade else "$autoridade:8080"

    return esquema + autoridadeComPorta + caminho
}

/** Liga o ViewModel ao conteúdo visual. */
@Composable
fun WelcomeScreen(
    onLoginEmailClick: () -> Unit,
    onSSOClick: () -> Unit
) {
    WelcomeScreenContent(
        onLoginEmailClick = onLoginEmailClick,
        onSSOClick = onSSOClick
    )
}

/** Preview com dados falsos*/
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomePreview() {
    AppTheme {
        WelcomeScreenContent(
            onLoginEmailClick = {},
            onSSOClick = {}
        )
    }
}

@Preview
@Composable
fun ServidorPreview() {
    AppTheme {
        ServidorDialog(onDismiss = {})

    }
}