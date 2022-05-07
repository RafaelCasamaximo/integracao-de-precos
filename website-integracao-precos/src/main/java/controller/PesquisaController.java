package controller;

import dao.DAOFactory;
import model.Empresa;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PesquisaController", urlPatterns = {
        "/Pesquisa"
})
public class PesquisaController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;

        switch (request.getServletPath()){
            case "/Pesquisa":
                dispatcher = request.getRequestDispatcher("/view/search/pesquisaAvancada.jsp");
                dispatcher.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
