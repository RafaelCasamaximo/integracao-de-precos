package dao;

import model.Jogo;
import model.Loja;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.sql.SQLException;

public interface JogoDAO extends DAO<Jogo> {
    public Jogo readByName(String name) throws SQLException, SecurityException;
}
