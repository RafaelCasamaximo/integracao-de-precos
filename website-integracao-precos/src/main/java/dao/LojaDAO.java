package dao;

import java.sql.SQLException;
import model.Loja;

public interface LojaDAO extends DAO<Loja> {
    public void authenticate(Loja loja) throws SQLException, SecurityException;
    public Loja readByName(String name) throws SQLException, SecurityException;
}
