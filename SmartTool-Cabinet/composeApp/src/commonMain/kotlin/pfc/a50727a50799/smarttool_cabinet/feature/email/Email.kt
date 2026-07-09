package pfc.a50727a50799.smarttool_cabinet.feature.email

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import pfc.a50727a50799.smarttool_cabinet.core.auth.UserRole
import pfc.a50727a50799.smarttool_cabinet.feature.session.SessionUiState
import pfc.a50727a50799.smarttool_cabinet.feature.welcome.WelcomeViewModel
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.CardShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldBg
import pfc.a50727a50799.smarttool_cabinet.ui.theme.FieldShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.PillShape
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextTitle
import smarttoolcabinet.composeapp.generated.resources.Res
import smarttoolcabinet.composeapp.generated.resources.arrowleft
import smarttoolcabinet.composeapp.generated.resources.eye_off
import smarttoolcabinet.composeapp.generated.resources.eye_open
import smarttoolcabinet.composeapp.generated.resources.lock
import smarttoolcabinet.composeapp.generated.resources.mail
import smarttoolcabinet.composeapp.generated.resources.tap_logo


@Composable
fun emailScreenContent(
    email: String,
    password: String,
    isLoading: Boolean,
    error: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onEntrarClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(100.dp))

        // LOGO
        Image(
            painter = painterResource(Res.drawable.tap_logo),
            contentDescription = "TAP Air Portugal",
            modifier = Modifier.fillMaxWidth(0.8f).widthIn(max = 260.dp)
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
        signInCard(
            email = email,
            password = password,
            isLoading = isLoading,
            error = error,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onEntrarClick = onEntrarClick,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun emailScreen(
    viewModel: WelcomeViewModel,
    onBackClick: () -> Unit,
    onAuthenticated: (UserRole) -> Unit
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(state.sessao) {
        state.sessao?.let {
            onAuthenticated(
                it.role
            )
        }
    }

    emailScreenContent(
        email = state.email,
        password = state.password,
        isLoading = state.isLoading,
        error = state.error,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onEntrarClick = viewModel::onLoginClick,
        onBackClick = onBackClick,
    )
}

/**
 * Preview do ecrã Email para visualização no Android Studio.
 *
 * Utiliza o tema do projeto e um estado por defeito para
 * permitir a pré-visualização durante o desenvolvimento.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        emailScreenContent(
            email = "lumelosampaio@gmail.com",
            password = "",
            isLoading = false,
            error = null,
            onEmailChange = {},
            onPasswordChange = {},
            onBackClick = {},
            onEntrarClick = {},
        )
    }
}

@Preview
@Composable
private fun signInPreview() {
    AppTheme {
        signInCard(
            email = "lumelosampaio@gmail.com",
            password = "",
            isLoading = false,
            error = null,
            onEmailChange = {},
            onPasswordChange = {},
            onEntrarClick = {},
            onBackClick = {})
    }
}

/**
 * O cartão branco do ecrã de login por email.
 * Mostra os campos de email e palavra-passe, o botão de entrar e um eventual erro.
 * Não tem lógica nenhuma — recebe o que mostrar e avisa quando o utilizador interage.
 *
 * @param email O que o utilizador escreveu até agora no campo de email.
 * @param password O que o utilizador escreveu até agora no campo da palavra-passe.
 * @param isLoading True enquanto o login está a correr (desativa o botão e mostra o spinner).
 * @param error Mensagem a mostrar se o login falhar. Null = sem erro.
 * @param onEmailChange Avisado sempre que o utilizador escreve no campo de email.
 * @param onPasswordChange Avisado sempre que o utilizador escreve na palavra-passe.
 * @param onLoginClick Chamado quando carrega em "Entrar".
 * @param onBackClick Chamado quando carrega em "Voltar".
 */
@Composable
private fun signInCard(
    email: String,
    password: String,
    isLoading: Boolean,
    error: String?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onEntrarClick: () -> Unit,
    onBackClick: () -> Unit,
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
                Color.White.copy(alpha = 0.9f), shape = CardShape
            ).border(
                0.5.dp, Color.Black.copy(alpha = 0.04f), shape = CardShape
            ).padding(24.dp)
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
                "Voltar", color = TapGreenishBlue, fontSize = 13.sp, fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Iniciar Sessão",
            color = TextTitle,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Introduza as suas credenciais",
            color = TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))

        // cores partilhadas pelos dois campos
        val fieldColors = TextFieldDefaults.colors(
            focusedContainerColor = FieldBg,
            unfocusedContainerColor = FieldBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )

        Text(
            text = "Email", color = TextTitle, fontWeight = FontWeight.Medium, fontSize = 13.sp
        )
        Spacer(Modifier.height(6.dp))

        TextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("nome@tap.pt", color = TextSecondary) },
            leadingIcon = {
                Image(
                    painter = painterResource(Res.drawable.mail),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            singleLine = true,
            shape = FieldShape,
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // pass
        Text(
            text = "Palavra-passe",
            color = TextTitle,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(6.dp))

        var passwordVisible by remember { mutableStateOf(false) }
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = { Text("********", color = TextSecondary) },
            leadingIcon = {
                Image(
                    painter = painterResource(Res.drawable.lock),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            },
            trailingIcon = {
                val olho = if (passwordVisible) Res.drawable.eye_off else Res.drawable.eye_open
                Image(
                    painter = painterResource(olho),
                    contentDescription = if (passwordVisible)
                        "Esconder palavra-passe"
                    else
                        "Mostrar palavra-passe",
                    modifier = Modifier.size(16.dp).clickable { passwordVisible = !passwordVisible }
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            singleLine = true,
            shape = FieldShape,
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        // entrar
        Button(
            onClick = onEntrarClick,
            enabled = !isLoading,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            if (isLoading)
                CircularProgressIndicator(
                    Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            else
                Text(text = "Entrar", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}