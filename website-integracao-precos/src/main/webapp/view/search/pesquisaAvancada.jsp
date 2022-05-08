<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 07/05/2022
  Time: 14:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/view/include/header.jsp"  %>
    <title>[Loja App] Pesquisar</title>
</head>
<body class="bg-light">
<%@include file="/view/include/navbar.jsp" %>
<br><br>
<div class="container">
    <div class="card mt-5 mb-5">
        <div class="card-body">
            <h3 class="card-title">Pesquisa Avançada</h3>
            <h6 class="card-subtitle mb-2 text-muted">Use essa área para procurar jogos e filtrar de acordo com sua vontade!</h6>
            <br>
            <br>
            <form
                    class="form"
                    action="${pageContext.servletContext.contextPath}/RequisicaoPesquisa"
                    method="POST"
            >
                <div class="form-group">
                    <label for="inputGameName">Nome do jogo</label>
                    <input name="gameName" type="text" class="form-control" id="inputGameName" aria-describedby="gameNameHelp" placeholder="Insira o nome do jogo">
                    <small id="gameNameHelp" class="form-text text-muted">A pesquisa irá ignorar maiúsculas e minúsculas.</small>
                </div>
                <div class="form-row">
                    <div class="col">
                        <label for="inputGameCompany">Nome da empresa</label>
                        <input name="gameCompany" type="text" class="form-control" id="inputGameCompany" placeholder="Insira o nome da empresa">
                        <small id="gameCompanyHelp" class="form-text text-muted">A pesquisa irá ignorar maiúsculas e minúsculas.</small>

                    </div>
                    <div class="col">
                        <label for="inputGameStore">Nome da loja</label>
                        <input name="gameStore" type="text" class="form-control" id="inputGameStore" placeholder="Insira o nome da Loja">
                        <small id="gameStoreHelp" class="form-text text-muted">A pesquisa irá ignorar maiúsculas e minúsculas.</small>

                    </div>
                </div>
                <br>
                <div class="form-group">
                    <label for="inputGameGenre">Gênero do jogo</label>
                    <input name="gameGenre" type="text" class="form-control" id="inputGameGenre" aria-describedby="gameGenreHelp" placeholder="Insira o gênero do jogo">
                    <small id="inputGameGenreHelp" class="form-text text-muted">A pesquisa irá ignorar maiúsculas, minúsculas e acentos. Palavras em diferentes linguas alteram o resultado. Mais de um gênero pode ser separado por um espaço. Caso o campo esteja em branco ele será ignorado.</small>
                </div>
                <div class="form-row">
                    <div class="col">
                        <label for="inputMinimumPrice">Preço Mínimo</label>
                        <input name="gameMinPrice" type="number" min="0.00" max="10000.00" step="0.01" class="form-control" id="inputMinimumPrice" placeholder="Insira o preço mínimo do jogo em reais.">
                        <small id="inputMinimumPriceHelp" class="form-text text-muted">O campo será ignorado caso o valor seja 0.</small>
                    </div>
                    <div class="col">
                        <label for="inputMaximumPrice">Preço Máximo</label>
                        <input name="gameMaxPrice" type="number" min="0.00" max="10000.00" step="0.01" class="form-control" id="inputMaximumPrice" placeholder="Insira o preço máximo do jogo em reais.">
                        <small id="inputMaximumPriceHelp" class="form-text text-muted">O campo será ignorado caso o valor seja 0.</small>
                    </div>
                </div>
                <div class="form-row">
                    <div class="col">
                        <label for="inputMinimumDiscount">Desconto Mínimo</label>
                        <input name="gameMinDiscount" type="number" min="0.00" max="10000.00" step="0.01" class="form-control" id="inputMinimumDiscount" placeholder="Insira o desconto mínimo do jogo em reais.">
                        <small id="inputMinimumDiscountHelp" class="form-text text-muted">O campo será ignorado caso o valor seja 0.</small>
                    </div>
                    <div class="col">
                        <label for="inputMaximumDiscount">Desconto Máximo</label>
                        <input name="gameMaxDiscount" type="number" min="0.00" max="10000.00" step="0.01" class="form-control" id="inputMaximumDiscount" placeholder="Insira o desconto máximo do jogo em reais.">
                        <small id="inputMaximumDiscountHelp" class="form-text text-muted">O campo será ignorado caso o valor seja 0.</small>
                    </div>
                </div>
                <br>
                <div class="form-group">
                    <div class="form-check">
                        <input name="gameIsFree" class="form-check-input" type="checkbox" value="" id="inputIsFree">
                        <label class="form-check-label" for="inputIsFree">
                            Gratuíto
                        </label>
                        <small id="inputIsFreeHelp" class="form-text text-muted">Caso o campo seja marcado a busca irá ignorar preços e descontos.</small>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary">Pesquisar</button>
            </form>
        </div>
    </div>
</div>


<%@include file="/view/include/scripts.jsp"%>
</body>
</html>
