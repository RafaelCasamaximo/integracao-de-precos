<%--
  Created by IntelliJ IDEA.
  User: rafael
  Date: 13/02/2022
  Time: 11:27
  To change this template use File | Settings | File Templates.
--%>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" >
    <a class="navbar-brand" href="#">Game Drawer</a>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.servletContext.contextPath}">Lojas</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.servletContext.contextPath}/ListaJogos">Jogos</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.servletContext.contextPath}/Empresas">Empresas</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.servletContext.contextPath}/Pesquisa">Pesquisar</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.servletContext.contextPath}/Dados">Dados</a>
            </li>
        </ul>
    </div>
</nav>
<base href="${pageContext.servletContext.contextPath}" />