package dao;

import model.Jogo;
import model.Loja;

import java.sql.SQLException;

public interface JogoDAO extends DAO<Jogo> {
    public Jogo readByName(String name) throws SQLException, SecurityException;

}
