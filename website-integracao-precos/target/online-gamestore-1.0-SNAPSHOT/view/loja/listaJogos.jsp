<%--
  Created by IntelliJ IDEA.
  User: rafael
  Date: 13/02/2022
  Time: 10:25
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
<body>
<%@include file="/view/include/navbar.jsp" %>

<div class="container">
    <table class="table mt-5">
        <thead>
        <tr>
            <th>Nome</th>
            <th>Detalhes</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="jogo" items="${requestScope.lista_jogo}">
            <tr>
                <td>
                    <span><c:out value="${jogo.left.nome}"/></span>
                </td>
                <td>
                    <!-- Button trigger modal -->
                    <a
                            type="button"
                            class="btn btn-primary link_visualizar_jogo"
                            data-toggle="modal"
                            data-target="#gameDetailModal"
                            data-href="${pageContext.servletContext.contextPath}/ListaJogos/read?id=${jogo.left.id}"
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
<!-- Modal -->
<div class="modal fade modal-visualizar-jogo" id="gameDetailModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
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
                            <p class="p_id"></p>
                            <p class="p_nome"></p>
                            <p class="p_genero"></p>
                            <p class="p_linguagens"></p>
                            <p class="p_suporte"></p>
                            <p class="p_empresa"></p>
                            <p class="p_gratuito"></p>
                            <p class="p_idade"></p>
                            <p class="p_descricaoCurta"></p>
                            <p class="p_descricaoLonga"></p>
                            <p class="p_idEmpresa"></p>
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
