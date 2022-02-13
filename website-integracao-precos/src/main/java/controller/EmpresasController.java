package controller;

import dao.DAO;
import dao.DAOFactory;
import model.Empresa;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "EmpresasController", urlPatterns = {
        "/Empresas"
})
public class EmpresasController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO<Empresa> dao;
        Empresa empresa;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()){
            case "/Empresas":
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    dao = daoFactory.getEmpresaDAO();

                    List<Empresa> lista_empresa = dao.all();
                    request.setAttribute("lista_empresa", lista_empresa);

                } catch (ClassNotFoundException | IOException | SQLException ex){
                    request.getSession().setAttribute("error", ex.getMessage());
                }

                dispatcher = request.getRequestDispatcher("/view/empresa/empresas.jsp");
                dispatcher.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
