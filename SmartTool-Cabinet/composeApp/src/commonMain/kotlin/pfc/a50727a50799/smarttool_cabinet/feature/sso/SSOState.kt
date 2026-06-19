package pfc.a50727a50799.smarttool_cabinet.feature.sso

/**
 * Representa o estado completo do ecrã SSO. 
 *
 * Esta data class contém todos os dados necessários para renderizar a UI. 
 * É imutável, garantindo que mudanças de estado criam novas instâncias,
 * facilitando a deteção de alterações pelo Compose.
 *
 * Todos os parâmetros têm valores por defeito, permitindo:
 * - Criação fácil de estados para testes e previews
 * - Atualizações parciais usando `copy()`
 *
 * Exemplo de atualização de estado no ViewModel:
 * ```kotlin
 * _state.update { currentState ->
 *     currentState.copy(paramOne = "novo valor")
 * }
 * ```
 *
 * @property paramOne Exemplo de parâmetro string com valor por defeito. 
 * @property paramTwo Exemplo de lista de strings, vazia por defeito.
 *
 * @see SSOViewModel
 */
data class SSOState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)