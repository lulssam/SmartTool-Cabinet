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

    val URL = environment.config.property("storage.jdbcUrl").getString()
    val USER = environment.config.property("storage.user").getString()
    val PASSWORD = environment.config.property("storage.password").getString()

    routing {

        // ====== GETS ======
        get("/api/ferramentas") {
            val lista = mutableListOf<FerramentaDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)
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
        get("/api/funcionarios") {
            val lista = mutableListOf<FuncionariosDTO>()
            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)
                try {
                    // Vai buscar todos os registos sem filtrar por email
                    val sql = "SELECT id_func, nomeCompleto, email, cargo, turno, ativo FROM View_Email"
                    val statement = connection.createStatement()
                    val resultSet = statement.executeQuery(sql)

                    while (resultSet.next()) {
                        lista.add(
                            FuncionariosDTO(
                                idFunc = resultSet.getInt("id_func"),
                                nome = resultSet.getString("nomeCompleto"),
                                cargo = resultSet.getString("cargo"),
                                turno = resultSet.getString("turno"),
                                ativo = resultSet.getBoolean("ativo")
                            )
                        )
                    }
                } finally {
                    connection.close()
                }
                call.respond(lista)
            } catch (e: Exception) {
                call.respondText(
                    "Erro na DB: ${e.message}",
                    ContentType.Text.Plain,
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        get("/api/funcionarios/{email}") {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)
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

        get("/api/historico/tecnico/{id}") {
            val idTecnico = call.parameters["id"]?.toIntOrNull()
            if (idTecnico == null) {
                call.respond(HttpStatusCode.BadRequest, "ID do técnico inválido")
                return@get
            }

            val listaHistorico = mutableListOf<HistoricoDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

                try {
                    val sql = """
                        SELECT 
                            r.idRequisicao, 
                            func.nomeCompleto AS tecnico, 
                            f.idFerramenta, 
                            f.nome_tipo AS ferramenta, 
                            r.dhRequisicao, 
                            r.dhDevolucao 
                        FROM requisicao r
                        JOIN funcionario func ON r.id_tecnico = func.id_func
                        JOIN requisicao_ferramenta rf ON r.idRequisicao = rf.idRequisicao
                        JOIN ferramenta f ON rf.codigo_tipo = f.codigo_tipo AND rf.nFerramenta = f.nFerramenta
                        WHERE r.id_tecnico = ? 
                          AND r.dhRequisicao >= CURDATE() - INTERVAL 30 DAY
                        ORDER BY r.dhRequisicao DESC
                    """.trimIndent()

                    val statement = connection.prepareStatement(sql)
                    statement.setInt(1, idTecnico)
                    val resultSet = statement.executeQuery()

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
                call.respondText(
                    "Erro na DB: ${e.message}",
                    ContentType.Text.Plain,
                    HttpStatusCode.InternalServerError
                )
            }
        }

        get("/api/armarios") {

            val listaArmarios = mutableListOf<ArmariosDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

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

            val listaFerramentasArmarios = mutableListOf<FerramentaDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

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

            val listaHistorico = mutableListOf<HistoricoDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

                try {
                    val sql =
                        "SELECT idRequisicao, tecnico, idFerramenta, ferramenta, dhRequisicao, dhDevolucao " +
                                "FROM View_Mapa_Emprestimos " +
                                "WHERE dhRequisicao >= CURDATE() - INTERVAL 30 DAY"
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

            val listaAlertas = mutableListOf<AlertasDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

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
            val idTecnico = call.parameters["id"]?.toIntOrNull()
            if (idTecnico == null) {
                call.respond(HttpStatusCode.BadRequest, "id inválido")
                return@get
            }

            val lista = mutableListOf<FerramentaDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)
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

        get("/api/tarefas") {
            val porTarefa = LinkedHashMap<Int, TarefaDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

                try {
                    val sql = """
                            SELECT idTarefa, titulo ,descricao, estado, prioridade, dhAtribuicao, tecnico, ferramenta
                            FROM   View_Tarefas_Detalhada
                            ORDER BY dhAtribuicao DESC, idTarefa
                        """.trimIndent()

                    val resultSet = connection.createStatement().executeQuery(sql)

                    while (resultSet.next()) {
                        val id = resultSet.getInt("idTarefa")

                        // se for a primeira vez que a tarefa aparece -> cria o dto
                        val tarefa = porTarefa.getOrPut(id) {
                            TarefaDTO(
                                idTarefa = id,
                                titulo = resultSet.getString("titulo"),
                                descricao = resultSet.getString("descricao"),
                                tecnico = resultSet.getString("tecnico"),
                                estado = resultSet.getString("estado"),
                                prioridade = resultSet.getString("prioridade"),
                                dhAtribuicao = resultSet.getString("dhAtribuicao")
                            )
                        }

                        // cada linha trás uma ferrramenta -> acrescentar linha
                        resultSet.getString("ferramenta")?.let { tarefa.ferramentas.add(it) }
                    }
                } finally {
                    connection.close()
                }
                call.respond(porTarefa.values.toList())
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            }
        }

        get("/api/tecnicos") {
            val listaTecnicos = mutableListOf<TecnicoDTO>()
            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

                try {
                    val sql = "SELECT id, nome, turno, disponivel FROM View_Tecnicos_Disponibilidade ORDER BY nome"
                    val rs = connection.createStatement().executeQuery(sql)
                    while (rs.next()) {
                        listaTecnicos.add(
                            TecnicoDTO(
                                id = rs.getInt("id"),
                                nome = rs.getString("nome"),
                                turno = rs.getString("turno"),
                                disponivel = rs.getBoolean("disponivel")
                            )
                        )
                    }
                } finally {
                    connection.close()
                }
                call.respond(listaTecnicos)
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondText("Erro na DB: ${e.message}", ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            }
        }

        get("/api/tecnicos/{id}/reservadas") {
            val idTecnico = call.parameters["id"]?.toIntOrNull()
            if (idTecnico == null) {
                call.respond(HttpStatusCode.BadRequest, "id inválido")
                return@get
            }

            val lista = mutableListOf<FerramentaDTO>()
            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

                try {
                    val sql = "SELECT idFerramenta, nome, categoria, estado, disponibilidade, Armario " +
                            "FROM View_Ferramentas_Reservadas_Tecnico WHERE id_tecnico = ?"
                    val statement = connection.prepareStatement(sql)
                    statement.setInt(1, idTecnico)

                    val resultSet = statement.executeQuery()
                    while (resultSet.next()) {
                        lista.add(
                            FerramentaDTO(
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

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

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

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

                connection.autoCommit = false

                try {
                    val pedido = call.receive<NovaTarefaDTO>()

                    // criar tarefa
                    val sqlTarefa =
                        "INSERT INTO tarefa (titulo, descricao, id_gestor, id_tecnico, prioridade, dhAtribuicao) VALUES (?, ?, ?, ?, ?, NOW())"
                    val statementTarefa = connection.prepareStatement(sqlTarefa, Statement.RETURN_GENERATED_KEYS)
                    statementTarefa.setString(1, pedido.titulo)
                    statementTarefa.setString(2, pedido.descricao)
                    statementTarefa.setInt(3, pedido.idGestor)
                    statementTarefa.setInt(4, pedido.idTecnico)
                    statementTarefa.setString(5, pedido.prioridade)
                    statementTarefa.executeUpdate()

                    val keys = statementTarefa.generatedKeys
                    if (!keys.next()) {
                        throw Exception("Falha ao obter o ID da nova tarefa.")
                    }
                    val idTarefaGerada = keys.getInt(1)

                    // associa as ferramentas permitidas e reserva-as
                    if (pedido.ferramentasPermitidasIds.isNotEmpty()) {
                        val stmtInsert = connection.prepareStatement(
                            "INSERT INTO tarefa_ferramenta_permitida (idTarefa, codigo_tipo, nFerramenta) VALUES (?, ?, ?)"
                        )
                        // só reserva se ainda estiver 'Disponivel'
                        val stmtReserva = connection.prepareStatement(
                            "UPDATE ferramenta SET disponibilidade = 'Reservada' " +
                                    "WHERE codigo_tipo = ? AND nFerramenta = ? AND disponibilidade = 'Disponivel'"
                        )

                        for (f in pedido.ferramentasPermitidasIds) {
                            stmtReserva.setInt(1, f.codigoTipo)
                            stmtReserva.setInt(2, f.nFerramenta)
                            if (stmtReserva.executeUpdate() == 0) {
                                connection.rollback()
                                call.respond(
                                    HttpStatusCode.Conflict,
                                    "A ferramenta ${f.codigoTipo}-${f.nFerramenta} já não está disponível."
                                )
                                return@post
                            }

                            stmtInsert.setInt(1, idTarefaGerada)
                            stmtInsert.setInt(2, f.codigoTipo)
                            stmtInsert.setInt(3, f.nFerramenta)
                            stmtInsert.addBatch()
                        }
                        stmtInsert.executeBatch()
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

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

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
            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(
                    URL, USER, PASSWORD
                )

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
            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)
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

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

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

            val listaEmFalta = mutableListOf<FerramentaEmFaltaDTO>()

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

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

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)
                connection.autoCommit = false // Inicia transação

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

            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(URL, USER, PASSWORD)

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