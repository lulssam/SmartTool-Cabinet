package pfc.a50727a50799.smarttool_cabinet.core.auth.data

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.core.auth.UserRole

/**
 * endpoint /api/funcionarios/{email} devolve*/
@Serializable
data class FuncionarioDto(
    val idFunc: Int,
    val nome: String,
    val cargo: String
)

/**
 * Transforma a string "cargo" do backend no enum UserRole.
 * Devolve null se vier algo desconhecido
 * */
fun FuncionarioDto.toUserRole(): UserRole? = when (cargo) {
    "GESTOR" -> UserRole.GESTOR
    "BACKOFFICE" -> UserRole.BACKOFFICE
    "TECNICO" -> UserRole.TECNICO
    else -> null
}



