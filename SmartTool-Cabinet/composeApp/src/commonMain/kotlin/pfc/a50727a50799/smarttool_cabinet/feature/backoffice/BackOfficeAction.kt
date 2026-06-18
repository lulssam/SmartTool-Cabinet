package pfc.a50727a50799.smarttool_cabinet.feature.backoffice

/**
 * Define todas as ações possíveis que o utilizador pode realizar no ecrã BackOffice. 
 *
 * Utiliza uma sealed interface para garantir:
 * - Exaustividade no tratamento de ações (o compilador avisa se faltar alguma)
 * - Type-safety nas ações e seus parâmetros
 * - Facilidade de adicionar novas ações sem quebrar código existente
 *
 * Exemplo de implementação:
 * ```kotlin
 * sealed interface BackOfficeAction {
 *     data class OnItemClick(val itemId: String) : BackOfficeAction
 *     data object OnRefresh : BackOfficeAction
 *     data object OnBackClick : BackOfficeAction
 * }
 * ```
 *
 * @see BackOfficeViewModel.onAction
 */
sealed interface BackOfficeAction {

}