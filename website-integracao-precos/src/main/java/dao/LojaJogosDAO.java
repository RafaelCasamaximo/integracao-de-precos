package dao;

import model.LojaJogos;
import model.Jogo;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.sql.SQLException;

import java.sql.Date;
import java.util.List;

public interface LojaJogosDAO extends DAO<LojaJogos> {
    public List<ImmutablePair<Jogo, LojaJogos>> getCrawlEntry(Integer id_loja, Integer id_jogo, Date data_crawl) throws SQLException;
    List<ImmutablePair<Date, Float>> getDatePrices(Integer id_loja, Integer id_jogo) throws SQLException;
    public List<ImmutablePair<Jogo, LojaJogos>> getAllEntries() throws SQLException;
    public LojaJogos readByStoreIDGameIDAndDate(Integer storeId, Integer gameId, Date data_crawl) throws SQLException;

    public List<ImmutablePair<Jogo, LojaJogos>> readByAdvancedSearch(String gameName, String gamePublisher, String gameStore, String gameGenre, float gameMinPrice, float gameMaxPrice, boolean gameIsFree) throws SQLException;
}
