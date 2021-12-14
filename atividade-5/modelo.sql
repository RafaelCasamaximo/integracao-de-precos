CREATE TABLE IntegracaoPrecos.Loja(
    id SERIAL,
    nome CHAR(60) NOT NULL,
    CONSTRAINT id_loja PRIMARY KEY(id)
);


CREATE TABLE IntegracaoPrecos.Empresa(
    id SERIAL,
    descricao_curta CHAR(60),
    numero_jogos INT,
    website CHAR(60),
    CONSTRAINT id_empresa PRIMARY KEY(id)
);

CREATE TABLE IntegracaoPrecos.Jogo(
    id SERIAL,
    nome CHAR(60) NOT NULL,
    genero CHAR(60),
    linguagens_suportadas CHAR(60),
    suporte_a_controle BOOLEAN,
    nome_empresa CHAR(60),
    gratuito BOOLEAN,
    idade_requerida INT,
    descricao_curta CHAR(60),
    descricao_longa CHAR(500),
    id_empresa INT,
    CONSTRAINT id_jogo PRIMARY KEY(id),
    CONSTRAINT fk_nome_empresa FOREIGN KEY(id_empresa)
        REFERENCES IntegracaoPrecos.Empresa(id)
);


CREATE TABLE IntegracaoPrecos.LojaJogos(
    id_loja INT,
    id_jogo INT,
    preco_jogo NUMERIC(6, 2),
    CONSTRAINT fk_id_loja FOREIGN KEY(id_loja)
        REFERENCES IntegracaoPrecos.Loja(id),
    CONSTRAINT fk_id_jogo FOREIGN KEY(id_jogo)
        REFERENCES IntegracaoPrecos.Jogo(id)
);