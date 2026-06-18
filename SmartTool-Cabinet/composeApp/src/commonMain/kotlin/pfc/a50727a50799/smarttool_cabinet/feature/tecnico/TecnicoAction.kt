package pfc.a50727a50799.smarttool_cabinet.feature.tecnico

/**
 * Define todas as ações possíveis que o utilizador pode realizar no ecrã Tecnico. 
 *
 * Utiliza uma sealed interface para garantir:
 * - Exaustividade no tratamento de ações (o compilador avisa se faltar alguma)
 * - Type-safety nas ações e seus parâmetros
 * - Facilidade de adicionar novas ações sem quebrar código existente
 *
 * Exemplo de implementação:
 * ```kotlin
 * sealed interface TecnicoAction {
 *     data class OnItemClick(val itemId: String) : TecnicoAction
 *     data object OnRefresh : TecnicoAction
 *     data object OnBackClick : TecnicoAction
 * }
 * ```
 *
 * @see TecnicoViewModel.onAction
 */
sealed interface TecnicoAction {
}