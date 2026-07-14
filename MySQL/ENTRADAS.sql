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
INSERT INTO tecnico (id_func) VALUES (3), (4), (5), (8);
INSERT INTO backoffice (id_func) VALUES (6), (7), (9);

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
  (1, 2, 'Operacional', 'Disponivel',    101, NULL, 'Chave de Fendas'),
  (1, 3, 'Operacional', 'Disponivel',    102, NULL, 'Chave de Fendas'),
  (2, 1, 'Operacional', 'Disponivel',     101, NULL, 'Chave Inglesa'),
  (2, 2, 'Danificada',  'Em Manutencao',  NULL, 1,   'Chave Inglesa'),
  (3, 1, 'Operacional', 'Disponivel',    102, NULL, 'Alicate Universal'),
  (3, 2, 'Operacional', 'Disponivel',     102, NULL, 'Alicate Universal'),
  (4, 1, 'Operacional', 'Disponivel',     201, NULL, 'Berbequim Pneumatico'),
  (4, 2, 'Abatida',     'Indisponivel',   NULL, 2,   'Berbequim Pneumatico'),
  (5, 1, 'Operacional', 'Disponivel',    201, NULL, 'Chave Dinamometrica'),
  (6, 1, 'Operacional', 'Disponivel',     NULL, 1,   'Multimetro Digital');
