package pfc.a50727a50799.smarttool_cabinet.feature.sso

/**
 * Define todas as ações possíveis que o utilizador pode realizar no ecrã SSO. 
 *
 * Utiliza uma sealed interface para garantir:
 * - Exaustividade no tratamento de ações (o compilador avisa se faltar alguma)
 * - Type-safety nas ações e seus parâmetros
 * - Facilidade de adicionar novas ações sem quebrar código existente
 *
 * Exemplo de implementação:
 * ```kotlin
 * sealed interface SSOAction {
 *     data class OnItemClick(val itemId: String) : SSOAction
 *     data object OnRefresh : SSOAction
 *     data object OnBackClick : SSOAction
 * }
 * ```
 *
 * @see SSOViewModel.onAction
 */
sealed interface SSOAction {

}