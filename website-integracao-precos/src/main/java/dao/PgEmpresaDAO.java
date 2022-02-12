package dao;

import model.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgEmpresaDAO implements EmpresaDAO {
    private Connection connection;

    public PgEmpresaDAO(Connection connection) { this.connection = connection; }

    private static final String CREATE_QUERY =
            "INSERT INTO empresa " +
                    "(descricao_curta, numero_jogos, website) " +
                    "VALUES (?, ?, ?)";

    private static final String READ_QUERY =
            "SELECT * " +
                    "FROM empresa " +
                    "WHERE id=?;";

    private static final String UPDATE_QUERY =
            "UPDATE empresa " +
                    "SET descricao_curta=?, numero_jogos=?, website=? " +
                    "WHERE id=?;";

    private static final String DELETE_QUERY =
            "DELETE FROM empresa " +
                    "WHERE id=?;";

    private static final String ALL_QUERY =
            "SELECT * " +
                    "FROM empresa;";

    @Override
    public void create(Empresa empresa) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, empresa.getDescricao_curta());
            statement.setInt(2, empresa.getNumero_jogos());
            statement.setString(3, empresa.getWebsite());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao criar empresa: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao criar jogo.");
            }
        }
    }

    @Override
    public Empresa read(Integer id) throws SQLException {
        Empresa empresa = new Empresa();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    empresa.setId(id);
                    empresa.setDescricao_curta(result.getString("descricao_curta"));
                    empresa.setNumero_jogos(result.getInt("numero_jogos"));
                    empresa.setWebsite(result.getString("website"));
                } else {
                    throw new SQLException("Erro ao visualizar: empresa não encontrada.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao visualizar: empresa não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar empresa.");
            }
        }

        return empresa;
    }

    @Override
    public void update(Empresa empresa) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, empresa.getDescricao_curta());
            statement.setInt(2, empresa.getNumero_jogos());
            statement.setString(3, empresa.getWebsite());
            statement.setInt(4, empresa.getId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao editar: empresa não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao editar empresa.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: empresa não encontrada.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: empresa não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir empresa.");
            }
        }
    }

    @Override
    public List<Empresa> all() throws SQLException {
        List<Empresa> lista_empresas = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Empresa empresa = new Empresa();

                empresa.setId(result.getInt("id"));
                empresa.setDescricao_curta(result.getString("descricao_curta"));
                empresa.setNumero_jogos(result.getInt("numero_jogos"));
                empresa.setWebsite(result.getString("website"));

                lista_empresas.add(empresa);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar empresas.");
        }

        return lista_empresas;
    }
}
