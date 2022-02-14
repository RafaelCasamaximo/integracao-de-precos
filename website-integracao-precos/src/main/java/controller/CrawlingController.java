package controller;

import JSONHelper.EmpresaEntry;
import JSONHelper.JSONEntry;
import JSONHelper.JogoEntry;
import JSONHelper.ListaEmpresaEntry;
import com.google.gson.*;
import dao.DAO;
import dao.DAOFactory;
import dao.LojaJogosDAO;
import model.Empresa;
import model.Jogo;
import model.Loja;
import model.LojaJogos;
import org.apache.commons.io.IOUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet(name = "CrawlingController", urlPatterns = {
        "/Dados",
        "/Dados/Solicitacao"
})
public class CrawlingController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        switch (request.getServletPath()){
            case "/Dados":
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {

                } catch (ClassNotFoundException | IOException | SQLException ex){
                    request.getSession().setAttribute("error", ex.getMessage());
                }

                dispatcher = request.getRequestDispatcher("/view/crawling/crawling.jsp");
                dispatcher.forward(request, response);
                break;
        }
    }

    protected void updateDatabase() throws IOException, InterruptedException, ParseException, ParseException, JSONException, SQLException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("crawlerOutput-06-02-2022 12-16-06.json");
        String result = IOUtils.toString(is);

        // Pega o objetos json de cada coleção
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(result);
        JsonObject steamJsonObject = (JsonObject) jsonObject.get("steamCollection");
        JsonObject epicJsonObject = (JsonObject) jsonObject.get("epicCollection");
        JsonObject playstationJsonObject = (JsonObject) jsonObject.get("playstationCollection");

        JSONEntry jsonEntry = new JSONEntry();

        // Pega data time e games array steam
        jsonEntry.steam.date = steamJsonObject.get("date").getAsString();
        jsonEntry.steam.time = steamJsonObject.get("time").getAsString();
        JsonArray steamGamesArray = steamJsonObject.get("games").getAsJsonArray();

        // Pega data time e games array epic
        jsonEntry.epic.date = epicJsonObject.get("date").getAsString();
        jsonEntry.epic.time = epicJsonObject.get("time").getAsString();
        JsonArray epicGamesArray = epicJsonObject.get("games").getAsJsonArray();


        // Pega data time e games array playstation
        jsonEntry.playstation.date = playstationJsonObject.get("date").getAsString();
        jsonEntry.playstation.time = playstationJsonObject.get("time").getAsString();
        JsonArray playstationGamesArray = playstationJsonObject.get("games").getAsJsonArray();

        // Trata array steam
        jsonEntry.steam.JsonArrayToList(new JSONArray(steamGamesArray.toString()));
        jsonEntry.epic.JsonArrayToList(new JSONArray(epicGamesArray.toString()));
        jsonEntry.playstation.JsonArrayToList(new JSONArray(playstationGamesArray.toString()));

        //Instancia DAOs
        DAO<Jogo> jogoDAO;
        DAO<Loja> lojaDAO;
        DAO<Empresa> empresaDAO;
        DAO<LojaJogos> lojaJogosDAO;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            jogoDAO = daoFactory.getJogoDAO();
            lojaDAO = daoFactory.getLojaDAO();
            empresaDAO = daoFactory.getEmpresaDAO();
            lojaJogosDAO = daoFactory.getLojaJogosDAO();

            //        For steam
            Loja steamLoja = new Loja(0, "Steam");
            Loja epicLoja = new Loja(1, "Epic");
            Loja playstationLoja = new Loja(2, "Playstation");

            //Cria ou dá update nas lojas
            try {
                lojaDAO.update(steamLoja);
            } catch (SQLException ex){
                lojaDAO.create(steamLoja);
            }

            try {
                lojaDAO.update(epicLoja);
            } catch (SQLException ex){
                lojaDAO.create(epicLoja);
            }

            try {
                lojaDAO.update(playstationLoja);
            } catch (SQLException ex){
                lojaDAO.create(playstationLoja);
            }
        }catch (ClassNotFoundException | IOException | SQLException ex){
            throw new SQLException("Erro ao atualizar database.");
        }

        ListaEmpresaEntry listaEmpresaEntry = new ListaEmpresaEntry(
                new JSONArray(steamGamesArray.toString()),
                new JSONArray(epicGamesArray.toString()),
                new JSONArray(playstationGamesArray.toString())
        );

        for (EmpresaEntry empresaEntryAux : listaEmpresaEntry.empresas) {
            Empresa empresa = new Empresa();
            empresa.setId(Integer.parseInt(empresaEntryAux.id));
            empresa.setDescricao_curta("");
            empresa.setNumero_jogos(0);
            empresa.setWebsite("");
//            try {
//                empresaDAO.update(empresa);
//            }catch (SQLException ex){
//                empresaDAO.create(empresa);
//            }
        }

        /*
        *
        * Percorrer todos os jogos de X loja
        * - Caso o jogo não exista, insere novo item
        * -- Caso o jogo já exista, insere novo item lojaTabela ou atualiza
        *
        * */

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String servletPath = request.getServletPath();
        switch (request.getServletPath()){
            case "/Dados/Solicitacao":

                try {
                    this.updateDatabase();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
//                Thread updateDBThread = new Thread(() -> {
//                    //Código que leva tempo aqui
//
//                });
//                updateDBThread.start();

                response.sendRedirect(request.getContextPath() + "/ListaJogos");
                break;

        }
    }
}
