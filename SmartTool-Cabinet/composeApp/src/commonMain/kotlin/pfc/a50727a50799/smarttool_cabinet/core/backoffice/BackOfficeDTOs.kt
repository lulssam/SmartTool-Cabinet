package pfc.a50727a50799.smarttool_cabinet.core.backoffice

import kotlinx.serialization.Serializable

@Serializable
data class FuncionariosDTO(
    val idFunc: Int = -1,
    val nome: String = "Desconhecido",
    val email: String? = null,
    val cargo: String? = null,
    val turno: String? = null,
    val ativo: Boolean = false
)

@Serializable
data class NovoCargoDTO(val cargo: String)

@Serializable
data class NovoTurnoDTO(val turno: String)
@Serializable
data class BOArmarioDTO(
    val nArmario: Int,
    val capacidade: Int,
    val estado: String,
    val trancado: Boolean
)