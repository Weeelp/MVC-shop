<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="local.messages" />

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="nav.orders" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <nav>
        <b><fmt:message key="nav.shop" /></b>
        <a href="${pageContext.request.contextPath}/products"><fmt:message key="nav.products" /></a>
        <a href="${pageContext.request.contextPath}/profile"><fmt:message key="nav.profile" /></a>
        <a href="${pageContext.request.contextPath}/logout"><fmt:message key="nav.logout" /></a>

        <div style="margin-left: auto; display: flex; gap: 10px;">
            <a href="${pageContext.request.contextPath}/lang?lang=ru" style="color: ${sessionScope.lang == 'en' ? '#aaa' : 'white'}; font-weight: ${sessionScope.lang == 'en' ? 'normal' : 'bold'}">RU</a>
            <a href="${pageContext.request.contextPath}/lang?lang=en" style="color: ${sessionScope.lang == 'en' ? 'white' : '#aaa'}; font-weight: ${sessionScope.lang == 'en' ? 'bold' : 'normal'}">EN</a>
        </div>
    </nav>

    <main>
        <h1><fmt:message key="nav.orders" /></h1>

        <p class="error">${error}</p>

        <c:forEach var="o" items="${orders}">
            <article class="card">
                <h2><fmt:message key="order.number" /> #${o.id}</h2>
                <p><fmt:message key="order.product" />: ${o.productName}</p>
                <p><fmt:message key="order.quantity" />: ${o.quantity}</p>
                <p><fmt:message key="order.sum" />: ${o.totalPrice} ₽</p>
                <p><fmt:message key="order.status" />: ${o.status}</p>

                <c:if test="${o.status == 'CREATED'}">
                    <form action="${pageContext.request.contextPath}/orders" method="post">
                        <input type="hidden" name="action" value="cancel">
                        <input type="hidden" name="id" value="${o.id}">
                        <button class="danger"><fmt:message key="order.cancel_btn" /></button>
                    </form>
                </c:if>
            </article>
        </c:forEach>
    </main>

</body>
</html>
