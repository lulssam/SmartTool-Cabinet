package pfc.a50727a50799.smarttool_cabinet.core.tecnico

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.tarefas.TecnicoUi

//#my_code
/**
 * Representa a informação de um técnico recebida da API.
 *
 * Contém os dados necessários para identificar o técnico
 * e conhecer o seu turno e disponibilidade.
 *
 * @property id Identificador do técnico.
 * @property nome Nome do técnico.
 * @property turno Turno de trabalho do técnico.
 * @property disponivel Indica se o técnico está disponível.
 */
@Serializable
data class TecnicoDto(
    val id: Int,
    val nome: String,
    val turno: String,
    val disponivel: Boolean
)
/**
 * Converte o técnico para o modelo utilizado pela interface.
 *
 * Esta conversão adapta os dados recebidos da API para o formato
 * esperado pela interface da aplicação.
 *
 * @return Objeto utilizado para apresentar o técnico na interface.
 */
fun TecnicoDto.toUi(): TecnicoUi = TecnicoUi(
    id = id,
    nome = nome,
    turno = turno,
    disponivel = disponivel
)