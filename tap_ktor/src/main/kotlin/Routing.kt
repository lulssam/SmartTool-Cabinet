package com.tapktor

import com.tapktor.respostas.FerramentaResponse
import com.tapktor.respostas.FuncionariosResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.sql.DriverManager
import javax.swing.text.AbstractDocument.Content

fun Application.configureRouting() {
    routing {

        // devolve vista das ferramentas
        get("/api/ferramentas") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
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

        // devolve {idFunc, nome, cargo}
        get("/api/funcionarios/{email}") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = "root"
            val password = "rootpass"

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)
                try {
                    val email = call.parameters["email"]
                    val sql = "SELECT id_func, nomeCompleto, email, cargo FROM View_Email WHERE email = ?"
                    val statement = connection.prepareStatement(sql)
                    statement.setString(1, email)
                    val resultSet = statement.executeQuery(
                    )

                    if (resultSet.next()) {
                        call.respond(
                            FuncionariosResponse(
                                idFunc = resultSet.getInt("id_func"),
                                nome = resultSet.getString("nomeCompleto"),
                                cargo = resultSet.getString("cargo")
                            )
                        )
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Funcionário não encontrado")
                    }
                } finally {
                    connection.close()
                }

            } catch (e: Exception) {
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain)
            }
        }
    }
}