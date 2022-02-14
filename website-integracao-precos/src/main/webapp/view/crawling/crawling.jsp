<%--
  Created by IntelliJ IDEA.
  User: rafael
  Date: 13/02/2022
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="/view/include/header.jsp"  %>
    <title>[Loja App] Dados</title>
</head>
<body>
    <%@include file="/view/include/navbar.jsp" %>
    <div class="container">
        <div class="card text-center mt-5">
            <div class="card-header">
                Detalhes sobre atualização
            </div>
            <div class="card-body">
                <p class="card-text">
                    Os dados foram atualizados em: dd/mm/aa às hh:mm:ss
                </p>
                <form
                    class="form"
                    action="${pageContext.servletContext.contextPath}/Dados/Solicitacao"
                    method="POST">
                    <button class="btn btn-primary" type="submit">Solicitar Atualização</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>