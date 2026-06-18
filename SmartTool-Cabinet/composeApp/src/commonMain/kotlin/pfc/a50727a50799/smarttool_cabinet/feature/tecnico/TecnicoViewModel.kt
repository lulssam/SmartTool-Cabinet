package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel responsável pela gestão do estado e lógica de negócio do ecrã Tecnico.
 *
 * Implementa o padrão MVI (Model-View-Intent) onde:
 * - **Model**: Representado por [TecnicoState]
 * - **View**: O Composable [TecnicoScreen]
 * - **Intent**: As ações definidas em [TecnicoAction]
 *
 * O estado é exposto como um [StateFlow] que carrega dados iniciais
 * de forma lazy quando a subscrição começa.
 *
 * @see TecnicoState
 * @see TecnicoAction
 */
class TecnicoViewModel : ViewModel() {

    /**
     * Flag que indica se os dados iniciais já foram carregados.
     * Previne recarregamentos desnecessários quando o estado é re-subscrito.
     */
    private var hasLoadedInitialData = false

    /**
     * Estado interno mutável do ecrã. 
     * Apenas o ViewModel pode modificar este estado. 
     */
    private val _state = MutableStateFlow(TecnicoState())

    /**
     * Estado público imutável do ecrã, exposto como [StateFlow].
     *
     * Características:
     * - Carrega dados iniciais automaticamente na primeira subscrição
     * - Mantém-se ativo por 5 segundos após perder todos os subscritores
     *   (útil para mudanças de configuração como rotação do ecrã)
     * - Emite o estado inicial imediatamente para novos subscritores
     */
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = TecnicoState()
        )

    /**
     * Processa as ações do utilizador e atualiza o estado conforme necessário.
     *
     * Este método centraliza toda a lógica de tratamento de eventos da UI,
     * garantindo um fluxo de dados unidirecional.
     *
     * @param action A ação a ser processada, definida em [TecnicoAction].
     */
    fun onAction(action: TecnicoAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}