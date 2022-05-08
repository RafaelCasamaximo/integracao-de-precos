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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
        LojaJogosDAO lojaJogosDAO;
        RequestDispatcher dispatcher;
        switch (request.getServletPath()){
            case "/Dados":
                try (DAOFactory daoFactory = DAOFactory.getInstance()) {
                    lojaJogosDAO = daoFactory.getLojaJogosDAO();
                    List<ImmutablePair<Jogo, LojaJogos>> lista_lojaJogos = lojaJogosDAO.getAllEntries();
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String lastUpdateDate = df.format(getLastUpdateDate(lista_lojaJogos));
                    if(lastUpdateDate.equals("01/01/2001"))
                    {
                        request.setAttribute("lastUpdateDate", "SEM ATUALIZAÇÃO RECENTE!");
                    }
                    else
                    {
                        request.setAttribute("lastUpdateDate", lastUpdateDate);
                    }

                } catch (ClassNotFoundException | IOException | SQLException ex){
                    request.getSession().setAttribute("error", ex.getMessage());
                }

                dispatcher = request.getRequestDispatcher("/view/crawling/crawling.jsp");
                dispatcher.forward(request, response);
                break;
        }
    }

    public java.sql.Date getLastUpdateDate(List<ImmutablePair<Jogo, LojaJogos>> lista_lojaJogos)
    {
        ArrayList<java.sql.Date> lista_date = new ArrayList<java.sql.Date>();
        for (ImmutablePair<Jogo, LojaJogos> lojaJogo : lista_lojaJogos) {
            lista_date.add(lojaJogo.right.getData_crawl());
        }
        //Date maxDate = list.stream().map(u -> u.date).max(Date::compareTo).get();
        Collections.sort(lista_date);
        java.sql.Date maxDate;
        try{
            maxDate = lista_date.get(lista_date.size() -1 );
        }catch (IndexOutOfBoundsException e){
            Date dateAux = new GregorianCalendar(2001, Calendar.JANUARY, 1).getTime();
            maxDate = new java.sql.Date(dateAux.getTime());
        }
        return maxDate;
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


    protected void updateDatabase() throws IOException, InterruptedException, ParseException, ParseException, JSONException, SQLException {
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        /*
        * Realiza o processo de leitura, streaming e deserializing do JSON do crawling,
        * transformando todo o conteudo em objetos java para serem processados
        * */

        InputStream is = getClass().getClassLoader().getResourceAsStream("crawlerOutput.json");
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
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            /*
             * Le todas as lojas existentes no banco de dados (devem haver exatamente 3 lojas)
             * Verificar se loja já existe. Caso existe não cria uma nova
             * */
            List<Loja> listaLojas = null;
            Loja steamLoja = new Loja(0, "Steam");
            Loja epicLoja = new Loja(1, "Epic");
            Loja playstationLoja = new Loja(2, "Playstation");
            try {
                listaLojas = lojaDAO.all();
            }catch (SQLException ex){

            }
            boolean createSteamStore = true;
            boolean createEpicStore = true;
            boolean createPlaystationStore = true;
            for (Loja loja : listaLojas) {
                // Compara o nome das duas strings porque por algum motivo o banco de dados retorna completando 300 char com espaço
                if(loja.getNome().replaceAll("\\s+","").equalsIgnoreCase("Steam"))
                {
                    createSteamStore = false;
                }
                if(loja.getNome().replaceAll("\\s+","").equalsIgnoreCase("Epic"))
                {
                    createEpicStore = false;
                }
                if(loja.getNome().replaceAll("\\s+","").equalsIgnoreCase("Playstation"))
                {
                    createPlaystationStore = false;
                }
            }
            if(createSteamStore)
            {
                try {
                    lojaDAO.create(steamLoja);
                }catch (SQLException ex){
                }
            }
            if(createEpicStore)
            {
                try {
                    lojaDAO.create(epicLoja);
                }catch (SQLException ex){
                }
            }
            if(createPlaystationStore)
            {
                try {
                    lojaDAO.create(playstationLoja);
                }catch (SQLException ex){
                }
            }
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            /*
            * Le as diferentes empresas que existem na steam, epic e playstation
            * Compara para ver se já existem empresas com o mesmo nome. Caso exista ele não adiciona
            * */
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
                empresa.setNumero_jogos(empresaEntryAux.num_jogos);
                empresa.setWebsite("");

                // Caso já exista uma empresa com o nome não adiciona. Caso não exista cria uma nova empresa
                try {
                    empresaDAO.readByName(empresa.getNome());
                }catch (SQLException ex){
                    try {
                        empresaDAO.create(empresa);
                    }catch (SQLException ex2){
                    }
                }
            }
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

            // Itera sobre todos os titulos do crawling vendo quais já foram adicionados
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

                // Caso o titulo não exista no BD ele é inserido e caso exista ele NÃO duplica
                try {
                    jogoDAO.readByName(jogo.getNome());
                }catch (SQLException ex){
                    try {
                        jogoDAO.create(jogo);
                    }catch (SQLException ex2){
                    }
                }

                // Ler o jogo inserido para poder criar ou não o lojaJogo equivalente
                Jogo jogoCriado = jogoDAO.readByName(jogoAux.nome);
                Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonEntry.steam.date);
                LojaJogos lojaJogos = new LojaJogos();
                lojaJogos.setId_jogo(jogoCriado.getId());
                lojaJogos.setLoja_crawl("Steam");
                lojaJogos.setId_loja(lojaDAO.readByName("Steam").getId());
                lojaJogos.setPreco_jogo((float)jogoAux.preco_disconto);
                lojaJogos.setData_crawl(new java.sql.Date(utilDate.getTime()));

                try {
                    LojaJogos ultimoRegistroLojaJogo = lojaJogosDAO.readByStoreIDGameIDAndDate(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
                    System.out.println("Já existe um registro com esse ID de loja, jogo e data de crawling: " + ultimoRegistroLojaJogo.getId_jogo() + ultimoRegistroLojaJogo.getId_loja() + ultimoRegistroLojaJogo.getData_crawl() + ultimoRegistroLojaJogo.getPreco_jogo());
                }catch (Exception ex){
                    try {
                        lojaJogosDAO.create(lojaJogos);
                        System.out.println("Criando uma nova lojaJogo porque algum dado nao coincide");
                    } catch (SQLException e) {
                    }
                }


//                try {
//
//                    // O jogo e o preço tão dentro disso aqui!!!
//                    List<ImmutablePair<Jogo, LojaJogos>> res = lojaJogosDAO.getCrawlEntry(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
//                }catch (Exception e){
//                }
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

                // Caso o titulo não exista no BD ele é inserido e caso exista ele NÃO duplica
                try {
                    jogoDAO.readByName(jogo.getNome());
                }catch (SQLException ex){
                    try {
                        jogoDAO.create(jogo);
                    }catch (SQLException ex2){
                    }
                }

                // Ler o jogo inserido para poder criar ou não o lojaJogo equivalente
                Jogo jogoCriado = jogoDAO.readByName(jogoAux.nome);
                Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonEntry.epic.date);
                LojaJogos lojaJogos = new LojaJogos();
                lojaJogos.setId_jogo(jogoCriado.getId());
                lojaJogos.setLoja_crawl("Epic");
                lojaJogos.setId_loja(lojaDAO.readByName("Epic").getId());
                lojaJogos.setPreco_jogo((float)jogoAux.preco_disconto);
                lojaJogos.setData_crawl(new java.sql.Date(utilDate.getTime()));

                try {
                    LojaJogos ultimoRegistroLojaJogo = lojaJogosDAO.readByStoreIDGameIDAndDate(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
                }catch (Exception ex){
                    try {
                        lojaJogosDAO.create(lojaJogos);
                    } catch (SQLException e) {
                    }
                }


//                try {
//
//                    // O jogo e o preço tão dentro disso aqui!!!
//                    List<ImmutablePair<Jogo, LojaJogos>> res = lojaJogosDAO.getCrawlEntry(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
//                }catch (Exception e){
//                }
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

                // Caso o titulo não exista no BD ele é inserido e caso exista ele NÃO duplica
                try {
                    jogoDAO.readByName(jogo.getNome());
                }catch (SQLException ex){
                    try {
                        jogoDAO.create(jogo);
                    }catch (SQLException ex2){
                    }
                }

                // Ler o jogo inserido para poder criar ou não o lojaJogo equivalente
                Jogo jogoCriado = jogoDAO.readByName(jogoAux.nome);
                Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonEntry.playstation.date);
                LojaJogos lojaJogos = new LojaJogos();
                lojaJogos.setId_jogo(jogoCriado.getId());
                lojaJogos.setLoja_crawl("Playstation");
                lojaJogos.setId_loja(lojaDAO.readByName("Playstation").getId());
                lojaJogos.setPreco_jogo((float)jogoAux.preco_disconto);
                lojaJogos.setData_crawl(new java.sql.Date(utilDate.getTime()));

                try {
                    LojaJogos ultimoRegistroLojaJogo = lojaJogosDAO.readByStoreIDGameIDAndDate(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
                }catch (Exception ex){
                    try {
                        lojaJogosDAO.create(lojaJogos);
                    } catch (SQLException e) {
                    }
                }


//                try {
//
//                    // O jogo e o preço tão dentro disso aqui!!!
//                    List<ImmutablePair<Jogo, LojaJogos>> res = lojaJogosDAO.getCrawlEntry(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
//                }catch (Exception e){
//                }
            }
//
//            for (JogoEntry jogoAux : jsonEntry.epic.jogos) {
//                Jogo jogo = new Jogo();
//                jogo.setNome(jogoAux.nome);
//                jogo.setGenero(jogoAux.genero);
//                jogo.setLinguagens_suportadas(jogoAux.linguagens_suportadas);
//                jogo.setSuporte_a_controle(jogoAux.suporte_a_controle);
//                jogo.setNome_empresa(jogoAux.nome_empresa);
//                jogo.setGratuito(jogoAux.gratuito);
//                jogo.setIdade_requerida(jogoAux.idade_requerida);
//                jogo.setDescricao_curta(jogoAux.descricao_curta);
//                jogo.setDescricao_longa(jogoAux.descricao_longa);
//                jogo.setId_empresa(empresaDAO.readByName(jogoAux.nome_empresa).getId());
//                try {
//                    jogoDAO.create(jogo);
//                }catch (SQLException ex){
//
//                }
//
//                Jogo jogoCriado = jogoDAO.readByName(jogoAux.nome);
//                Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonEntry.epic.date);
//                LojaJogos lojaJogos = new LojaJogos();
//                lojaJogos.setId_jogo(jogoCriado.getId());
//                lojaJogos.setLoja_crawl("Epic");
//                lojaJogos.setId_loja(lojaDAO.readByName("Epic").getId());
//                lojaJogos.setPreco_jogo((float)jogoAux.preco_disconto);
//                lojaJogos.setData_crawl(new java.sql.Date(utilDate.getTime()));
//
//                try {
//                    lojaJogosDAO.create(lojaJogos);
//                    // O jogo e o preço tão dentro disso aqui!!!
//                    List<ImmutablePair<Jogo, LojaJogos>> res = lojaJogosDAO.getCrawlEntry(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
//                }catch (Exception e){
//
//                }
//
//            }
//
//            for (JogoEntry jogoAux : jsonEntry.playstation.jogos) {
//                Jogo jogo = new Jogo();
//                jogo.setNome(jogoAux.nome);
//                jogo.setGenero(jogoAux.genero);
//                jogo.setLinguagens_suportadas(jogoAux.linguagens_suportadas);
//                jogo.setSuporte_a_controle(jogoAux.suporte_a_controle);
//                jogo.setNome_empresa(jogoAux.nome_empresa);
//                jogo.setGratuito(jogoAux.gratuito);
//                jogo.setIdade_requerida(jogoAux.idade_requerida);
//                jogo.setDescricao_curta(jogoAux.descricao_curta);
//                jogo.setDescricao_longa(jogoAux.descricao_longa);
//                jogo.setId_empresa(empresaDAO.readByName(jogoAux.nome_empresa).getId());
//                try {
//                    jogoDAO.create(jogo);
//                }catch (SQLException ex){
//
//                }
//
//                Jogo jogoCriado = jogoDAO.readByName(jogoAux.nome);
//                Date utilDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonEntry.playstation.date);
//                LojaJogos lojaJogos = new LojaJogos();
//                lojaJogos.setId_jogo(jogoCriado.getId());
//                lojaJogos.setLoja_crawl("Playstation");
//                lojaJogos.setId_loja(lojaDAO.readByName("Playstation").getId());
//                lojaJogos.setPreco_jogo((float)jogoAux.preco_disconto);
//                lojaJogos.setData_crawl(new java.sql.Date(utilDate.getTime()));
//
//                try {
//                    lojaJogosDAO.create(lojaJogos);
//                    // O jogo e o preço tão dentro disso aqui!!!
//                    List<ImmutablePair<Jogo, LojaJogos>> res = lojaJogosDAO.getCrawlEntry(lojaJogos.getId_loja(), lojaJogos.getId_jogo(), lojaJogos.getData_crawl());
//                }catch (Exception e){
//
//                }
//
//            }

        }catch (ClassNotFoundException | IOException | SQLException ex){
            throw new SQLException("Erro ao atualizar database.");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }


}
