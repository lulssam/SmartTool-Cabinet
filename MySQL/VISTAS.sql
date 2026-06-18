USE SMARTTOOL;

-- View usada pelo endpoint GET /api/ferramentas (junta ferramenta + tipo_ferramenta)
CREATE VIEW View_Inventario_Detalhado AS
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
CREATE VIEW View_Mapa_Emprestimos AS
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
