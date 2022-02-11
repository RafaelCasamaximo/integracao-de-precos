package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import model.Loja;

public class PgLojaDAO implements LojaDAO {
    private final Connection connection;

    public PgLojaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Loja loja) throws SQLException {

    }

    @Override
    public Loja read(Integer id) throws SQLException {
        return null;
    }

    @Override
    public void update(Loja loja) throws SQLException {

    }

    @Override
    public void delete(Integer id) throws SQLException {

    }

    @Override
    public List<Loja> all() throws SQLException {
        return null;
    }

    @Override
    public void authenticate(Loja loja) throws SQLException, SecurityException {

    }
}
