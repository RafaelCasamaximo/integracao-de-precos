CREATE TABLE Loja(
    id SERIAL,
    nome CHAR(100) NOT NULL,
    CONSTRAINT id_loja PRIMARY KEY(id)
);


CREATE TABLE Empresa(
    id SERIAL,
    nome CHAR(100) NOT NULL,
    descricao_curta CHAR(500),
    numero_jogos INT,
    website CHAR(100),
    CONSTRAINT id_empresa PRIMARY KEY(id)
);

CREATE TABLE Jogo(
    id SERIAL,
    nome CHAR(500) NOT NULL,
    genero CHAR(500),
    linguagens_suportadas CHAR(500),
    suporte_a_controle BOOLEAN,
    nome_empresa CHAR(100),
    gratuito BOOLEAN,
    idade_requerida INT,
    descricao_curta CHAR(500),
    descricao_longa CHAR(2000),
    id_empresa INT,
    CONSTRAINT id_jogo PRIMARY KEY(id),
    CONSTRAINT fk_nome_empresa FOREIGN KEY(id_empresa)
        REFERENCES Empresa(id)
);


CREATE TABLE LojaJogos(
    id_loja INT,
    id_jogo INT,
    preco_jogo NUMERIC(20, 5),
    loja_crawl CHAR(100),
    data_crawl DATE,
    CONSTRAINT fk_id_loja FOREIGN KEY(id_loja)
        REFERENCES Loja(id),
    CONSTRAINT fk_id_jogo FOREIGN KEY(id_jogo)
        REFERENCES Jogo(id)
);


