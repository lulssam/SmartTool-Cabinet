package pfc.a50727a50799.smarttool_cabinet.feature.sso

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.feature.welcome.WelcomeViewModel
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextTitle
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.arrowleft
import smarttoolcabinet.composeapp.generated.resources.google_logo
import smarttoolcabinet.composeapp.generated.resources.tap_logo


@Composable
fun SSOScreenContent(
    onBackClick: () -> Unit,
    onGoogleClick: () -> Unit,
    isLoading: Boolean,
    error: String?
) {
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
        Spacer(Modifier.height(48.dp))

        SSOCard(onBackClick = onBackClick, onGoogleClick = onGoogleClick, isLoading = isLoading)

        if (error != null) {
            Spacer(Modifier.height(12.dp))
            Text(error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(Modifier.weight(1f))

    }
}

@Composable
fun SSOScreen(
    viewModel: WelcomeViewModel,
    onBackClick: () -> Unit,
    onGoogleClick: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    SSOScreenContent(
        isLoading = state.isLoading,
        error = state.error,
        onBackClick = onBackClick,
        onGoogleClick = onGoogleClick
    )
}

/**
 * Preview do ecrã SSO para visualização no Android Studio.
 *
 * Utiliza o tema do projeto e um estado por defeito para
 * permitir a pré-visualização durante o desenvolvimento.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        SSOScreenContent(
            isLoading = false,
            error = null,
            onBackClick = {},
            onGoogleClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewCard() {
    AppTheme {
        SSOCard(
            onBackClick = {},
            onGoogleClick = {},
            isLoading = false
        )
    }
}

@Preview
@Composable
private fun PreviewGoogleButton() {
    AppTheme {
        GoogleProviderButton(
            onGoogleClick = {},
            enabled = true
        )
    }
}


@Composable
private fun SSOCard(
    onBackClick: () -> Unit,
    onGoogleClick: () -> Unit,
    isLoading: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                shape = CardShape, shadow = Shadow(
                    radius = 10.dp,
                    spread = 3.dp,
                    color = Color.Black.copy(alpha = 0.1f),
                    offset = DpOffset(2.dp, 2.dp)
                )
            )
            .background(
                Color.White.copy(alpha = 0.9f),
                shape = CardShape
            )
            .border(
                0.5.dp, Color.Black.copy(alpha = 0.04f),
                shape = CardShape
            )

            .padding(24.dp)

    ) {
        // Voltar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onBackClick)
        ) {
            Image(
                painter = painterResource(Res.drawable.arrowleft),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                "Voltar", color = TapGreenishBlue, fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Autenticação federada",
            color = TextTitle,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Selecione o seu fornecedor de identidade",
            color = TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))
        GoogleProviderButton(onGoogleClick = onGoogleClick, enabled = !isLoading)
    }
}

@Composable
private fun GoogleProviderButton(
    onGoogleClick: () -> Unit,
    enabled: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .background(Color.White, shape = RoundedCornerShape(14.dp))
            .border(0.5.dp, Color.Black.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
            .clickable(onClick = onGoogleClick, enabled = enabled)
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.google_logo),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )

        Column {
            Text(
                text = "Google",
                color = TextTitle,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}