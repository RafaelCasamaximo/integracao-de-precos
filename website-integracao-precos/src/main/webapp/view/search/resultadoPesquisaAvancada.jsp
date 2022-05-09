<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 07/05/2022
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/view/include/header.jsp"  %>
    <title>[Loja App] Lista</title>
</head>
<body class="bg-light">
<%@include file="/view/include/navbar.jsp" %>
<br><br>
<div class="container">
    <div class="card mt-5 mb-5">
        <div class="card-body">
            <table class="table">
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Data do Crawling</th>
                    <th>Loja</th>
                    <th>Preço</th>
                    <th>Detalhes</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="jogo" items="${requestScope.lista_lojaJogos}">
                    <tr>
                        <td>
                            <span><c:out value="${jogo.left.nome}"/></span>
                        </td>
                        <td>
                            <span><c:out value="${jogo.right.data_crawl}"/></span>
                        </td>
                        <td>
                            <span><c:out value="${jogo.right.loja_crawl}" /></span>
                        </td>
                        <td>
                            <span>R$<c:out value="${(jogo.right.preco_jogo / 100)}" /></span>
                        </td>

                        <td>
                            <!-- Button trigger modal -->
                            <a
                                    type="button"
                                    class="btn btn-primary link_visualizar_jogo"
                                    data-toggle="modal"
                                    data-target="#gameDetailModal"
                                    data-href="${pageContext.servletContext.contextPath}/ListaJogos/read?id_jogo=${jogo.right.id_jogo}&id_loja=${jogo.right.id_loja}&data_crawl=${jogo.right.data_crawl}"
                                    href="#"
                            >
                                Detalhes
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade modal-visualizar-jogo" id="gameDetailModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLongTitle">Descrição de Jogo</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="card mt-3">
                                <div class="card-body">
                                    <h5 class="card-title">Produto</h5>
                                    <p class="p_id"></p>
                                    <p class="p_nome"></p>
                                    <p class="p_idEmpresa"></p>
                                    <p class="p_empresa"></p>
                                </div>
                            </div>
                            <div class="card mt-3">
                                <div class="card-body">
                                    <h5 class="card-title">Loja</h5>
                                    <p class="p_lojaCrawl"></p>
                                    <p class="p_preco"></p>
                                </div>
                            </div>
                            <div class="card mt-3">
                                <div class="card-body">
                                    <h5 class="card-title">Detalhes</h5>
                                    <p class="p_genero"></p>
                                    <p class="p_linguagens"></p>
                                    <p class="p_gratuito"></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
            </div>
        </div>
    </div>
</div>


<%@include file="/view/include/scripts.jsp"%>
<script src="${pageContext.servletContext.contextPath}/assets/js/jogo.js"></script>
</body>
</html>
