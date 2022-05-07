<%--
  Created by IntelliJ IDEA.
  User: rafael
  Date: 13/02/2022
  Time: 11:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/view/include/header.jsp"  %>
    <title>[Loja App] Empresas</title>
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
                    <th>ID</th>
                    <th>Nome</th>
<%--                    <th>Descrição Curta</th>--%>
                    <th>Número de Jogos</th>
<%--                    <th>Website</th>--%>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="empresa" items="${requestScope.lista_empresa}">
                    <tr>
                        <td>
                            <span><c:out value="${empresa.id}"/></span>
                        </td>
                        <td>
                            <span><c:out value="${empresa.nome}"/></span>
                        </td>
<%--                        <td>--%>
<%--                                &lt;%&ndash;                    <span><c:out value="${empresa.descricao_curta}"/></span>&ndash;%&gt;--%>
<%--                            <span>Indisponível</span>--%>
<%--                        </td>--%>
                        <td>
                            <span><c:out value="${empresa.numero_jogos}"/></span>
                        </td>
<%--                        <td>--%>
<%--                                &lt;%&ndash;                    <span><c:out value="${empresa.website}"/></span>&ndash;%&gt;--%>
<%--                            <span>Indisponível</span>--%>
<%--                        </td>--%>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@include file="/view/include/scripts.jsp"%>
</body>
</html>
