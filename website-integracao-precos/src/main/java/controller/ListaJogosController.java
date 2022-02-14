package controller;

import com.google.gson.Gson;
import dao.DAO;
import dao.DAOFactory;
import model.Jogo;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@WebServlet(name = "ListaJogos", urlPatterns = {
        "/ListaJogos",
        "/ListaJogos/Steam",
        "/ListaJogos/Epic",
        "/ListaJogos/Playstation",
        "/ListaJogos/read"
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

            case "/ListaJogos/Epic":
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

            case "/ListaJogos/Playstation":
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

            case "/ListaJogos/read":
                int id = Integer.parseInt(request.getParameter("id"));
                Jogo jogoDetail = new Jogo();

                try (DAOFactory daoFactory = DAOFactory.getInstance()){
                    dao = daoFactory.getJogoDAO();
                    jogoDetail = dao.read(id);

                }catch (ClassNotFoundException | IOException | SQLException ex){
                    request.getSession().setAttribute("error", ex.getMessage());
                }


                String json = new Gson().toJson(jogoDetail);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
