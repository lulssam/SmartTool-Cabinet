package pfc.a50727a50799.smarttool_cabinet.feature.gestor

/**
 * Define todas as ações possíveis que o utilizador pode realizar no ecrã Gestor. 
 *
 * Utiliza uma sealed interface para garantir:
 * - Exaustividade no tratamento de ações (o compilador avisa se faltar alguma)
 * - Type-safety nas ações e seus parâmetros
 * - Facilidade de adicionar novas ações sem quebrar código existente
 *
 * Exemplo de implementação:
 * ```kotlin
 * sealed interface GestorAction {
 *     data class OnItemClick(val itemId: String) : GestorAction
 *     data object OnRefresh : GestorAction
 *     data object OnBackClick : GestorAction
 * }
 * ```
 *
 * @see GestorViewModel.onAction
 */
sealed interface GestorAction {

}