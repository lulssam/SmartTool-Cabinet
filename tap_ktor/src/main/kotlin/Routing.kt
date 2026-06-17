package com.tapktor

import com.tapktor.dtos.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.DriverManager
import java.sql.Statement

/**
 * Função principal das calls da API
 *
 * mudar user e password consoante a pessoa a correr*/
fun Application.configureRouting() {

    val USER = "root"
    val PASSWORD = "rootpass"

    routing {

        // ====== GETS ======
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
                    val resultSet = statement.executeQuery()

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

        // devolve ferramentas por armário (estado + disponibilidade)
        get("/api/armarios/{id}/ferramentas") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            val listaFerramentasArmarios = mutableListOf<FerramentaDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "id do armário inválido")
                        return@get
                    }
                    val sql =
                        "SELECT idFerramenta, Nome_Tipo, categoria, estado, disponibilidade, Armario " +
                                "FROM View_Inventario_Detalhado " +
                                "WHERE Armario = ?"
                    val statement = connection.prepareStatement(sql)
                    statement.setInt(1, id)
                    val resultSet = statement.executeQuery()

                    while (resultSet.next()) {
                        listaFerramentasArmarios.add(
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
                call.respond(listaFerramentasArmarios)
            } catch (e: Exception) {
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain)
            }
        }

        // requisições dos últimos 7 dias (usa View_Mapa_Emprestimos)
        // todo ver se eventualmente fazemos com 30 dias por ex
        get("/api/historico") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            val listaHistorico = mutableListOf<HistoricoDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {
                    val sql =
                        "SELECT idRequisicao, tecnico, idFerramenta, ferramenta, dhRequisicao, dhDevolucao " +
                                "FROM View_Mapa_Emprestimos " +
                                "WHERE dhRequisicao >= CURDATE() - INTERVAL 7 DAY"
                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery(sql)

                    while (resultSet.next()) {
                        listaHistorico.add(
                            HistoricoDTO(
                                idRequisicao = resultSet.getInt("idRequisicao"),
                                nomeFuncionario = resultSet.getString("tecnico"),
                                idFerramenta = resultSet.getInt("idFerramenta"),
                                nomeFerramenta = resultSet.getString("ferramenta"),
                                dhRequisicao = resultSet.getString("dhRequisicao"),
                                dhDevolucao = resultSet.getString("dhDevolucao")
                            )
                        )
                    }
                } finally {
                    connection.close()
                }

                call.respond(listaHistorico)
            } catch (e: Exception) {
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain)
            }
        }

        // ====== POSTS ======
        // técnico requisita ferramenta
        post("/api/requisicoes") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)
                connection.autoCommit = false

                try {
                    val pedido = call.receive<NovaRequisicaoDTO>()
                    val sqlRequisicao = "INSERT INTO requisicao (dhRequisicao, id_tecnico) VALUES (NOW(), ?)"
                    val statement = connection.prepareStatement(sqlRequisicao, Statement.RETURN_GENERATED_KEYS)
                    statement.setInt(1, pedido.idTecnico)
                    statement.executeUpdate()

                    val keys = statement.generatedKeys
                    keys.next()
                    val idRequisicao = keys.getInt(1)

                    val sqlFerramenta = "INSERT INTO requisicao_ferramenta (idRequisicao, codigo_tipo, nFerramenta) " +
                            "VALUES (?, ?, ?)"

                    val statementFerramenta = connection.prepareStatement(sqlFerramenta)
                    statementFerramenta.setInt(1, idRequisicao)
                    statementFerramenta.setInt(2, pedido.codigoTipo)
                    statementFerramenta.setInt(3, pedido.nFerramenta)
                    statementFerramenta.executeUpdate()

                    connection.commit()

                    call.respond(HttpStatusCode.Created, "Requisição $idRequisicao criada")
                } catch (e: Exception) {
                    connection.rollback() // se alguma coisa falhar, desfaz tudo
                    throw e
                } finally {
                    connection.close()
                }
            } catch (e: Exception) {
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain)
            }
        }

        // ====== PATCH ======
        // tecnico devolve ferramenta
        patch("/api/requisicoes/{id}/devolver") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {
                    val id = call.parameters["id"]?.toIntOrNull()

                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "id da requisição inválido")
                        return@patch
                    }

                    val sql = "UPDATE requisicao SET dhDevolucao = NOW() WHERE idRequisicao = ?"
                    val statement = connection.prepareStatement(sql)
                    statement.setInt(1, id)

                    val linhasAfetadas = statement.executeUpdate()
                    if (linhasAfetadas == 0) {
                        // nenhuma linha mudou -> o id não existe
                        call.respond(HttpStatusCode.NotFound, "Requisição $id não encontrada")
                    } else {
                        call.respond(HttpStatusCode.OK, "Requisição $id devolvida")
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