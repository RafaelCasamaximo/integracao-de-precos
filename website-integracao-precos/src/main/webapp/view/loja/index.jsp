<%--
  Created by IntelliJ IDEA.
  User: iury
  Date: 11/02/2022
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="content/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>[Loja App] Lojas</title>
</head>
<body>
    <div class="container">
        <table>
            <thead>
                <tr>
                    <th>Nome</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="loja" items="${requestScope.lista_loja}">
                    <tr>
                        <td>
                            <span><c:out value="${loja.nome}"/></span>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
