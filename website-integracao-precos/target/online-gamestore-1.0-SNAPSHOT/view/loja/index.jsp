<%--
  Created by IntelliJ IDEA.
  User: iury
  Date: 11/02/2022
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/view/include/header.jsp"  %>
    <title>[Loja App] Lojas</title>
</head>
<body>
    <%@include file="/view/include/navbar.jsp" %>
    <div class="container">
        <table class="table mt-5">
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Jogos</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="loja" items="${requestScope.lista_loja}">
                    <tr>
                        <td>
                            <span><c:out value="${loja.nome}"/></span>
                        </td>
                        <td>
                            <a type="button" class="btn btn-primary" href="${pageContext.servletContext.contextPath}/ListaJogos/${loja.nome}" >
                                Visualizar
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <%@include file="/view/include/scripts.jsp"%>
</body>
</html>
