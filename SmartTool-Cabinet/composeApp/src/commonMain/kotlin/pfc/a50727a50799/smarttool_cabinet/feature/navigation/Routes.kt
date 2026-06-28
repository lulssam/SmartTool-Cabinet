package pfc.a50727a50799.smarttool_cabinet.feature.navigation

import kotlinx.serialization.Serializable
import pfc.a50727a50799.smarttool_cabinet.core.auth.UserRole

/** Rota do ecrã de login (entrada por email/password ou Google). */
@Serializable data object WelcomeRoute

@Serializable data object LoginEmailRoute

@Serializable data object SSORoute

/** Rota do ecrã principal do Gestor. */
@Serializable data object GestorRoute

/** Rota do ecrã principal do Back Office. */
@Serializable data object BackOfficeRoute

/** Rota do ecrã principal do Técnico. */
@Serializable data object TecnicoRoute

@Serializable data object HistoricoTecnicoRoute

@Serializable data object FerramentasTecnicoRoute

@Serializable data object ArmariosGestorRoute

@Serializable data object AlertasGestorRoute
/**
 * Indica qual é o ecrã inicial de cada cargo, para a app saber para onde enviar
 * o utilizador logo a seguir ao login (ou no arranque, se já tiver sessão).
 *
 * @param role O cargo do utilizador (Gestor, Back Office ou Técnico).
 * @return A rota do ecrã principal correspondente a esse cargo.
 */
fun routeForRole(role: UserRole): Any = when (role) {
    UserRole.GESTOR -> GestorRoute
    UserRole.BACKOFFICE -> BackOfficeRoute
    UserRole.TECNICO -> TecnicoRoute
}