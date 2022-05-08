package controller;

import dao.DAOFactory;
import model.Empresa;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PesquisaController", urlPatterns = {
        "/Pesquisa",
        "/RequisicaoPesquisa",
        "/ResultadoPesquisa"
})
public class PesquisaController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        HttpSession session = request.getSession();

        switch (request.getServletPath()){
            case "/Pesquisa":
                dispatcher = request.getRequestDispatcher("/view/search/pesquisaAvancada.jsp");
                dispatcher.forward(request, response);
                break;

            case "/ResultadoPesquisa":
                String gameName = (String) session.getAttribute("gameName");
                String gamePublisher = (String) session.getAttribute("gameCompany");
                String gameStore = (String) session.getAttribute("gameStore");
                String gameGenre = (String) session.getAttribute("gameGenre");
                String gameMinPrice = (String) session.getAttribute("gameMinPrice");
                String gameMaxPrice = (String) session.getAttribute("gameMaxPrice");
                String gameMinDiscount = (String) session.getAttribute("gameMinDiscount");
                String gameMaxDiscount = (String) session.getAttribute("gameMaxDiscount");
                String gameIsFree = (String) session.getAttribute("gameIsFree");

                System.out.println("Valores Lidos:" + gameName + gamePublisher + gameStore + gameGenre + gameMinPrice + gameMaxPrice + gameMinDiscount + gameMaxDiscount + gameIsFree);


                dispatcher = request.getRequestDispatcher("/view/search/resultadoPesquisaAvancada.jsp");
                dispatcher.forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String servletPath = request.getServletPath();
        switch (request.getServletPath()){
            case "/RequisicaoPesquisa":

                String gameName = request.getParameter("gameName");
                String gamePublisher = request.getParameter("gameCompany");
                String gameStore = request.getParameter("gameStore");
                String gameGenre = request.getParameter("gameGenre");
                String gameMinPrice = request.getParameter("gameMinPrice");
                String gameMaxPrice = request.getParameter("gameMaxPrice");
                String gameMinDiscount = request.getParameter("gameMinDiscount");
                String gameMaxDiscount = request.getParameter("gameMaxDiscount");
                String gameIsFree = request.getParameter("gameIsFree");

//                System.out.println("Valores Lidos:" + gameName + gamePublisher + gameStore + gameGenre + gameMinPrice + gameMaxPrice + gameMinDiscount + gameMaxDiscount + gameIsFree);

                session.setAttribute("gameName", gameName);
                session.setAttribute("gameCompany", gamePublisher);
                session.setAttribute("gameStore", gameStore);
                session.setAttribute("gameGenre", gameGenre);
                session.setAttribute("gameMinPrice", gameMinPrice);
                session.setAttribute("gameMaxPrice", gameMaxPrice);
                session.setAttribute("gameMinDiscount", gameMinDiscount);
                session.setAttribute("gameMaxDiscount", gameMaxDiscount);
                session.setAttribute("gameIsFree", gameIsFree);
                response.sendRedirect(request.getContextPath() + "/ResultadoPesquisa");
                break;

        }
    }
}
