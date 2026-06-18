package pfc.a50727a50799.smarttool_cabinet.feature.backoffice

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * Composable raiz que serve como ponto de entrada para o ecrã BackOffice. 
 *
 * Esta função é responsável por:
 * - Instanciar e gerir o [BackOfficeViewModel]
 * - Observar o estado do ViewModel de forma lifecycle-aware
 * - Delegar a renderização para [BackOfficeScreen]
 *
 * @param viewModel O ViewModel que gere o estado e lógica do ecrã. 
 *                  Por defeito, é criado automaticamente pelo Compose.
 *
 * @see BackOfficeViewModel
 * @see BackOfficeScreen
 */
@Composable
fun BackOfficeRoot(
    viewModel: BackOfficeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackOfficeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

/**
 * Composable stateless que renderiza a UI do ecrã BackOffice.
 *
 * Segue o padrão de UI stateless, recebendo todo o estado necessário
 * como parâmetro e comunicando eventos através de callbacks.
 * Isto facilita os testes e previews.
 *
 * @param state O estado atual do ecrã contendo todos os dados a apresentar.
 * @param onAction Callback invocado quando o utilizador realiza uma ação.
 *                 As ações são definidas em [BackOfficeAction]. 
 *
 * @see BackOfficeState
 * @see BackOfficeAction
 */
@Composable
fun BackOfficeScreen(
    state: BackOfficeState,
    onAction: (BackOfficeAction) -> Unit,
) {

}

/**
 * Preview do ecrã BackOffice para visualização no Android Studio.
 *
 * Utiliza o tema do projeto e um estado por defeito para
 * permitir a pré-visualização durante o desenvolvimento.
 */
@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        BackOfficeScreen(
            state = BackOfficeState(),
            onAction = {}
        )
    }
}