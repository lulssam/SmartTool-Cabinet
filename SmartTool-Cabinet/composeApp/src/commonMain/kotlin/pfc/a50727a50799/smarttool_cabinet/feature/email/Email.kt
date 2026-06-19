package pfc.a50727a50799.smarttool_cabinet.feature.email

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme


/**
 * Composable raiz que serve como ponto de entrada para o ecrã Email. 
 *
 * Esta função é responsável por:
 * - Instanciar e gerir o [EmailViewModel]
 * - Observar o estado do ViewModel de forma lifecycle-aware
 * - Delegar a renderização para [EmailScreen]
 *
 * @param viewModel O ViewModel que gere o estado e lógica do ecrã. 
 *                  Por defeito, é criado automaticamente pelo Compose.
 *
 * @see EmailViewModel
 * @see EmailScreen
 */
@Composable
fun EmailRoot(
    viewModel: EmailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EmailScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

/**
 * Composable stateless que renderiza a UI do ecrã Email.
 *
 * Segue o padrão de UI stateless, recebendo todo o estado necessário
 * como parâmetro e comunicando eventos através de callbacks.
 * Isto facilita os testes e previews.
 *
 * @param state O estado atual do ecrã contendo todos os dados a apresentar.
 * @param onAction Callback invocado quando o utilizador realiza uma ação.
 *                 As ações são definidas em [EmailAction]. 
 *
 * @see EmailState
 * @see EmailAction
 */
@Composable
fun EmailScreen(
    state: EmailState,
    onAction: (EmailAction) -> Unit,
) {

}

/**
 * Preview do ecrã Email para visualização no Android Studio.
 *
 * Utiliza o tema do projeto e um estado por defeito para
 * permitir a pré-visualização durante o desenvolvimento.
 */
@Preview
@Composable
private fun Preview() {
    AppTheme {
        EmailScreen(
            state = EmailState(),
            onAction = {}
        )
    }
}