USE SMARTTOOL;

-- View usada pelo endpoint GET /api/ferramentas (junta ferramenta + tipo_ferramenta)
CREATE OR REPLACE VIEW View_Inventario_Detalhado AS
SELECT  f.idFerramenta,
        f.nome_tipo        AS Nome_Tipo,
        tf.categoria       AS categoria,
        f.estado,
        f.disponibilidade,
        f.nArmario         AS Armario
FROM    ferramenta f
JOIN    tipo_ferramenta tf ON f.codigo_tipo = tf.codigo;


CREATE OR REPLACE VIEW View_Email AS
SELECT f.id_func, 
       f.nomeCompleto, 
       f.email, 
       f.turno,
       f.ativo, -- Adicionada a coluna para controlar o Soft Delete
       CASE
         WHEN g.id_func IS NOT NULL THEN 'GESTOR'
         WHEN t.id_func IS NOT NULL THEN 'TECNICO'
         WHEN b.id_func IS NOT NULL THEN 'BACKOFFICE'
       END AS cargo
FROM funcionario f
LEFT JOIN gestor     g ON f.id_func = g.id_func
LEFT JOIN tecnico    t ON f.id_func = t.id_func
LEFT JOIN backoffice b ON f.id_func = b.id_func; 

-- View usada pelo endpoint GET /api/historico
-- não filtrei já aqui pela data para isto puder ser usado noutras queries
CREATE OR REPLACE VIEW View_Mapa_Emprestimos AS
SELECT  r.idRequisicao,
        f.nomeCompleto      AS tecnico,
        fe.idFerramenta,
        fe.nome_tipo        AS ferramenta,
        r.dhRequisicao,
        r.dhDevolucao
FROM    requisicao r
JOIN    funcionario f          ON r.id_tecnico   = f.id_func
JOIN    requisicao_ferramenta rf ON r.idRequisicao = rf.idRequisicao
JOIN    ferramenta fe          ON rf.codigo_tipo = fe.codigo_tipo
                              AND rf.nFerramenta = fe.nFerramenta;
                              
                              
-- View usada pelo endpoint das ferramentas que um técnico tem em uso (ainda não devolvidas)
CREATE OR REPLACE VIEW View_Ferramentas_Tecnico AS
SELECT  r.idRequisicao,
        r.id_tecnico,
        fe.idFerramenta,
        fe.nome_tipo        AS nome,
        fe.estado,
        fe.disponibilidade,
        tf.categoria        AS categoria,
        fe.nArmario         AS Armario
FROM    requisicao r
JOIN    requisicao_ferramenta rf ON r.idRequisicao  = rf.idRequisicao
JOIN    ferramenta fe            ON rf.codigo_tipo  = fe.codigo_tipo
                                AND rf.nFerramenta  = fe.nFerramenta
JOIN    tipo_ferramenta tf       ON fe.codigo_tipo  = tf.codigo
WHERE   r.dhDevolucao IS NULL;

-- View usada pelo endpoint GET /api/tarefas
CREATE OR REPLACE VIEW View_Tarefas_Detalhada AS
SELECT  t.idTarefa, t.id_tecnico, t.titulo, t.descricao, t.estado, t.prioridade, t.dhAtribuicao,
        f.nomeCompleto AS tecnico,
        fe.nome_tipo   AS ferramenta
FROM    tarefa t
JOIN    funcionario f ON t.id_tecnico = f.id_func
LEFT JOIN tarefa_ferramenta_permitida tfp ON t.idTarefa = tfp.idTarefa
LEFT JOIN ferramenta fe ON tfp.codigo_tipo = fe.codigo_tipo AND tfp.nFerramenta = fe.nFerramenta;

-- View usada pelo endpoint GET /api/tecnicos
CREATE OR REPLACE VIEW View_Tecnicos_Disponibilidade AS
SELECT  f.id_func      AS id,
        f.nomeCompleto AS nome,
        f.turno,
        NOT EXISTS (
            SELECT 1 FROM tarefa t
            WHERE t.id_tecnico = f.id_func AND t.estado <> 'CONCLUIDA'
        ) AS disponivel
FROM    tecnico tc
JOIN    funcionario f ON tc.id_func = f.id_func
WHERE   f.ativo = TRUE;


CREATE OR REPLACE VIEW View_Ferramentas_Reservadas_Tecnico AS
SELECT  t.id_tecnico,
        fe.idFerramenta,
        fe.nome_tipo    AS nome,
        tf.categoria    AS categoria,
        fe.estado,
        fe.disponibilidade,
        fe.nArmario     AS Armario
FROM    tarefa t
JOIN    tarefa_ferramenta_permitida tfp ON t.idTarefa = tfp.idTarefa
JOIN    ferramenta fe ON tfp.codigo_tipo = fe.codigo_tipo AND tfp.nFerramenta = fe.nFerramenta
JOIN    tipo_ferramenta tf ON fe.codigo_tipo = tf.codigo
WHERE   t.estado <> 'CONCLUIDA';

