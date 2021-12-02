CREATE USER databaseAdmin LOGIN CREATEDB PASSWORD 'databaseAdmin';

CREATE DATABASE databaseAdmin WITH OWNER = databaseAdmin ENCodING = UTF8;

CREATE SCHEMA IntegracaoPrecos;

CREATE TABLE IntegracaoPrecos.Loja(
    id SERIAL,
    nome CHAR(60) NOT NULL,
    /* Encher linguiça com dados aqui */
    CONSTRAINT id_loja PRIMARY KEY(id)
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
    CONSTRAINT id_jogo PRIMARY KEY(id) 
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


CREATE TABLE IntegracaoPrecos.Empresa(
    id_empresa INT,
    descricao_curta CHAR(60),
    numero_jogos INT,
    website CHAR(60)
    CONSTRAINT fk_id_empresa FOREIGN KEY(id_empresa)
        REFERENCES IntegracaoPrecos.Jogo(nome_empresa)
    CONSTRAINT pk_id_empresa PRIMARY KEY(id_empresa)
);


