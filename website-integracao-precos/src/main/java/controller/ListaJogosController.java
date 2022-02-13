package controller;

import dao.DAO;
import dao.DAOFactory;
import model.Jogo;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ListaJogos", urlPatterns = {
        "/ListaJogos",
        "/ListaJogos/Steam",
        "/ListaJogos/Epic",
        "/ListaJogos/Playstation",
})
public class ListaJogosController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO<Jogo> dao;
        Jogo jogo;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()){
            case "/ListaJogos":
                try (DAOFactory daoFactory = DAOFactory.getInstance()){
                    dao = daoFactory.getJogoDAO();

                    List<Jogo> lista_jogo = dao.all();
                    request.setAttribute("lista_jogo", lista_jogo);

                } catch (ClassNotFoundException | IOException | SQLException ex){
                    request.getSession().setAttribute("error", ex.getMessage());
                }

                dispatcher = request.getRequestDispatcher("/view/loja/listaJogos.jsp");
                dispatcher.forward(request, response);
                break;

            case "/ListaJogos/Steam":
                try(DAOFactory daoFactory = DAOFactory.getInstance()){
                    System.out.println("Teste");

                }catch (ClassNotFoundException | IOException | SQLException ex){

                }
                dispatcher = request.getRequestDispatcher("/view/loja/listaJogos.jsp");
                dispatcher.forward(request, response);
                break;

            case "/ListaJogos/Epic":
                try(DAOFactory daoFactory = DAOFactory.getInstance()){
                    System.out.println("Teste2");


                }catch (ClassNotFoundException | IOException | SQLException ex){

                }
                dispatcher = request.getRequestDispatcher("/view/loja/listaJogos.jsp");
                dispatcher.forward(request, response);
                break;

            case "/ListaJogos/Playstation":
                try(DAOFactory daoFactory = DAOFactory.getInstance()){
                    System.out.println("Teste3");


                }catch (ClassNotFoundException | IOException | SQLException ex){

                }
                dispatcher = request.getRequestDispatcher("/view/loja/listaJogos.jsp");
                dispatcher.forward(request, response);
                break;

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
