CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    nivel_do_usuario VARCHAR(20) NOT NULL
);

CREATE TABLE estabelecimento (
    id SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL,
    nome_estabelecimento VARCHAR(200) NOT NULL,
    codigo VARCHAR(200),

    CONSTRAINT fk_estabelecimento_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

CREATE TABLE lote (
    id SERIAL PRIMARY KEY,
    id_estabelecimento INT NOT NULL,
    id_usuario INT,
    nome_produto VARCHAR(200) NOT NULL,
    categoria VARCHAR(200),
    quantidade INT NOT NULL,
    unidade_de_medida VARCHAR(20),
    preco NUMERIC(19,4),
    data_validade DATE NOT NULL,
   
    CONSTRAINT fk_lote_estabelecimento
        FOREIGN KEY (id_estabelecimento) REFERENCES estabelecimento(id),
    CONSTRAINT fk_lote_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);

CREATE TABLE movimentacao (
    id SERIAL PRIMARY KEY,
    id_lote INT NOT NULL,
    id_usuario INT NOT NULL,
    tipo_movimentacao VARCHAR(20) NOT NULL,
    quantidade INT NOT NULL,
    data_ocorrencia DATE NOT NULL,
    justificativa_ocorrencia VARCHAR(200),
    entidades VARCHAR(200),
   
    CONSTRAINT fk_movimentacao_lote
        FOREIGN KEY (id_lote) REFERENCES lote(id),
    CONSTRAINT fk_movimentacao_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);