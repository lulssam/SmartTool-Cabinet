package com.tapktor

import com.tapktor.dtos.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.DriverManager
import java.sql.Statement

fun Application.configureRouting() {

    val USER = "root"
    val PASSWORD = "rootpass"

    routing {

        // ====== GETS ======
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

        get("/api/funcionarios/{email}") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)
                try {
                    val email = call.parameters["email"]
                    val sql = "SELECT id_func, nomeCompleto, email, cargo, turno, ativo FROM View_Email WHERE email = ?"
                    val statement = connection.prepareStatement(sql)
                    statement.setString(1, email)
                    val resultSet = statement.executeQuery()

                    if (resultSet.next()) {
                        call.respond(
                            FuncionariosDTO(
                                idFunc = resultSet.getInt("id_func"),
                                nome = resultSet.getString("nomeCompleto"),
                                cargo = resultSet.getString("cargo"),
                                turno = resultSet.getString("turno"),
                                ativo = resultSet.getBoolean("ativo")
                            )
                        )
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Funcionário não encontrado")
                    }
                } finally {
                    connection.close()
                }

            } catch (e: Exception) {
                call.respondText(
                    "Erro na DB: ${e.message}",
                    ContentType.Text.Plain,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        get("/api/armarios") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            val listaArmarios = mutableListOf<ArmariosDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {
                    val sql = "SELECT nArmario, capacidade, estado, trancado FROM armario"
                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery(sql)

                    while (resultSet.next()) {
                        listaArmarios.add(
                            ArmariosDTO(
                                nArmario = resultSet.getInt("nArmario"),
                                capacidade = resultSet.getInt("capacidade"),
                                estado = resultSet.getString("estado"),
                                trancado = resultSet.getBoolean("trancado")
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

        get("/api/alertas") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            val listaAlertas = mutableListOf<AlertasDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {

                    val stmtFerramentas = connection.createStatement()
                    val rsFerramentas = stmtFerramentas.executeQuery(
                        "SELECT idFerramenta, nome_tipo FROM ferramenta WHERE disponibilidade = 'Requisitada'"
                    )
                    while (rsFerramentas.next()) {
                        listaAlertas.add(
                            AlertasDTO(
                                tipo = "FERRAMENTA_EM_FALTA",
                                descricao = "Ferramenta '${rsFerramentas.getString("nome_tipo")}' não devolvida",
                                referencia = rsFerramentas.getInt("idFerramenta").toString()
                            )
                        )
                    }

                    val stmtArmarios = connection.createStatement()
                    val rsArmarios = stmtArmarios.executeQuery(
                        "SELECT nArmario FROM armario WHERE trancado = FALSE"
                    )
                    while (rsArmarios.next()) {
                        listaAlertas.add(
                            AlertasDTO(
                                tipo = "ARMARIO_DESTRANCADO",
                                descricao = "Armário ${rsArmarios.getInt("nArmario")} está destrancado",
                                referencia = rsArmarios.getInt("nArmario").toString()
                            )
                        )
                    }
                } finally {
                    connection.close()
                }

                call.respond(listaAlertas)
            } catch (e: Exception) {
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain)
            }
        }

        get("/api/tecnicos/{id}/ferramentas") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"

            val idTecnico = call.parameters["id"]?.toIntOrNull()
            if (idTecnico == null) {
                call.respond(HttpStatusCode.BadRequest, "id inválido")
                return@get
            }

            val lista = mutableListOf<FerramentaDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, USER, PASSWORD)
                try {
                    val sql = "SELECT idRequisicao, idFerramenta, nome, categoria, estado, disponibilidade, Armario " +
                            "FROM View_Ferramentas_Tecnico WHERE id_tecnico = ?"
                    val statement = connection.prepareStatement(sql)
                    statement.setInt(1, idTecnico)
                    val resultSet = statement.executeQuery()

                    while (resultSet.next()) {
                        lista.add(
                            FerramentaDTO(
                                idRequisicao = resultSet.getInt("idRequisicao"),
                                idFerramenta = resultSet.getInt("idFerramenta"),
                                nome = resultSet.getString("nome"),
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

        // ====== POSTS ======
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

                    val sqlValidacao = """
                        SELECT 1 
                        FROM tarefa t
                        JOIN tarefa_ferramenta_permitida tfp ON t.idTarefa = tfp.idTarefa
                        WHERE t.id_tecnico = ? 
                          AND tfp.codigo_tipo = ? 
                          AND tfp.nFerramenta = ?
                          AND t.estado != 'CONCLUIDA'
                    """.trimIndent()

                    val stmtValidacao = connection.prepareStatement(sqlValidacao)
                    stmtValidacao.setInt(1, pedido.idTecnico)
                    stmtValidacao.setInt(2, pedido.codigoTipo)
                    stmtValidacao.setInt(3, pedido.nFerramenta)

                    val rsValidacao = stmtValidacao.executeQuery()

                    if (!rsValidacao.next()) {
                        call.respond(
                            HttpStatusCode.Forbidden,
                            "Acesso Negado: Não tens nenhuma tarefa ativa que te permita levantar esta ferramenta."
                        )
                        return@post
                    }

                    val sqlRequisicao = "INSERT INTO requisicao (dhRequisicao, id_tecnico) VALUES (NOW(), ?)"
                    val statement = connection.prepareStatement(sqlRequisicao, Statement.RETURN_GENERATED_KEYS)
                    statement.setInt(1, pedido.idTecnico)
                    statement.executeUpdate()

                    val keys = statement.generatedKeys
                    if (!keys.next()) throw Exception("Erro ao gerar ID da requisição")
                    val idRequisicao = keys.getInt(1)

                    val sqlFerramenta =
                        "INSERT INTO requisicao_ferramenta (idRequisicao, codigo_tipo, nFerramenta) VALUES (?, ?, ?)"
                    val statementFerramenta = connection.prepareStatement(sqlFerramenta)
                    statementFerramenta.setInt(1, idRequisicao)
                    statementFerramenta.setInt(2, pedido.codigoTipo)
                    statementFerramenta.setInt(3, pedido.nFerramenta)
                    statementFerramenta.executeUpdate()

                    val sqlUpdateFerramenta =
                        "UPDATE ferramenta SET disponibilidade = 'Requisitada' WHERE codigo_tipo = ? AND nFerramenta = ?"
                    val stmtUpdate = connection.prepareStatement(sqlUpdateFerramenta)
                    stmtUpdate.setInt(1, pedido.codigoTipo)
                    stmtUpdate.setInt(2, pedido.nFerramenta)
                    stmtUpdate.executeUpdate()

                    connection.commit()

                    call.respond(HttpStatusCode.Created, "Requisição $idRequisicao criada com sucesso.")

                } catch (e: Exception) {
                    connection.rollback()
                    throw e
                } finally {
                    connection.close()
                }
            } catch (e: Exception) {
                call.respondText(
                    "Erro na DB: ${e.message}",
                    ContentType.Text.Plain,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        post("/api/tarefas") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                connection.autoCommit = false

                try {
                    val pedido = call.receive<NovaTarefaDTO>()

                    val sqlTarefa =
                        "INSERT INTO tarefa (descricao, id_gestor, id_tecnico, dhAtribuicao) VALUES (?, ?, ?, NOW())"
                    val statementTarefa = connection.prepareStatement(sqlTarefa, Statement.RETURN_GENERATED_KEYS)
                    statementTarefa.setString(1, pedido.descricao)
                    statementTarefa.setInt(2, pedido.idGestor)
                    statementTarefa.setInt(3, pedido.idTecnico)
                    statementTarefa.executeUpdate()

                    val keys = statementTarefa.generatedKeys
                    if (!keys.next()) {
                        throw Exception("Falha ao obter o ID da nova tarefa.")
                    }
                    val idTarefaGerada = keys.getInt(1)

                    if (pedido.ferramentasPermitidasIds.isNotEmpty()) {
                        val sqlFerramenta =
                            "INSERT INTO tarefa_ferramenta_permitida (idTarefa, codigo_tipo, nFerramenta) VALUES (?, ?, ?)"
                        val statementFerramenta = connection.prepareStatement(sqlFerramenta)

                        for (ferramenta in pedido.ferramentasPermitidasIds) {
                            statementFerramenta.setInt(1, idTarefaGerada)
                            statementFerramenta.setInt(2, ferramenta.codigoTipo)
                            statementFerramenta.setInt(3, ferramenta.nFerramenta)
                            statementFerramenta.addBatch()
                        }
                        statementFerramenta.executeBatch()
                    }

                    connection.commit()

                    call.respond(
                        HttpStatusCode.Created,
                        "Tarefa $idTarefaGerada atribuída ao técnico ${pedido.idTecnico} com sucesso."
                    )

                } catch (e: Exception) {
                    connection.rollback()
                    throw e
                } finally {
                    connection.close()
                }
            } catch (e: Exception) {
                call.respondText(
                    "Erro na DB: ${e.message}",
                    ContentType.Text.Plain,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        // ====== PATCH ======
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

        patch("/api/ferramentas/{id}/estado") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "id da ferramenta inválido")
                        return@patch
                    }

                    val pedido = call.receive<EstadoFerramentaDTO>()

                    val sqlDispo = "SELECT disponibilidade FROM ferramenta WHERE idFerramenta = ?"
                    val statementDisp = connection.prepareStatement(sqlDispo)
                    statementDisp.setInt(1, id)

                    val resultSet = statementDisp.executeQuery()

                    if (!resultSet.next()) {
                        call.respond(HttpStatusCode.NotFound, "Ferramenta com o id: $id não encontrada.")
                        return@patch
                    }

                    val disponibilidade = resultSet.getString("disponibilidade")
                    if (disponibilidade != "Requisitada") {
                        call.respond(
                            HttpStatusCode.Conflict,
                            "Só é possível mudar o estado de uma ferramenta requisitada (está: $disponibilidade)."
                        )
                        return@patch
                    }

                    val sql = "UPDATE ferramenta SET estado = ? WHERE idFerramenta = ?"
                    val statement = connection.prepareStatement(sql)
                    statement.setString(1, pedido.estado)
                    statement.setInt(2, id)
                    statement.executeUpdate()

                    call.respond(HttpStatusCode.OK, "Ferramenta com o id: $id teve o seu estado atualizado.")

                } finally {
                    connection.close()
                }
            } catch (e: Exception) {
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain)
            }
        }

        patch("/api/funcionarios/{id}/cargo") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)
                connection.autoCommit = false

                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "id do funcionário inválido")
                        return@patch
                    }

                    val pedido = call.receive<NovoCargoDTO>()
                    val novoCargo = pedido.cargo.uppercase()


                    if (novoCargo !in listOf("GESTOR", "TECNICO", "BACKOFFICE")) {
                        call.respond(HttpStatusCode.BadRequest, "Cargo inválido. Usa GESTOR, TECNICO ou BACKOFFICE.")
                        return@patch
                    }

                    val sqlCheck = "SELECT id_func FROM funcionario WHERE id_func = ?"
                    val stmtCheck = connection.prepareStatement(sqlCheck)
                    stmtCheck.setInt(1, id)
                    if (!stmtCheck.executeQuery().next()) {
                        call.respond(HttpStatusCode.NotFound, "Funcionário $id não encontrado.")
                        return@patch
                    }

                    val deleteGestor = connection.prepareStatement("DELETE FROM gestor WHERE id_func = ?")
                    deleteGestor.setInt(1, id)
                    deleteGestor.executeUpdate()

                    val deleteTecnico = connection.prepareStatement("DELETE FROM tecnico WHERE id_func = ?")
                    deleteTecnico.setInt(1, id)
                    deleteTecnico.executeUpdate()

                    val deleteBackoffice = connection.prepareStatement("DELETE FROM backoffice WHERE id_func = ?")
                    deleteBackoffice.setInt(1, id)
                    deleteBackoffice.executeUpdate()

                    val sqlInsert = when (novoCargo) {
                        "GESTOR" -> "INSERT INTO gestor (id_func) VALUES (?)"
                        "TECNICO" -> "INSERT INTO tecnico (id_func) VALUES (?)"
                        "BACKOFFICE" -> "INSERT INTO backoffice (id_func) VALUES (?)"
                        else -> throw IllegalArgumentException("Cargo não suportado")
                    }

                    val stmtInsert = connection.prepareStatement(sqlInsert)
                    stmtInsert.setInt(1, id)
                    stmtInsert.executeUpdate()

                    connection.commit()

                    call.respond(HttpStatusCode.OK, "Cargo do funcionário $id atualizado com sucesso para $novoCargo.")

                } catch (e: Exception) {
                    connection.rollback()
                    throw e
                } finally {
                    connection.close()
                }
            } catch (e: Exception) {
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain)
            }
        }

        patch("/api/funcionarios/{id}/turno") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "id do funcionário inválido")
                        return@patch
                    }

                    val pedido = call.receive<NovoTurnoDTO>()
                    val novoTurno = pedido.turno.uppercase()


                    if (novoTurno !in listOf("MANHA", "TARDE", "NOITE")) {
                        call.respond(HttpStatusCode.BadRequest, "Turno inválido. Usa MANHA, TARDE ou NOITE.")
                        return@patch
                    }

                    val sql = "UPDATE funcionario SET turno = ? WHERE id_func = ?"
                    val statement = connection.prepareStatement(sql)
                    statement.setString(1, novoTurno)
                    statement.setInt(2, id)

                    val linhasAfetadas = statement.executeUpdate()

                    if (linhasAfetadas == 0) {
                        call.respond(HttpStatusCode.NotFound, "Funcionário $id não encontrado.")
                    } else {
                        call.respond(HttpStatusCode.OK, "Turno do funcionário $id atualizado para $novoTurno.")
                    }

                } finally {
                    connection.close()
                }
            } catch (e: Exception) {
                call.respondText(
                    "Erro na DB: ${e.message}",
                    ContentType.Text.Plain,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        get("/api/ferramentas/em-falta") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            val listaEmFalta = mutableListOf<FerramentaEmFaltaDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {

                    val sql = """
                        SELECT idFerramenta, ferramenta, tecnico, dhRequisicao 
                        FROM View_Mapa_Emprestimos 
                        WHERE dhDevolucao IS NULL
                    """.trimIndent()

                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery(sql)

                    while (resultSet.next()) {
                        listaEmFalta.add(
                            FerramentaEmFaltaDTO(
                                idFerramenta = resultSet.getInt("idFerramenta"),
                                nomeFerramenta = resultSet.getString("ferramenta"),
                                detentor = resultSet.getString("tecnico"),
                                dataRequisicao = resultSet.getString("dhRequisicao") ?: "Data desconhecida"
                            )
                        )
                    }
                } finally {
                    connection.close()
                }

                call.respond(listaEmFalta)

            } catch (e: Exception) {
                call.respondText(
                    "Erro na DB: ${e.message}",
                    ContentType.Text.Plain,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        post("/api/funcionarios") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)
                connection.autoCommit = false

                try {
                    val pedido = call.receive<NovoFuncionarioDTO>()
                    val cargoFormatado = pedido.cargo.uppercase()

                    if (cargoFormatado !in listOf("GESTOR", "TECNICO", "BACKOFFICE")) {
                        call.respond(HttpStatusCode.BadRequest, "Cargo inválido.")
                        return@post
                    }

                    val sqlFunc = "INSERT INTO funcionario (nomeCompleto, email, turno) VALUES (?, ?, ?)"
                    val stmtFunc = connection.prepareStatement(sqlFunc, Statement.RETURN_GENERATED_KEYS)
                    stmtFunc.setString(1, pedido.nomeCompleto)
                    stmtFunc.setString(2, pedido.email)
                    stmtFunc.setString(3, pedido.turno.uppercase())
                    stmtFunc.executeUpdate()

                    val keys = stmtFunc.generatedKeys
                    if (!keys.next()) throw Exception("Falha ao obter o ID do funcionário.")
                    val idFuncGerado = keys.getInt(1)

                    val sqlCargo = when (cargoFormatado) {
                        "GESTOR" -> "INSERT INTO gestor (id_func) VALUES (?)"
                        "TECNICO" -> "INSERT INTO tecnico (id_func) VALUES (?)"
                        "BACKOFFICE" -> "INSERT INTO backoffice (id_func) VALUES (?)"
                        else -> throw Exception("Erro no mapeamento do cargo.")
                    }

                    val stmtCargo = connection.prepareStatement(sqlCargo)
                    stmtCargo.setInt(1, idFuncGerado)
                    stmtCargo.executeUpdate()

                    connection.commit()
                    call.respond(HttpStatusCode.Created, "Funcionário $idFuncGerado criado com sucesso.")

                } catch (e: Exception) {
                    connection.rollback()
                    throw e
                } finally {
                    connection.close()
                }
            } catch (e: Exception) {
                call.respondText(
                    "Erro na DB: ${e.message}",
                    ContentType.Text.Plain,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        patch("/api/funcionarios/{id}/desativar") {
            val url = "jdbc:mysql://localhost:3306/smarttool?useSSL=false&allowPublicKeyRetrieval=true"
            val user = USER
            val password = PASSWORD

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(url, user, password)

                try {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                        return@patch
                    }

                    val sql = "UPDATE funcionario SET ativo = FALSE WHERE id_func = ?"
                    val statement = connection.prepareStatement(sql)
                    statement.setInt(1, id)

                    val linhas = statement.executeUpdate()
                    if (linhas == 0) {
                        call.respond(HttpStatusCode.NotFound, "Funcionário não encontrado.")
                    } else {
                        call.respond(
                            HttpStatusCode.OK,
                            "Funcionário $id desativado com sucesso (mantido no histórico)."
                        )
                    }

                } finally {
                    connection.close()
                }
            } catch (e: Exception) {
                call.respondText(
                    "Erro na DB: ${e.message}",
                    ContentType.Text.Plain,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }
}