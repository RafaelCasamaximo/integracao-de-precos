package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Loja;

public class PgLojaDAO implements LojaDAO {
    private final Connection connection;

    public PgLojaDAO(Connection connection) {
        this.connection = connection;
    }

    private static String READ_QUERY =
            "SELECT id, nome" +
                    "FROM j2ee.loja" +
                    "WHERE id = ?;";

    private static final String ALL_QUERY =
            "SELECT id, nome" +
                    "FROM j2ee.loja" +
                    "ORDER BY id;";

    @Override
    public void create(Loja loja) throws SQLException {

    }

    @Override
    public Loja read(Integer id) throws SQLException {
        Loja loja = new Loja();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    loja.setId(id);
                    loja.setNome(result.getString("nome"));
                } else {
                    throw new SQLException("Erro ao visualizar: loja não encontrada");
                }
            } catch (SQLException ex) {
                Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

                if (ex.getMessage().equals("Erro ao visualizar: loja não encontrada")) {
                    throw ex;
                } else {
                    throw new SQLException("Erro ao visualizar loja");
                }
            }
        }

        return loja;
    }

    @Override
    public void update(Loja loja) throws SQLException {

    }

    @Override
    public void delete(Integer id) throws SQLException {

    }

    @Override
    public List<Loja> all() throws SQLException {
        List<Loja> listaLoja = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Loja loja = new Loja();
                loja.setId(result.getInt("id"));
                loja.setNome(result.getString("nome"));

                listaLoja.add(loja);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgLojaDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar usuários.");
        }

        return listaLoja;
    }

    @Override
    public void authenticate(Loja loja) throws SQLException, SecurityException {

    }
}
