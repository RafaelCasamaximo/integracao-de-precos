package controller;

import dao.DAO;
import dao.DAOFactory;
import model.Loja;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "LojaController", urlPatterns = { "/loja" })
public class LojaController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO<Loja> dao;
        Loja loja;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "/loja":
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    dao = daoFactory.getLojaDAO();

                    List<Loja> lista_loja = dao.all();
                    request.setAttribute("lista_loja", lista_loja);
                } catch(ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                }

                dispatcher = request.getRequestDispatcher("/view/loja/index.jsp");
                dispatcher.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
