package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TecnicoViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        TecnicoUiState(
            minhasFerramentas = listOf(
                FerramentaTecnicoUi(
                    id = 1,
                    nome = "Chave de Caixa 10mm",
                    detalhes = "Arm. 1 · Chaves",
                    estado = "Em Uso"
                )
            )
        )
    )
    val state: StateFlow<TecnicoUiState> = _state.asStateFlow()
}