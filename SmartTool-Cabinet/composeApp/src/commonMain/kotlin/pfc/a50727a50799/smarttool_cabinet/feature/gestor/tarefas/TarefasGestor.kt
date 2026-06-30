package pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme

/**
 * Parte visual do ecrã TarefasGestor.
 *
 * Não sabe nada sobre a lógica da aplicação — apenas mostra o que recebe
 * e avisa quando o utilizador faz algo. Fácil de testar e de pré-visualizar.
 *
 * @param state Tudo o que o ecrã precisa para se mostrar corretamente.
 */
@Composable
private fun TarefasGestorScreenContent(
    state: TarefasGestorUiState,
) {
    when {
        state.isLoading ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        state.error != null ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error, color = MaterialTheme.colorScheme.error)
            }

        else -> {
            // UI aqui
        }
    }
}

/**
 * Liga o [TarefasGestorViewModel] ao [TarefasGestorScreenContent].
 *
 * Observa o estado do ViewModel e passa-o para o ecrã.
 * Não contém lógica de UI — apenas faz a ligação.
 *
 * @param viewModel O ViewModel que gere o estado deste ecrã.
 *                  É criado automaticamente pelo Compose se não for fornecido.
 */
@Composable
fun TarefasGestorScreen(
    viewModel: TarefasGestorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    TarefasGestorScreenContent(
        state = state,
    )
}

/**
 * Pré-visualização do ecrã TarefasGestor para usar durante o desenvolvimento.
 *
 * Usa dados fictícios para simular como o ecrã ficará com informação real.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TarefasGestorPreview() {
    AppTheme {
        TarefasGestorScreenContent(
            state = TarefasGestorUiState(),
        )
    }
}