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


-- View usada pelo endpoint GET /api/funcionarios/{email}
CREATE VIEW View_Email AS
SELECT f.id_func, f.nomeCompleto, f.email,      
       CASE
         WHEN g.id_func IS NOT NULL THEN 'GESTOR'
         WHEN t.id_func IS NOT NULL THEN 'TECNICO'
         WHEN b.id_func IS NOT NULL THEN 'BACKOFFICE'
       END AS cargo
FROM funcionario f
LEFT JOIN gestor     g ON f.id_func = g.id_func
LEFT JOIN tecnico    t ON f.id_func = t.id_func
LEFT JOIN backoffice b ON f.id_func = b.id_func;  