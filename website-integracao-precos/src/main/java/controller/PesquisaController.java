package controller;

import dao.DAO;
import dao.DAOFactory;
import dao.LojaJogosDAO;
import model.Empresa;
import model.Jogo;
import model.Loja;
import model.LojaJogos;
import org.apache.commons.lang3.tuple.ImmutablePair;
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
                DAO<LojaJogos> dao;
                LojaJogosDAO lojaJogosDAO;

                String gameName = (String) session.getAttribute("gameName");
                String gamePublisher = (String) session.getAttribute("gameCompany");
                String gameStore = (String) session.getAttribute("gameStore");
                String gameGenre = (String) session.getAttribute("gameGenre");

                float gameMinPrice = 0;
                float gameMaxPrice = 1000000;
                boolean gameIsFree = false;
                try {
                    gameMinPrice = Float.parseFloat((String) session.getAttribute("gameMinPrice")) * 100;
                }catch (Exception exception){
                    gameMinPrice = 0;
                }
                try {
                    gameMaxPrice = Float.parseFloat((String) session.getAttribute("gameMaxPrice")) * 100;
                }catch (Exception exception){
                    gameMaxPrice = 1000000;
                }
                try {
                    System.out.println("Minha tentativa no controller: " + (String) session.getAttribute("gameIsFree"));
                    gameIsFree = Boolean.parseBoolean((String) session.getAttribute("gameIsFree")) == true ? true : false;
                }catch (Exception exception){
                    gameIsFree = false;
                }

                if(gameIsFree)
                {
                    gameMinPrice = 0;
                    gameMaxPrice = 1000000;
                }

                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    lojaJogosDAO = daoFactory.getLojaJogosDAO();

                    List<ImmutablePair<Jogo, LojaJogos>> lista_lojaJogos = lojaJogosDAO.readByAdvancedSearch(gameName, gamePublisher, gameStore, gameGenre, gameMinPrice, gameMaxPrice, gameIsFree);
                    request.setAttribute("lista_lojaJogos", lista_lojaJogos);


                } catch(ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                }

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
                String gameIsFree = request.getParameter("gameIsFree");
                System.out.println("Tentativa no Post: " + request.getParameter("gameIsFree"));

                session.setAttribute("gameName", gameName);
                session.setAttribute("gameCompany", gamePublisher);
                session.setAttribute("gameStore", gameStore);
                session.setAttribute("gameGenre", gameGenre);
                session.setAttribute("gameMinPrice", gameMinPrice);
                session.setAttribute("gameMaxPrice", gameMaxPrice);
                session.setAttribute("gameIsFree", gameIsFree);
                response.sendRedirect(request.getContextPath() + "/ResultadoPesquisa");
                break;

        }
    }
}
