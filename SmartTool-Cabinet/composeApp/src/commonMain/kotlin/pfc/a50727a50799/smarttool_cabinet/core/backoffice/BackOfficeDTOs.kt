package pfc.a50727a50799.smarttool_cabinet.core.backoffice

import kotlinx.serialization.Serializable

//#my_code
/**
 * Representa a informação de um funcionário recebida da API.
 *
 * Contém os dados necessários para identificar o funcionário
 * e conhecer o seu cargo, turno e estado.
 *
 * @property idFunc Identificador único do funcionário.
 * @property nome Nome do funcionário.
 * @property email Endereço de correio eletrónico do funcionário.
 * @property cargo Cargo desempenhado pelo funcionário.
 * @property turno Turno de trabalho do funcionário.
 * @property ativo Indica se o funcionário se encontra ativo.
 */
@Serializable
data class FuncionariosDTO(
    val idFunc: Int = -1,
    val nome: String = "Desconhecido",
    val email: String? = null,
    val cargo: String? = null,
    val turno: String? = null,
    val ativo: Boolean = false
)
/**
 * Representa um pedido para alterar o cargo de um funcionário.
 *
 * @property cargo Novo cargo a atribuir ao funcionário.
 */
@Serializable
data class NovoCargoDTO(val cargo: String)
/**
 * Representa um pedido para alterar o turno de um funcionário.
 *
 * @property turno Novo turno a atribuir ao funcionário.
 */
@Serializable
data class NovoTurnoDTO(val turno: String)
/**
 * Representa a informação de um armário recebida pela área de BackOffice.
 *
 * @property nArmario Número que identifica o armário.
 * @property capacidade Quantidade máxima de ferramentas que o armário pode armazenar.
 * @property estado Estado atual do armário.
 * @property trancado Indica se o armário se encontra trancado.
 */
@Serializable
data class BOArmarioDTO(
    val nArmario: Int,
    val capacidade: Int,
    val estado: String,
    val trancado: Boolean
)
/**
 * Representa os dados necessários para criar um novo funcionário.
 *
 * @property nomeCompleto Nome completo do funcionário.
 * @property email Endereço de correio eletrónico do funcionário.
 * @property cargo Cargo que será atribuído ao funcionário.
 * @property turno Turno de trabalho atribuído ao funcionário.
 */
@Serializable
data class CriarFuncionarioRequest(
    val nomeCompleto: String,
    val email: String,
    val cargo: String,
    val turno: String
)