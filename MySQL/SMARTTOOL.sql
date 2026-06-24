CREATE DATABASE smarttool;
USE SMARTTOOL;

CREATE TABLE funcionario (
  id_func INT NOT NULL AUTO_INCREMENT,
  nomeCompleto VARCHAR(150) NOT NULL,
  email VARCHAR(100) NOT NULL,
  turno VARCHAR(10) DEFAULT 'MANHA',
  ativo BOOLEAN DEFAULT TRUE,
  PRIMARY KEY (id_func),
  UNIQUE KEY (email)
);

CREATE TABLE gestor (
  id_func INT NOT NULL,
  PRIMARY KEY (id_func),
  FOREIGN KEY (id_func) REFERENCES funcionario(id_func) ON DELETE CASCADE
);

CREATE TABLE tecnico (
  id_func INT NOT NULL,
  PRIMARY KEY (id_func),
  FOREIGN KEY (id_func) REFERENCES funcionario(id_func) ON DELETE CASCADE
);

CREATE TABLE backoffice (
  id_func INT NOT NULL,
  PRIMARY KEY (id_func),
  FOREIGN KEY (id_func) REFERENCES funcionario(id_func) ON DELETE CASCADE
);

CREATE TABLE armazem (
  id_Armazem INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id_Armazem)
);

CREATE TABLE armario (
  nArmario INT NOT NULL,
  capacidade INT NOT NULL,
  estado VARCHAR(50) NOT NULL,
  trancado BOOLEAN NOT NULL,
  id_gestor INT DEFAULT NULL,
  PRIMARY KEY (nArmario),
  FOREIGN KEY (id_gestor) REFERENCES gestor(id_func) ON DELETE SET NULL
);

CREATE TABLE tipo_ferramenta (
  codigo INT NOT NULL, -- 4 digitos da parte do codigo ex: 00001
  nome VARCHAR(100) NOT NULL,
  categoria VARCHAR(50) NOT NULL,
  descricao TEXT,
  PRIMARY KEY (codigo)
);

CREATE TABLE ferramenta (
  codigo_tipo INT NOT NULL, -- 5 digitos (fk do tipo de ferramenta)
  nFerramenta INT NOT NULL, -- 4 digitos (chave parcial)
  idFerramenta INT AS (codigo_tipo * 100000 + nFerramenta) STORED, -- faz o calculo diretamente (ex: ct = 0001, nf = 00023 -> id = 000100023)
  estado VARCHAR(50) NOT NULL,
  disponibilidade VARCHAR(50) NOT NULL,
  nome_tipo VARCHAR(100) NOT NULL,
  nArmario INT DEFAULT NULL,
  id_Armazem INT DEFAULT NULL,
  PRIMARY KEY (codigo_tipo, nFerramenta),
  UNIQUE KEY (idFerramenta),
  FOREIGN KEY (codigo_tipo) REFERENCES tipo_ferramenta(codigo),
  FOREIGN KEY (nArmario) REFERENCES armario(nArmario) ON DELETE SET NULL,
  FOREIGN KEY (id_Armazem) REFERENCES armazem(id_Armazem) ON DELETE SET NULL
);

CREATE TABLE requisicao (
  idRequisicao INT NOT NULL AUTO_INCREMENT,
  dhRequisicao DATETIME NOT NULL,
  dhDevolucao DATETIME DEFAULT NULL,
  id_tecnico INT NOT NULL,
  PRIMARY KEY (idRequisicao),
  FOREIGN KEY (id_tecnico) REFERENCES tecnico(id_func) ON DELETE CASCADE
);

CREATE TABLE requisicao_ferramenta (
  idRequisicao INT NOT NULL,
  codigo_tipo INT NOT NULL,
  nFerramenta INT NOT NULL,
  PRIMARY KEY (idRequisicao, codigo_tipo, nFerramenta),
  FOREIGN KEY (idRequisicao) REFERENCES requisicao(idRequisicao) ON DELETE CASCADE,
  FOREIGN KEY (codigo_tipo, nFerramenta) REFERENCES ferramenta(codigo_tipo, nFerramenta) ON DELETE CASCADE
);

CREATE TABLE tarefa (
  idTarefa INT NOT NULL AUTO_INCREMENT,
  descricao VARCHAR(255) NOT NULL,
  id_gestor INT NOT NULL,
  id_tecnico INT NOT NULL,
  estado VARCHAR(50) DEFAULT 'PENDENTE', -- Pode ser PENDENTE, EM CURSO, CONCLUIDA
  dhAtribuicao DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (idTarefa),
  FOREIGN KEY (id_gestor) REFERENCES gestor(id_func),
  FOREIGN KEY (id_tecnico) REFERENCES tecnico(id_func)
);

CREATE TABLE tarefa_ferramenta_permitida (
  idTarefa INT NOT NULL,
  codigo_tipo INT NOT NULL,
  nFerramenta INT NOT NULL,
  PRIMARY KEY (idTarefa, codigo_tipo, nFerramenta),
  FOREIGN KEY (idTarefa) REFERENCES tarefa(idTarefa) ON DELETE CASCADE,
  FOREIGN KEY (codigo_tipo, nFerramenta) REFERENCES ferramenta(codigo_tipo, nFerramenta) ON DELETE CASCADE
);