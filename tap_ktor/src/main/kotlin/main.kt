package com.tapktor
//#my_code
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

/**
 * Ponto de entrada da aplicação.
 *
 * Inicia o servidor Ktor utilizando a configuração definida
 * nos ficheiros da aplicação.
 *
 * @param args Argumentos recebidos durante o arranque da aplicação.
 */
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

/**
 * Configura os principais componentes da aplicação.
 *
 * Durante o arranque da aplicação, este método inicializa a
 * serialização de dados e regista todas as rotas da API.
 */
fun Application.module() {
    configureSerialization()
    configureRouting()
}