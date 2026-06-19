package pfc.a50727a50799.smarttool_cabinet.feature.email

/**
 * Define todas as ações possíveis que o utilizador pode realizar no ecrã Email. 
 *
 * Utiliza uma sealed interface para garantir:
 * - Exaustividade no tratamento de ações (o compilador avisa se faltar alguma)
 * - Type-safety nas ações e seus parâmetros
 * - Facilidade de adicionar novas ações sem quebrar código existente
 *
 * Exemplo de implementação:
 * ```kotlin
 * sealed interface EmailAction {
 *     data class OnItemClick(val itemId: String) : EmailAction
 *     data object OnRefresh : EmailAction
 *     data object OnBackClick : EmailAction
 * }
 * ```
 *
 * @see EmailViewModel.onAction
 */
sealed interface EmailAction {

}