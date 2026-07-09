package pfc.a50727a50799.smarttool_cabinet.core.armario

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.ArmarioUi
import pfc.a50727a50799.smarttool_cabinet.feature.gestor.dashboard.EstadoArmario
/**
 * Representa a informação de um armário recebida da API.
 *
 * Contém os dados necessários para identificar o armário
 * e conhecer o seu estado atual.
 *
 * @property nArmario Número que identifica o armário.
 * @property capacidade Quantidade máxima de ferramentas que o armário pode armazenar.
 * @property estado Estado atual do armário.
 * @property trancado Indica se o armário se encontra trancado.
 */
//#my_code
@Serializable
data class ArmarioDto(
    val nArmario: Int,
    val capacidade: Int,
    val estado: String, // operacional, avariado
    val trancado: Boolean
)
/**
 * Converte os dados do armário para o modelo utilizado pela interface.
 *
 * Durante a conversão são acrescentadas informações que não são fornecidas
 * pela API, como o número de espaços ocupados e o número de ferramentas em falta.
 *
 * @param slotsOcupados Número de posições atualmente ocupadas no armário.
 * @param emFalta Número de ferramentas em falta no armário.
 * @return Objeto utilizado pela interface para apresentar o armário.
 */
fun ArmarioDto.toUi(
    slotsOcupados: Int,
    emFalta: Int
) = ArmarioUi(
    nome = "Armário $nArmario",
    slotsTotal = capacidade,
    slotsOcupados = slotsOcupados,
    emFalta = emFalta,
    trancado = trancado,
    estadoArmario = when (estado) {
        "Operacional" -> EstadoArmario.OPERACIONAL
        "Alerta" -> EstadoArmario.ALERTA
        else -> EstadoArmario.AVARIADO
    }

)