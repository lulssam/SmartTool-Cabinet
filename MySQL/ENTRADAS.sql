USE SMARTTOOL;

-- ------------------------------------------------------------
-- 1. FUNCIONARIOS
-- ------------------------------------------------------------
INSERT INTO funcionario (id_func, nomeCompleto, email) VALUES
  (1, 'Joao Silva',      'joao.silva@tap.pt'),
  (2, 'Maria Santos',    'maria.santos@tap.pt'),
  (3, 'Pedro Costa',     'pedro.costa@tap.pt'),
  (4, 'Ana Ferreira',    'ana.ferreira@tap.pt'),
  (5, 'Rui Almeida',     'rui.almeida@tap.pt'),
  (6, 'Carlos Mendes',   'carlos.mendes@tap.pt'),
  (7, 'Sofia Rodrigues', 'sofia.rodrigues@tap.pt');

-- ------------------------------------------------------------
-- 2. PAPEIS (subtipos de funcionario)
-- ------------------------------------------------------------
INSERT INTO gestor (id_func) VALUES
  (1),
  (2);

INSERT INTO tecnico (id_func) VALUES
  (3),
  (4),
  (5);

INSERT INTO backoffice (id_func) VALUES
  (6),
  (7);

-- ------------------------------------------------------------
-- 3. ARMAZENS
-- ------------------------------------------------------------
INSERT INTO armazem (id_Armazem) VALUES
  (1),
  (2);

-- ------------------------------------------------------------
-- 4. ARMARIOS (cada um pode pertencer a um gestor)
-- ------------------------------------------------------------
INSERT INTO armario (nArmario, capacidade, estado, id_gestor) VALUES
  (101, 20, 'Operacional', 1),
  (102, 20, 'Operacional', 1),
  (103, 15, 'Avariado',    2),
  (201, 30, 'Operacional', 2);

-- ------------------------------------------------------------
-- 5. TIPOS DE FERRAMENTA
-- ------------------------------------------------------------
INSERT INTO tipo_ferramenta (codigo, nome, categoria, descricao) VALUES
  (1, 'Chave de Fendas',       'Manual',     'Chave de fendas de cabeca plana'),
  (2, 'Chave Inglesa',         'Manual',     'Chave ajustavel para porcas e parafusos'),
  (3, 'Alicate Universal',     'Manual',     'Alicate de uso geral'),
  (4, 'Berbequim Pneumatico',  'Pneumatica', 'Berbequim alimentado a ar comprimido'),
  (5, 'Chave Dinamometrica',   'Medicao',    'Aperto controlado com binario definido'),
  (6, 'Multimetro Digital',    'Eletrica',   'Medicao de tensao, corrente e resistencia');

-- ------------------------------------------------------------
-- 6. FERRAMENTAS
--    NOTA: idFerramenta e uma coluna GERADA (codigo_tipo*100000 + nFerramenta)
--          -> NUNCA inserir idFerramenta manualmente.
--    Coerencia: ferramenta 'Requisitada' tem de estar numa requisicao em aberto.
-- ------------------------------------------------------------
INSERT INTO ferramenta
  (codigo_tipo, nFerramenta, estado, disponibilidade, nArmario, id_Armazem, nome_tipo) VALUES
  -- Tipo 1 - Chave de Fendas
  (1, 1, 'Operacional', 'Disponivel',     101, NULL, 'Chave de Fendas'),
  (1, 2, 'Operacional', 'Requisitada',    101, NULL, 'Chave de Fendas'),
  (1, 3, 'Operacional', 'Requisitada',    102, NULL, 'Chave de Fendas'),
  -- Tipo 2 - Chave Inglesa
  (2, 1, 'Operacional', 'Disponivel',     101, NULL, 'Chave Inglesa'),
  (2, 2, 'Danificada',  'Em Manutencao',  NULL, 1,    'Chave Inglesa'),
  -- Tipo 3 - Alicate Universal
  (3, 1, 'Operacional', 'Requisitada',    102, NULL, 'Alicate Universal'),
  (3, 2, 'Operacional', 'Disponivel',     102, NULL, 'Alicate Universal'),
  -- Tipo 4 - Berbequim Pneumatico
  (4, 1, 'Operacional', 'Disponivel',     201, NULL, 'Berbequim Pneumatico'),
  (4, 2, 'Abatida',     'Indisponivel',   NULL, 2,    'Berbequim Pneumatico'),
  -- Tipo 5 - Chave Dinamometrica
  (5, 1, 'Operacional', 'Requisitada',    201, NULL, 'Chave Dinamometrica'),
  -- Tipo 6 - Multimetro Digital
  (6, 1, 'Operacional', 'Disponivel',     NULL, 1,    'Multimetro Digital');

-- ------------------------------------------------------------
-- 7. REQUISICOES
--    Req em aberto -> dhDevolucao = NULL (ferramentas estao 'Requisitada')
--    Req fechada   -> dhDevolucao preenchida (ferramentas voltaram a 'Disponivel')
-- ------------------------------------------------------------
INSERT INTO requisicao (idRequisicao, dhRequisicao, dhDevolucao, id_tecnico) VALUES
  (1, '2026-06-09 09:15:00', '2026-06-09 17:30:00', 3),  -- fechada (Pedro)
  (2, '2026-06-12 08:00:00', NULL,                  4),  -- aberta  (Ana)
  (3, '2026-06-15 14:20:00', NULL,                  5),  -- aberta  (Rui)
  (4, '2026-06-16 07:45:00', NULL,                  3);  -- aberta  (Pedro, hoje)

-- ------------------------------------------------------------
-- 8. FERRAMENTAS POR REQUISICAO
-- ------------------------------------------------------------
INSERT INTO requisicao_ferramenta (idRequisicao, codigo_tipo, nFerramenta) VALUES
  -- Req 1 (fechada) -> ferramentas ja devolvidas (Disponivel)
  (1, 1, 1),
  (1, 2, 1),
  -- Req 2 (aberta) -> Ana tem estas em uso
  (2, 1, 2),
  (2, 3, 1),
  -- Req 3 (aberta) -> Rui tem esta em uso
  (3, 5, 1),
  -- Req 4 (aberta) -> Pedro tem esta em uso
  (4, 1, 3);