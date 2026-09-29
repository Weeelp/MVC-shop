<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="local.messages" />

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="nav.register" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <main class="card">
        <h1><fmt:message key="nav.register" /></h1>

        <p class="error">${error}</p>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <input name="username" placeholder="<fmt:message key='login.username_placeholder' />" required>
            <input type="email" name="email" placeholder="Email" required>
            <input type="password" name="password" placeholder="<fmt:message key='login.password_placeholder' />" required>
            <button><fmt:message key="nav.register" /></button>
        </form>

        <a href="${pageContext.request.contextPath}/login"><fmt:message key="nav.login" /></a>
    </main>
</body>
</html>
