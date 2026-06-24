package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HistoricoViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        HistoricoUiState(
            secoes = listOf(
                SecaoHistoricoUi(
                    data = "HOJE",
                    movimentos = listOf(
                        HistoricoItemUi(1, "Chave de Caixa 10mm", TipoMovimento.RETIROU),
                        HistoricoItemUi(2, "Multímetro Digital", TipoMovimento.RETIROU)
                    )
                ),
                SecaoHistoricoUi(
                    data = "ONTEM",
                    movimentos = listOf(
                        HistoricoItemUi(3, "Pistola de Calor", TipoMovimento.DEVOLVEU),
                        HistoricoItemUi(4, "Chave de Fendas", TipoMovimento.DEVOLVEU)
                    )
                ),
                SecaoHistoricoUi(
                    data = "15 MAIO",
                    movimentos = listOf(
                        HistoricoItemUi(5, "Chave de Fendas", TipoMovimento.DEVOLVEU),
                        HistoricoItemUi(6, "Chave Spline", TipoMovimento.DEVOLVEU),
                        HistoricoItemUi(7, "Martelo", TipoMovimento.DEVOLVEU)
                    )
                )
            )
        )
    )
    val state: StateFlow<HistoricoUiState> = _state.asStateFlow()

}