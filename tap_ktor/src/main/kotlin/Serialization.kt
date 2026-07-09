package com.tapktor

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
/**
 * Configura a serialização de dados da aplicação.
 *
 * Esta configuração permite que a aplicação converta automaticamente
 * objetos Kotlin para JSON quando envia respostas e converta JSON
 * recebido nos pedidos para objetos Kotlin.
 */

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
