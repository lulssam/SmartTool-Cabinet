package pfc.a50727a50799.smarttool_cabinet.feature.gestor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pfc.a50727a50799.smarttool_cabinet.core.ferramenta.FerramentaDto
import pfc.a50727a50799.smarttool_cabinet.ui.theme.AppTheme
import pfc.a50727a50799.smarttool_cabinet.ui.theme.TextSecondary


@Composable
private fun GestorScreenContent(
    ferramentas: List<FerramentaDto>,
    isLoading: Boolean,
    error: String?
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(error, color = MaterialTheme.colorScheme.error)
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ferramentas) { f ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(f.nome)
                        Text(f.categoria)
                        Text(f.estado, color = TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun GestorScreen(
    viewModel: GestorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    GestorScreenContent(
        ferramentas = state.ferramentas,
        isLoading = state.isLoading,
        error = state.error
    )
}

/**
 * Preview do ecrã Gestor para visualização no Android Studio.
 *
 * Utiliza o tema do projeto e um estado por defeito para
 * permitir a pré-visualização durante o desenvolvimento.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        GestorScreenContent(
            ferramentas = listOf(
                FerramentaDto(1, "Chave de Fendas", "DISPONIVEL", "Pneumatico", "Arm.101"),
                FerramentaDto(2, "Chave Inglesa", "EM_FALTA", "Pneumatico", "Arm.102")
            ),
            isLoading = false,
            error = null
        )
    }
}