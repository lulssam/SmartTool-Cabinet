package pfc.a50727a50799.smarttool_cabinet.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapBrandGreen
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapGreenishBlue
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TapLightGreen

/**
 * A parte visual do login. Não sabe nada da lógica — só mostra o que recebe
 * e avisa quando o user faz algo. Totalmente previewável sem ViewModel.
 */
@Composable
fun LoginScreenContent(
    isLoading: Boolean,
    error: String?,
    resultado: String?,                 // texto a mostrar quando o login corre bem
    onLoginEmailClick: () -> Unit,
    onSSOClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Button(
            onClick = onLoginEmailClick,
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = TapGreenishBlue),
            contentPadding = PaddingValues(vertical = 18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Iniciar sessão com email",
                fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onSSOClick,
            colors = ButtonDefaults.buttonColors(containerColor = TapLightGreen),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 18.dp),
        ) {
            Text(
                text = "Autenticação Federada",
                fontSize = 15.sp
            )
        }

        if (error != null) {
            Spacer(Modifier.height(12.dp))
            Text(error, color = MaterialTheme.colorScheme.error)
        }
        if (resultado != null) {
            Spacer(Modifier.height(12.dp))
            Text(resultado, color = MaterialTheme.colorScheme.primary)
        }
    }
}

/** Liga o ViewModel ao conteúdo visual. */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onSSOClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    LoginScreenContent(
        isLoading = state.isLoading,
        error = state.error,
        resultado = state.sessao?.let { "OK! ${it.email} — ${it.role}" },
        onLoginEmailClick = viewModel::onLoginClick,
        onSSOClick = onSSOClick
    )
}

/** Preview com dados falsos*/
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    AppTheme {
        LoginScreenContent(
            isLoading = false,
            error = null,
            resultado = null,
            onLoginEmailClick = {},
            onSSOClick = {}
        )
    }
}