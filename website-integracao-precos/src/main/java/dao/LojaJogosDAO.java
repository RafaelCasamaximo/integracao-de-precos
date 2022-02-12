package dao;

import model.LojaJogos;

import java.sql.Date;

public interface LojaJogosDAO extends DAO<LojaJogos> {
    public LojaJogos getCrawlEntry(Integer id_loja, Integer id_jogo, Date data_crawl);
}
