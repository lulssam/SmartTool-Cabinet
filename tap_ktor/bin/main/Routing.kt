package com.tapktor

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.sql.DriverManager

fun Application.configureRouting() {
    routing {

        get("/") {
            call.respondText("Hello, World! O motor está a funcionar!")
        }

        get("/api/ferramentas") {
            val url = "jdbc:mysql://localhost:3306/GestaoFerramentas?useSSL=false&allowPublicKeyRetrieval=true"
            val user = "root"
            val password = "rootpass"

            val lista = mutableListOf<FerramentaResponse>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)
                try {
                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery(
                        "SELECT idFerramenta, Nome_Tipo, categoria, estado, disponibilidade, Armario FROM View_Inventario_Detalhado"
                    )

                    while (resultSet.next()) {
                        lista.add(
                            FerramentaResponse(
                                idFerramenta = resultSet.getInt("idFerramenta"),
                                nome = resultSet.getString("Nome_Tipo"),
                                categoria = resultSet.getString("categoria"),
                                estado = resultSet.getString("estado"),
                                disponibilidade = resultSet.getString("disponibilidade"),
                                localizacao = "Arm. ${resultSet.getString("Armario")}"
                            )
                        )
                    }
                } finally {
                    connection.close()
                }

                call.respond(lista)

            } catch (e: Exception) {
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain)
            }
        }
    }
}

@Serializable
data class FerramentaResponse(
    val idFerramenta: Int,
    val nome: String,
    val categoria: String,
    val estado: String,
    val disponibilidade: String,
    val localizacao: String
)

@Serializable
data class HistoricoResponse(
    val idrequisicao: Int,
    val tecnico: String,
    val ferramenta: String,
    val dataSaida: String
)