package pfc.a50727a50799.smarttool_cabinet.core.auth.data

import pfc.a50727a50799.smarttool_cabinet.core.auth.Session

/**
 * Guarda em memória, quem tem a sessão iniciada agora.
 * Fonte única lida pelos ecrãs que precisam do utilizador atual.
 * Null = ninguém*/
object SessionManager {
    var atual: Session? = null
}