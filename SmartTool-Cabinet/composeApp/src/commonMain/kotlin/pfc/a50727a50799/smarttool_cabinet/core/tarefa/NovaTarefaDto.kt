package pfc.a50727a50799.smarttool_cabinet.core.tarefa

import kotlinx.serialization.Serializable
//#my_code
/**
 * Representa os dados necessários para criar uma nova tarefa.
 *
 * Contém a informação sobre quem cria a tarefa,
 * a quem será atribuída e quais as ferramentas permitidas.
 *
 * @property idGestor Identificador do gestor que cria a tarefa.
 * @property idTecnico Identificador do técnico responsável pela tarefa.
 * @property titulo Título da tarefa.
 * @property descricao Descrição da tarefa.
 * @property prioridade Prioridade atribuída à tarefa.
 * @property ferramentasPermitidasIds Lista das ferramentas que podem ser utilizadas na tarefa.
 */
@Serializable
data class NovaTarefaDto(
    val idGestor: Int,
    val idTecnico: Int,
    val titulo: String,
    val descricao: String,
    val prioridade: String = "NORMAL",
    val ferramentasPermitidasIds: List<FerramentaIdDto>
)
/**
 * Identifica uma ferramenta através do seu tipo e do seu número.
 *
 * Esta informação é utilizada para indicar quais as ferramentas
 * que podem ser utilizadas numa tarefa.
 *
 * @property codigoTipo Código que identifica o tipo da ferramenta.
 * @property nFerramenta Número da ferramenta dentro desse tipo.
 */
@Serializable
data class FerramentaIdDto(
    val codigoTipo: Int,
    val nFerramenta: Int
)
