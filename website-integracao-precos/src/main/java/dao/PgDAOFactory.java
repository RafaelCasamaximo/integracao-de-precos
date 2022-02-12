/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;

public class PgDAOFactory extends DAOFactory {

    public PgDAOFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public LojaDAO getLojaDAO() {
        return new PgLojaDAO(this.connection);
    }

    @Override
    public EmpresaDAO getEmpresaDAO() {
        return new PgEmpresaDAO(this.connection);
    }

    @Override
    public JogoDAO getJogoDAO() {
        return new PgJogoDAO(this.connection);
    }

    @Override
    public LojaJogosDAO getLojaJogosDAO() {
        return new PgLojaJogosDAO(this.connection);
    }

}
