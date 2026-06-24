package pfc.a50727a50799.smarttool_cabinet.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A parte visual do login. Não sabe nada da lógica — só mostra o que recebe
 * e avisa quando o user faz algo. Totalmente previewável sem ViewModel.
 */
@Composable
fun LoginScreenContent(
    email: String,
    password: String,
    isLoading: Boolean,
    error: String?,
    resultado: String?,                 // texto a mostrar quando o login corre bem
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login (teste)", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email, onValueChange = onEmailChange,
            label = { Text("Email") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password, onValueChange = onPasswordChange,
            label = { Text("Password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Button(onClick = onLoginClick, enabled = !isLoading, modifier = Modifier.fillMaxWidth()) {
            Text(if (isLoading) "A entrar..." else "Entrar")
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
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.state.collectAsState()
    LoginScreenContent(
        email = state.email,
        password = state.password,
        isLoading = state.isLoading,
        error = state.error,
        resultado = state.sessao?.let { "OK! ${it.email} — ${it.role}" },
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLoginClick
    )
}

/** Preview com dados falsos — vês o ecrã sem precisar de Firebase nem backend. */
@Preview
@Composable
fun LoginPreview() {
    LoginScreenContent(
        email = "joao.silva@tap.pt", password = "123456",
        isLoading = false, error = null, resultado = null,
        onEmailChange = {}, onPasswordChange = {}, onLoginClick = {}
    )
}