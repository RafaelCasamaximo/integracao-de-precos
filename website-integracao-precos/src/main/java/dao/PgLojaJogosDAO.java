package dao;

import model.LojaJogos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgLojaJogosDAO implements LojaJogosDAO {
    public Connection connection;

    public PgLojaJogosDAO(Connection connection) { this.connection = connection; }

    private static final String CREATE_QUERY =
            "INSERT INTO lojajogos " +
                    "(id_loja, id_jogo, preco_jogo, loja_crawl, data_crawl) " +
                    "VALUES (?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT * " +
                    "FROM lojajogos " +
                    "WHERE id=?;";

    private static final String UPDATE_QUERY =
            "UPDATE lojajogos " +
                    "SET id_loja=?, id_jogo=?, preco_jogo=?, loja_crawl=?, data_crawl=? " +
                    "WHERE id_loja=? AND id_jogo=? AND data_crawl=?;";

    private static final String DELETE_QUERY =
            "DELETE FROM lojajogos " +
                    "WHERE id=?;";

    private static final String ALL_QUERY =
            "SELECT * " +
                    "FROM lojajogos;";

    @Override
    public void create(LojaJogos lojaJogos) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, lojaJogos.getId_loja());
            statement.setInt(2, lojaJogos.getId_jogo());
            statement.setFloat(3, lojaJogos.getPreco_jogo());
            statement.setString(4, lojaJogos.getLoja_crawl());
            statement.setDate(5, lojaJogos.getData_crawl());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao criar tabela intermediaria: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao criar tabela intermediaria.");
            }
        }
    }

    @Override
    public LojaJogos read(Integer id) throws SQLException {
        LojaJogos lojaJogos = new LojaJogos();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    lojaJogos.setId_loja(result.getInt("id_loja"));
                    lojaJogos.setId_jogo(result.getInt("id_jogo"));
                    lojaJogos.setPreco_jogo(result.getFloat("preco_jogo"));
                    lojaJogos.setLoja_crawl(result.getString("loja_crawl"));
                    lojaJogos.setData_crawl(result.getDate("data_crawl"));
                } else {
                    throw new SQLException("Erro ao visualizar: tabela intermediária não encontrada.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao visualizar: tabela intermediária não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar tabela intermediária.");
            }
        }

        return lojaJogos;
    }

    @Override
    public void update(LojaJogos lojaJogos) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, lojaJogos.getId_loja());
            statement.setInt(2, lojaJogos.getId_jogo());
            statement.setFloat(3, lojaJogos.getPreco_jogo());
            statement.setString(4, lojaJogos.getLoja_crawl());
            statement.setDate(5, lojaJogos.getData_crawl());
            statement.setInt(6, lojaJogos.getId_loja());
            statement.setInt(7, lojaJogos.getId_jogo());
            statement.setDate(8, lojaJogos.getData_crawl());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar tabela intermediaria: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar tabela intermediaria.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: jogo não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: tabela intermediária não encontrada.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir tabela intermediária.");
            }
        }
    }

    @Override
    public List<LojaJogos> all() throws SQLException {
        List<LojaJogos> lista_ljogos = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                LojaJogos lojaJogo = new LojaJogos();

                lojaJogo.setId_jogo(result.getInt("id_loja"));
                lojaJogo.setId_loja(result.getInt("id_jogo"));
                lojaJogo.setPreco_jogo(result.getFloat("preco"));
                lojaJogo.setLoja_crawl(result.getString("loja_crawl"));
                lojaJogo.setData_crawl(result.getDate("data_crawl"));

                lista_ljogos.add(lojaJogo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar tabela intermediária.");
        }

        return lista_ljogos;
    }

    @Override
    public LojaJogos getCrawlEntry(Integer id_loja, Integer id_jogo, Date data_crawl) {
        return null;
    }
}
