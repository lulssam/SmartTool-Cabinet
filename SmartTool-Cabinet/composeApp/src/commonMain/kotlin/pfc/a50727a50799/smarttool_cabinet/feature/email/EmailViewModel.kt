package pfc.a50727a50799.smarttool_cabinet.feature.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel responsável pela gestão do estado e lógica de negócio do ecrã Email.
 *
 * Implementa o padrão MVI (Model-View-Intent) onde:
 * - **Model**: Representado por [EmailState]
 * - **View**: O Composable [EmailScreen]
 * - **Intent**: As ações definidas em [EmailAction]
 *
 * O estado é exposto como um [StateFlow] que carrega dados iniciais
 * de forma lazy quando a subscrição começa.
 *
 * @see EmailState
 * @see EmailAction
 */
class EmailViewModel : ViewModel() {

    /**
     * Flag que indica se os dados iniciais já foram carregados.
     * Previne recarregamentos desnecessários quando o estado é re-subscrito.
     */
    private var hasLoadedInitialData = false

    /**
     * Estado interno mutável do ecrã. 
     * Apenas o ViewModel pode modificar este estado. 
     */
    private val _state = MutableStateFlow(EmailState())

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
            initialValue = EmailState()
        )

    /**
     * Processa as ações do utilizador e atualiza o estado conforme necessário.
     *
     * Este método centraliza toda a lógica de tratamento de eventos da UI,
     * garantindo um fluxo de dados unidirecional.
     *
     * @param action A ação a ser processada, definida em [EmailAction].
     */
    fun onAction(action: EmailAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }

}