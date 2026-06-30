USE smarttool;

-- ============================================================
-- Dados de exemplo. Referência: hoje = 2026-06-30.
-- ============================================================

-- 1. FUNCIONARIOS
INSERT INTO funcionario (id_func, nomeCompleto, email, turno) VALUES
  (1, 'Joao Silva',      'joao.silva@tap.pt', 'MANHA'),
  (2, 'Maria Santos',    'maria.santos@tap.pt', 'TARDE'),
  (3, 'Pedro Costa',     'pedro.costa@tap.pt', 'MANHA'),
  (4, 'Ana Ferreira',    'ana.ferreira@tap.pt', 'TARDE'),
  (5, 'Rui Almeida',     'rui.almeida@tap.pt', 'NOITE'),
  (6, 'Carlos Mendes',   'carlos.mendes@tap.pt', 'MANHA'),
  (7, 'Sofia Rodrigues', 'sofia.rodrigues@tap.pt', 'TARDE'),
  (8, 'Luísa Sampaio',   'lumelosampaio@gmail.com', 'NOITE'),
  (9, 'Gonçalo Charneca', 'gonka2004@gmail.com', 'MANHA');

-- 2. PAPEIS
INSERT INTO gestor (id_func) VALUES (1), (2);
INSERT INTO tecnico (id_func) VALUES (3), (4), (5);
INSERT INTO backoffice (id_func) VALUES (6), (7), (9), (8);

-- 3. ARMAZENS
INSERT INTO armazem (id_Armazem) VALUES (1), (2);

-- 4. ARMARIOS
INSERT INTO armario (nArmario, capacidade, estado, trancado, id_gestor) VALUES
  (101, 20, 'Operacional', TRUE, 1),
  (102, 20, 'Operacional', TRUE, 1),
  (103, 15, 'Avariado', FALSE, 2),
  (201, 30, 'Operacional', TRUE, 2);

-- 5. TIPOS DE FERRAMENTA
INSERT INTO tipo_ferramenta (codigo, nome, categoria, descricao) VALUES
  (1, 'Chave de Fendas',       'Manual',     'Chave de fendas de cabeca plana'),
  (2, 'Chave Inglesa',         'Manual',     'Chave ajustavel para porcas e parafusos'),
  (3, 'Alicate Universal',     'Manual',     'Alicate de uso geral'),
  (4, 'Berbequim Pneumatico',  'Pneumatica', 'Berbequim alimentado a ar comprimido'),
  (5, 'Chave Dinamometrica',   'Medicao',    'Aperto controlado com binario definido'),
  (6, 'Multimetro Digital',    'Eletrica',   'Medicao de tensao, corrente e resistencia');

-- 6. FERRAMENTAS (as 'Requisitada' = as que estão em requisições abertas)
INSERT INTO ferramenta
  (codigo_tipo, nFerramenta, estado, disponibilidade, nArmario, id_Armazem, nome_tipo) VALUES
  (1, 1, 'Operacional', 'Disponivel',     101, NULL, 'Chave de Fendas'),
  (1, 2, 'Operacional', 'Requisitada',    101, NULL, 'Chave de Fendas'),
  (1, 3, 'Operacional', 'Requisitada',    102, NULL, 'Chave de Fendas'),
  (2, 1, 'Operacional', 'Disponivel',     101, NULL, 'Chave Inglesa'),
  (2, 2, 'Danificada',  'Em Manutencao',  NULL, 1,   'Chave Inglesa'),
  (3, 1, 'Operacional', 'Requisitada',    102, NULL, 'Alicate Universal'),
  (3, 2, 'Operacional', 'Disponivel',     102, NULL, 'Alicate Universal'),
  (4, 1, 'Operacional', 'Disponivel',     201, NULL, 'Berbequim Pneumatico'),
  (4, 2, 'Abatida',     'Indisponivel',   NULL, 2,   'Berbequim Pneumatico'),
  (5, 1, 'Operacional', 'Requisitada',    201, NULL, 'Chave Dinamometrica'),
  (6, 1, 'Operacional', 'Disponivel',     NULL, 1,   'Multimetro Digital');

-- 7. REQUISICOES (datas recentes; dhDevolucao NULL = ainda em uso)
INSERT INTO requisicao (idRequisicao, dhRequisicao, dhDevolucao, id_tecnico) VALUES
  (1, '2026-06-22 08:30:00', '2026-06-22 17:10:00', 3),  -- fechada (Pedro)
  (2, '2026-06-25 09:00:00', '2026-06-26 11:20:00', 4),  -- fechada (Ana)
  (3, '2026-06-28 14:20:00', NULL,                  5),  -- aberta  (Rui)
  (4, '2026-06-29 07:45:00', '2026-06-29 16:30:00', 3),  -- fechada (Pedro, ontem)
  (5, '2026-06-30 08:00:00', NULL,                  4),  -- aberta  (Ana, hoje)
  (6, '2026-06-30 09:15:00', NULL,                  5);  -- aberta  (Rui, hoje)

-- 8. FERRAMENTAS POR REQUISICAO
INSERT INTO requisicao_ferramenta (idRequisicao, codigo_tipo, nFerramenta) VALUES
  (1, 1, 1), (1, 2, 1),         -- Req 1 fechada
  (2, 4, 1), (2, 6, 1),         -- Req 2 fechada
  (3, 5, 1),                    -- Req 3 aberta (Rui)
  (4, 3, 2),                    -- Req 4 fechada
  (5, 1, 2), (5, 3, 1),         -- Req 5 aberta (Ana)
  (6, 1, 3);                    -- Req 6 aberta (Rui)

-- 9. TAREFAS (datas recentes; cobre 3 estados e 3 prioridades)
INSERT INTO tarefa (idTarefa, descricao, id_gestor, id_tecnico, estado, prioridade, dhAtribuicao) VALUES
  (1, 'Manutenção preventiva do trem de aterragem', 1, 3, 'CONCLUIDA', 'NORMAL', '2026-06-26 09:00:00'),
  (2, 'Inspeção do painel elétrico principal',      2, 4, 'CONCLUIDA', 'ALTA',   '2026-06-27 10:30:00'),
  (3, 'Inspeção do motor A320',                     1, 3, 'EM CURSO',  'ALTA',   '2026-06-30 08:00:00'),  -- hoje
  (4, 'Substituição de parafusos da fuselagem',     1, 4, 'PENDENTE',  'NORMAL', '2026-06-30 08:30:00'),  -- hoje
  (5, 'Inspeção dos sistemas aviónicos',            2, 5, 'PENDENTE',  'ALTA',   '2026-06-29 14:00:00'),  -- ontem
  (6, 'Verificação do sistema hidráulico',          2, 3, 'EM CURSO',  'NORMAL', '2026-06-29 09:30:00'),  -- ontem
  (7, 'Calibração de sensores de pressão',          1, 4, 'CONCLUIDA', 'BAIXA',  '2026-06-27 16:00:00');

-- 10. FERRAMENTAS PERMITIDAS POR TAREFA
INSERT INTO tarefa_ferramenta_permitida (idTarefa, codigo_tipo, nFerramenta) VALUES
  (1, 1, 1), (1, 2, 1),
  (2, 6, 1), (2, 3, 2),
  (3, 6, 1), (3, 5, 1),
  (4, 1, 1), (4, 2, 1),
  (5, 6, 1), (5, 3, 2),
  (6, 4, 1), (6, 3, 1),
  (7, 5, 1);