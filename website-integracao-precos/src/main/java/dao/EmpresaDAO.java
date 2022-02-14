package dao;

import model.Empresa;
import model.Loja;

import java.sql.SQLException;

public interface EmpresaDAO extends DAO<Empresa> {
    public Empresa readByName(String name) throws SQLException, SecurityException;

}
