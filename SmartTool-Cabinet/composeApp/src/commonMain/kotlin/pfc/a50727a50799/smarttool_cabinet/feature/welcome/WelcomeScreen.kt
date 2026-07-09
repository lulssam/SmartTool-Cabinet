package pfc.a50727a50799.smarttool_cabinet.feature.welcome

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
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

        Spacer(modifier = Modifier.weight(0.4f))
    }
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