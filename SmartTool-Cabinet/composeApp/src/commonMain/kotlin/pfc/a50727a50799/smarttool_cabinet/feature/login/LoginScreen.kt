package pfc.a50727a50799.smarttool_cabinet.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme

/**
 * Apresenta o conteúdo visual do ecrã de autenticação.
 *
 * Este componente mostra os campos de email e palavra-passe,
 * o botão para iniciar sessão e as mensagens de sucesso ou erro.
 * Toda a lógica é fornecida através dos parâmetros recebidos.
 *
 * @param email Endereço de correio eletrónico introduzido pelo utilizador.
 * @param password Palavra-passe introduzida pelo utilizador.
 * @param isLoading Indica se o processo de autenticação está em execução.
 * @param error Mensagem de erro a apresentar, caso exista.
 * @param resultado Mensagem de sucesso a apresentar após a autenticação.
 * @param onEmailChange Função chamada quando o email é alterado.
 * @param onPasswordChange Função chamada quando a palavra-passe é alterada.
 * @param onLoginClick Função chamada quando o utilizador seleciona a opção para iniciar sessão.
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
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
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


/**
 * Preview do ecrã de autenticação.
 *
 * Utiliza dados de exemplo para permitir a visualização
 * da interface durante o desenvolvimento, sem necessidade
 * de ligação ao backend ou ao serviço de autenticação.
 */
@Preview
@Composable
fun LoginPreview() {
    AppTheme {
        LoginScreenContent(
            email = "joao.silva@tap.pt", password = "123456",
            isLoading = false, error = null, resultado = null,
            onEmailChange = {}, onPasswordChange = {}, onLoginClick = {}
        )
    }
}