<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="local.messages" />

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="nav.profile" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <nav>
        <b><fmt:message key="nav.shop" /></b>
        <a href="${pageContext.request.contextPath}/products"><fmt:message key="nav.products" /></a>
        <a href="${pageContext.request.contextPath}/orders"><fmt:message key="nav.orders" /></a>
        <a href="${pageContext.request.contextPath}/logout"><fmt:message key="nav.logout" /></a>

        <div style="margin-left: auto; display: flex; gap: 10px;">
            <a href="${pageContext.request.contextPath}/lang?lang=ru" style="color: ${sessionScope.lang == 'en' ? '#aaa' : 'white'}; font-weight: ${sessionScope.lang == 'en' ? 'normal' : 'bold'}">RU</a>
            <a href="${pageContext.request.contextPath}/lang?lang=en" style="color: ${sessionScope.lang == 'en' ? 'white' : '#aaa'}; font-weight: ${sessionScope.lang == 'en' ? 'bold' : 'normal'}">EN</a>
        </div>
    </nav>

    <main>
        <section class="card">
            <h1><fmt:message key="nav.profile" /></h1>

            <p class="error">${error}</p>

            <p><fmt:message key="profile.login_label" />: ${sessionScope.user.username}</p>

            <form action="${pageContext.request.contextPath}/profile" method="post">
                <input type="email" name="email" value="${sessionScope.user.email}" required>
                <button><fmt:message key="profile.save_btn" /></button>
            </form>
        </section>
    </main>

</body>
</html>
