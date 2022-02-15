package controller;

import JSONHelper.EmpresaEntry;
import JSONHelper.JSONEntry;
import JSONHelper.JogoEntry;
import JSONHelper.ListaEmpresaEntry;
import com.google.gson.*;
import dao.*;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
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
        JogoDAO jogoDAO;
        LojaDAO lojaDAO;
        EmpresaDAO empresaDAO;
        LojaJogosDAO lojaJogosDAO;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            // Instancias DAO
            jogoDAO = daoFactory.getJogoDAO();
            lojaDAO = daoFactory.getLojaDAO();
            empresaDAO = daoFactory.getEmpresaDAO();
            lojaJogosDAO = daoFactory.getLojaJogosDAO();

            Loja steamLoja = new Loja(0, "Steam");
            Loja epicLoja = new Loja(1, "Epic");
            Loja playstationLoja = new Loja(2, "Playstation");

            try {
                lojaDAO.create(steamLoja);
            }catch (SQLException ex){

            }

            try {
                lojaDAO.create(epicLoja);
            }catch (SQLException ex){

            }

            try {
                lojaDAO.create(playstationLoja);
            }catch (SQLException ex){

            }

            ListaEmpresaEntry listaEmpresaEntry = new ListaEmpresaEntry(
                    new JSONArray(steamGamesArray.toString()),
                    new JSONArray(epicGamesArray.toString()),
                    new JSONArray(playstationGamesArray.toString())
            );

            for (EmpresaEntry empresaEntryAux : listaEmpresaEntry.empresas) {
                Empresa empresa = new Empresa();
                empresa.setNome(empresaEntryAux.nome);
                empresa.setId(Integer.parseInt(empresaEntryAux.id));
                empresa.setDescricao_curta("");
                empresa.setNumero_jogos(0);
                empresa.setWebsite("");

                try {
                    empresaDAO.create(empresa);
                }catch (SQLException ex){

                }
            }


            for (JogoEntry jogoAux : jsonEntry.steam.jogos) {
                Jogo jogo = new Jogo();
                jogo.setNome(jogoAux.nome);
                jogo.setGenero(jogoAux.genero);
                jogo.setLinguagens_suportadas(jogoAux.linguagens_suportadas);
                jogo.setSuporte_a_controle(jogoAux.suporte_a_controle);
                jogo.setNome_empresa(jogoAux.nome_empresa);
                jogo.setGratuito(jogoAux.gratuito);
                jogo.setIdade_requerida(jogoAux.idade_requerida);
                jogo.setDescricao_curta(jogoAux.descricao_curta);
                jogo.setDescricao_longa(jogoAux.descricao_longa);
                jogo.setId_empresa(empresaDAO.readByName(jogoAux.nome_empresa).getId());
                try {
                    jogoDAO.create(jogo);
                }catch (SQLException ex){

                }

                Jogo jogoCriado = jogoDAO.readByName(jogoAux.nome);
                Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonEntry.steam.date);
                LojaJogos lojaJogos = new LojaJogos();
                lojaJogos.setId_jogo(jogoCriado.getId());
                lojaJogos.setLoja_crawl("Steam");
                lojaJogos.setId_loja(lojaDAO.readByName("Steam").getId());
                lojaJogos.setPreco_jogo((float)jogoAux.preco_disconto);
                lojaJogos.setData_crawl(new java.sql.Date(utilDate.getTime()));

                try {
                    lojaJogosDAO.create(lojaJogos);
                    // O jogo e o preço tão dentro disso aqui!!!
                    List<ImmutablePair<Jogo, LojaJogos>> res = lojaJogosDAO.getCrawlEntry(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
                }catch (Exception e){

                }
            }

            for (JogoEntry jogoAux : jsonEntry.epic.jogos) {
                Jogo jogo = new Jogo();
                jogo.setNome(jogoAux.nome);
                jogo.setGenero(jogoAux.genero);
                jogo.setLinguagens_suportadas(jogoAux.linguagens_suportadas);
                jogo.setSuporte_a_controle(jogoAux.suporte_a_controle);
                jogo.setNome_empresa(jogoAux.nome_empresa);
                jogo.setGratuito(jogoAux.gratuito);
                jogo.setIdade_requerida(jogoAux.idade_requerida);
                jogo.setDescricao_curta(jogoAux.descricao_curta);
                jogo.setDescricao_longa(jogoAux.descricao_longa);
                jogo.setId_empresa(empresaDAO.readByName(jogoAux.nome_empresa).getId());
                try {
                    jogoDAO.create(jogo);
                }catch (SQLException ex){

                }

                Jogo jogoCriado = jogoDAO.readByName(jogoAux.nome);
                Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonEntry.steam.date);
                LojaJogos lojaJogos = new LojaJogos();
                lojaJogos.setId_jogo(jogoCriado.getId());
                lojaJogos.setLoja_crawl("Epic");
                lojaJogos.setId_loja(lojaDAO.readByName("Epic").getId());
                lojaJogos.setPreco_jogo((float)jogoAux.preco_disconto);
                lojaJogos.setData_crawl(new java.sql.Date(utilDate.getTime()));

                try {
                    lojaJogosDAO.create(lojaJogos);
                    // O jogo e o preço tão dentro disso aqui!!!
                    List<ImmutablePair<Jogo, LojaJogos>> res = lojaJogosDAO.getCrawlEntry(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
                }catch (Exception e){

                }

            }

            for (JogoEntry jogoAux : jsonEntry.playstation.jogos) {
                Jogo jogo = new Jogo();
                jogo.setNome(jogoAux.nome);
                jogo.setGenero(jogoAux.genero);
                jogo.setLinguagens_suportadas(jogoAux.linguagens_suportadas);
                jogo.setSuporte_a_controle(jogoAux.suporte_a_controle);
                jogo.setNome_empresa(jogoAux.nome_empresa);
                jogo.setGratuito(jogoAux.gratuito);
                jogo.setIdade_requerida(jogoAux.idade_requerida);
                jogo.setDescricao_curta(jogoAux.descricao_curta);
                jogo.setDescricao_longa(jogoAux.descricao_longa);
                jogo.setId_empresa(empresaDAO.readByName(jogoAux.nome_empresa).getId());
                try {
                    jogoDAO.create(jogo);
                }catch (SQLException ex){

                }

                Jogo jogoCriado = jogoDAO.readByName(jogoAux.nome);
                Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonEntry.steam.date);
                LojaJogos lojaJogos = new LojaJogos();
                lojaJogos.setId_jogo(jogoCriado.getId());
                lojaJogos.setLoja_crawl("Playstation");
                lojaJogos.setId_loja(lojaDAO.readByName("Playstation").getId());
                lojaJogos.setPreco_jogo((float)jogoAux.preco_disconto);
                lojaJogos.setData_crawl(new java.sql.Date(utilDate.getTime()));

                try {
                    lojaJogosDAO.create(lojaJogos);
                    // O jogo e o preço tão dentro disso aqui!!!
                    List<ImmutablePair<Jogo, LojaJogos>> res = lojaJogosDAO.getCrawlEntry(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
                }catch (Exception e){

                }

            }

        }catch (ClassNotFoundException | IOException | SQLException ex){
            throw new SQLException("Erro ao atualizar database.");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }





        /*
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
