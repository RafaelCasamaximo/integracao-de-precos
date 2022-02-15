package dao;

import model.Jogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgJogoDAO implements JogoDAO {
    private Connection connection;

    public PgJogoDAO(Connection connection) { this.connection = connection; }

    private static final String CREATE_QUERY =
            "INSERT INTO jogo " +
                    "(nome, genero, linguagens_suportadas, suporte_a_controle, nome_empresa, " +
                    "gratuito, idade_requerida, descricao_curta, descricao_longa, id_empresa) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT * " +
                    "FROM jogo " +
                    "WHERE id=?;";

    private static final String READ_BY_NAME_QUERY =
            "SELECT * " +
                    "FROM jogo " +
                    "WHERE nome=?;";


    private static final String UPDATE_QUERY =
            "UPDATE jogo " +
                    "SET nome=?, genero=?, linguagens_suportadas=?, suporte_a_controle=?, nome_empresa=?, " +
                    "gratuito=?, idade_requerida=?, descricao_curta=?, descricao_longa=?, id_empresa=? " +
                    "WHERE id=?;";

    private static final String DELETE_QUERY =
            "DELETE FROM jogo " +
                    "WHERE id=?;";

    private static final String ALL_QUERY =
            "SELECT * " +
                    "FROM jogo;";

    @Override
    public void create(Jogo jogo) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, jogo.getNome());
            statement.setString(2, jogo.getGenero());
            statement.setString(3, jogo.getLinguagens_suportadas());
            statement.setBoolean(4, jogo.getSuporte_a_controle());
            statement.setString(5, jogo.getNome_empresa());
            statement.setBoolean(6, jogo.getGratuito());
            statement.setInt(7, jogo.getIdade_requerida());
            statement.setString(8, jogo.getDescricao_curta());
            statement.setString(9, jogo.getDescricao_longa());
            statement.setInt(10, jogo.getId_empresa());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().contains("fk_nome_empresa")) {
                throw new SQLException("Erro ao criar jogo: jogo já existente.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao criar jogo: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao criar jogo.");
            }
        }
    }

    @Override
    public Jogo read(Integer id) throws SQLException {
        Jogo jogo = new Jogo();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    jogo.setId(result.getInt("id"));
                    jogo.setNome(result.getString("nome"));
                    jogo.setGenero(result.getString("genero"));
                    jogo.setLinguagens_suportadas(result.getString("linguagens_suportadas"));
                    jogo.setSuporte_a_controle(result.getBoolean("suporte_a_controle"));
                    jogo.setNome_empresa(result.getString("nome_empresa"));
                    jogo.setGratuito(result.getBoolean("gratuito"));
                    jogo.setIdade_requerida(result.getInt("idade_requerida"));
                    jogo.setDescricao_curta(result.getString("descricao_curta"));
                    jogo.setDescricao_longa(result.getString("descricao_longa"));
                    jogo.setId_empresa(result.getInt("id_empresa"));
                } else {
                    throw new SQLException("Erro ao visualizar: jogo não encontrado.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao visualizar: jogo não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar jogo.");
            }
        }

        return jogo;
    }

    @Override
    public void update(Jogo jogo) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, jogo.getNome());
            statement.setString(2, jogo.getGenero());
            statement.setString(3, jogo.getLinguagens_suportadas());
            statement.setBoolean(4, jogo.getSuporte_a_controle());
            statement.setString(5, jogo.getNome_empresa());
            statement.setBoolean(6, jogo.getGratuito());
            statement.setInt(7, jogo.getIdade_requerida());
            statement.setString(8, jogo.getDescricao_curta());
            statement.setString(9, jogo.getDescricao_longa());
            statement.setInt(10, jogo.getId_empresa());
            statement.setInt(11, jogo.getId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao editar: jogo não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao editar jogo.");
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

            if (ex.getMessage().equals("Erro ao excluir: jogo não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir jogo.");
            }
        }
    }

    @Override
    public List<Jogo> all() throws SQLException {
        List<Jogo> lista_jogos = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Jogo jogo = new Jogo();

                jogo.setId(result.getInt("id"));
                jogo.setNome(result.getString("nome"));
                jogo.setGenero(result.getString("genero"));
                jogo.setLinguagens_suportadas(result.getString("linguagens_suportadas"));
                jogo.setSuporte_a_controle(result.getBoolean("suporte_a_controle"));
                jogo.setNome_empresa(result.getString("nome_empresa"));
                jogo.setGratuito(result.getBoolean("gratuito"));
                jogo.setIdade_requerida(result.getInt("idade_requerida"));
                jogo.setDescricao_curta(result.getString("descricao_curta"));
                jogo.setDescricao_longa(result.getString("descricao_longa"));
                jogo.setId_empresa(result.getInt("id_empresa"));

                lista_jogos.add(jogo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar jogos.");
        }

        return lista_jogos;
    }

    public Jogo readByName(String name) throws SQLException, SecurityException
    {
        Jogo jogo = new Jogo();

        try (PreparedStatement statement = connection.prepareStatement(READ_BY_NAME_QUERY)) {
            statement.setString(1, name);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    jogo.setId(result.getInt("id"));
                    jogo.setNome(result.getString("nome"));
                    jogo.setGenero(result.getString("genero"));
                    jogo.setLinguagens_suportadas(result.getString("linguagens_suportadas"));
                    jogo.setSuporte_a_controle(result.getBoolean("suporte_a_controle"));
                    jogo.setNome_empresa(result.getString("nome_empresa"));
                    jogo.setGratuito(result.getBoolean("gratuito"));
                    jogo.setIdade_requerida(result.getInt("idade_requerida"));
                    jogo.setDescricao_curta(result.getString("descricao_curta"));
                    jogo.setDescricao_longa(result.getString("descricao_longa"));
                    jogo.setId_empresa(result.getInt("id_empresa"));
                } else {
                    throw new SQLException("Erro ao visualizar: jogo não encontrado.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgJogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao visualizar: jogo não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar jogo.");
            }
        }

        return jogo;
    }
}
