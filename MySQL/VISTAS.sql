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