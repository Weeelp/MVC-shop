<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="local.messages" />

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="nav.login" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <main class="card">
        <h1><fmt:message key="nav.login" /></h1>

        <p class="error">${error}</p>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input name="username" placeholder="<fmt:message key='admin.name_placeholder' />" required>
            <input type="password" name="password" placeholder="<fmt:message key='admin.qty_placeholder' />" required>
            <button><fmt:message key="nav.login" /></button>
        </form>

        <a href="${pageContext.request.contextPath}/register"><fmt:message key="nav.register" /></a>
    </main>
</body>
</html>
