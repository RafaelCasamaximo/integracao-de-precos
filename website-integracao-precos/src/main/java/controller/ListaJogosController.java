package controller;

import com.google.gson.Gson;
import dao.*;
import model.Jogo;
import model.Loja;
import model.LojaJogos;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

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
        LojaDAO lojaDAO;
        EmpresaDAO empresaDAO;
        JogoDAO jogoDAO;
        LojaJogosDAO lojaJogosDAO;
        Jogo jogo;
        List<ImmutablePair<Jogo, LojaJogos>> resultado = null;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()){
            case "/ListaJogos":
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    jogoDAO = daoFactory.getJogoDAO();

                    List<Jogo> lista_jogo = jogoDAO.all();
                    request.setAttribute("lista_jogo", resultado);

                } catch (ClassNotFoundException | IOException | SQLException ex){
                    request.getSession().setAttribute("error", ex.getMessage());
                }

                dispatcher = request.getRequestDispatcher("/view/loja/listaJogos.jsp");
                dispatcher.forward(request, response);
                break;

            case "/ListaJogos/Steam":
                try (DAOFactory daoFactory = DAOFactory.getInstance()){
                    lojaDAO = daoFactory.getLojaDAO();
                    lojaJogosDAO = daoFactory.getLojaJogosDAO();

                    Loja loja = lojaDAO.readByName("Steam");

                    resultado = lojaJogosDAO.getCrawlEntry(loja.getId(), null, null);
                    request.setAttribute("lista_jogo", resultado);

                } catch (ClassNotFoundException | IOException | SQLException ex){
                    request.getSession().setAttribute("error", ex.getMessage());
                }

                dispatcher = request.getRequestDispatcher("/view/loja/listaJogos.jsp");
                dispatcher.forward(request, response);
                break;

            case "/ListaJogos/Epic":
                try (DAOFactory daoFactory = DAOFactory.getInstance()){
                    lojaDAO = daoFactory.getLojaDAO();
                    lojaJogosDAO = daoFactory.getLojaJogosDAO();

                    Loja loja = lojaDAO.readByName("Epic");

                    resultado = lojaJogosDAO.getCrawlEntry(loja.getId(), null, null);
                    request.setAttribute("lista_jogo", resultado);

                } catch (ClassNotFoundException | IOException | SQLException ex){
                    request.getSession().setAttribute("error", ex.getMessage());
                }

                dispatcher = request.getRequestDispatcher("/view/loja/listaJogos.jsp");
                dispatcher.forward(request, response);
                break;

            case "/ListaJogos/Playstation":
                try (DAOFactory daoFactory = DAOFactory.getInstance()){
                    lojaDAO = daoFactory.getLojaDAO();
                    lojaJogosDAO = daoFactory.getLojaJogosDAO();

                    Loja loja = lojaDAO.readByName("Playstation");

                    resultado = lojaJogosDAO.getCrawlEntry(loja.getId(), null, null);
                    request.setAttribute("lista_jogo", resultado);

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
                    jogoDAO = daoFactory.getJogoDAO();
                    jogoDetail = jogoDAO.read(id);

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
