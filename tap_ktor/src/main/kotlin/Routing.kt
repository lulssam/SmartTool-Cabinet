package com.tapktor

import com.tapktor.dtos.ArmariosDTO
import com.tapktor.dtos.FerramentaDTO
import com.tapktor.dtos.FuncionariosDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.DriverManager

/**
 * Função principal das calls da API
 *
 * mudar user e password consoante a pessoa a correr*/
fun Application.configureRouting() {

    val USER = "root"
    val PASSWORD = "rootpass"

    routing {

        // devolve vista das ferramentas
        get("/api/ferramentas") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            val lista = mutableListOf<FerramentaDTO>()

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
                            FerramentaDTO(
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
            val user = USER
            val password = PASSWORD

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
                            FuncionariosDTO(
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

        // devolve lista de armários com estado
        get("/api/armarios") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            val listaArmarios = mutableListOf<ArmariosDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {
                    val sql = "SELECT nArmario, capacidade, estado FROM armario"
                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery(sql)

                    while (resultSet.next()) {
                        listaArmarios.add(
                            ArmariosDTO(
                                nArmario = resultSet.getInt("nArmario"),
                                capacidade = resultSet.getInt("capacidade"),
                                estado = resultSet.getString("estado")
                            )
                        )
                    }
                } finally {
                    connection.close()
                }
                call.respond(listaArmarios)
            } catch (e: Exception) {
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain)
            }
        }
    }
}