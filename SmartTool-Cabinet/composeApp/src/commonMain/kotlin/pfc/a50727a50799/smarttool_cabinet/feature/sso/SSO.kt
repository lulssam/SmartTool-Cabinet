package pfc.a50727a50799.smarttool_cabinet.feature.sso

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme


/**
 * Composable raiz que serve como ponto de entrada para o ecrã SSO. 
 *
 * Esta função é responsável por:
 * - Instanciar e gerir o [SSOViewModel]
 * - Observar o estado do ViewModel de forma lifecycle-aware
 * - Delegar a renderização para [SSOScreen]
 *
 * @param viewModel O ViewModel que gere o estado e lógica do ecrã. 
 *                  Por defeito, é criado automaticamente pelo Compose.
 *
 * @see SSOViewModel
 * @see SSOScreen
 */
@Composable
fun SSORoot(
    viewModel: SSOViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SSOScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

/**
 * Composable stateless que renderiza a UI do ecrã SSO.
 *
 * Segue o padrão de UI stateless, recebendo todo o estado necessário
 * como parâmetro e comunicando eventos através de callbacks.
 * Isto facilita os testes e previews.
 *
 * @param state O estado atual do ecrã contendo todos os dados a apresentar.
 * @param onAction Callback invocado quando o utilizador realiza uma ação.
 *                 As ações são definidas em [SSOAction]. 
 *
 * @see SSOState
 * @see SSOAction
 */
@Composable
fun SSOScreen(
    state: SSOState,
    onAction: (SSOAction) -> Unit,
) {

}

/**
 * Preview do ecrã SSO para visualização no Android Studio.
 *
 * Utiliza o tema do projeto e um estado por defeito para
 * permitir a pré-visualização durante o desenvolvimento.
 */
@Preview
@Composable
private fun Preview() {
    AppTheme {
        SSOScreen(
            state = SSOState(),
            onAction = {}
        )
    }
}