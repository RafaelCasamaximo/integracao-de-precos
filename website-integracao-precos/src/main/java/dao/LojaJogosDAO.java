package dao;

import model.LojaJogos;
import model.Jogo;
import java.sql.SQLException;

import java.sql.Date;

public interface LojaJogosDAO extends DAO<LojaJogos> {
    public Jogo getCrawlEntry(Integer id_loja, Integer id_jogo, Date data_crawl) throws SQLException;
}
