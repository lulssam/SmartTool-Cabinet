package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


/**
 * Composable raiz que serve como ponto de entrada para o ecrã Tecnico. 
 *
 * Esta função é responsável por:
 * - Instanciar e gerir o [TecnicoViewModel]
 * - Observar o estado do ViewModel de forma lifecycle-aware
 * - Delegar a renderização para [TecnicoScreen]
 *
 * @param viewModel O ViewModel que gere o estado e lógica do ecrã. 
 *                  Por defeito, é criado automaticamente pelo Compose.
 *
 * @see TecnicoViewModel
 * @see TecnicoScreen
 */
@Composable
fun TecnicoRoot(
    viewModel: TecnicoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TecnicoScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

/**
 * Composable stateless que renderiza a UI do ecrã Tecnico.
 *
 * Segue o padrão de UI stateless, recebendo todo o estado necessário
 * como parâmetro e comunicando eventos através de callbacks.
 * Isto facilita os testes e previews.
 *
 * @param state O estado atual do ecrã contendo todos os dados a apresentar.
 * @param onAction Callback invocado quando o utilizador realiza uma ação.
 *                 As ações são definidas em [TecnicoAction]. 
 *
 * @see TecnicoState
 * @see TecnicoAction
 */
@Composable
fun TecnicoScreen(
    state: TecnicoState,
    onAction: (TecnicoAction) -> Unit,
) {

}

/**
 * Preview do ecrã Tecnico para visualização no Android Studio.
 *
 * Utiliza o tema do projeto e um estado por defeito para
 * permitir a pré-visualização durante o desenvolvimento.
 */
@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        TecnicoScreen(
            state = TecnicoState(),
            onAction = {}
        )
    }
}