<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="${sessionScope.lang}" scope="session" />
<fmt:setBundle basename="local.messages" />

<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="nav.products" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="${sessionScope.theme}">
    <nav>
        <b><fmt:message key="nav.shop" /></b>
        <a href="${pageContext.request.contextPath}/products"><fmt:message key="nav.products" /></a>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/login"><fmt:message key="nav.login" /></a>
                <a href="${pageContext.request.contextPath}/register"><fmt:message key="nav.register" /></a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/orders"><fmt:message key="nav.orders" /></a>
                <a href="${pageContext.request.contextPath}/profile"><fmt:message key="nav.profile" /></a>
                <a href="${pageContext.request.contextPath}/logout"><fmt:message key="nav.logout" /></a>
            </c:otherwise>
        </c:choose>
        <div style="margin-left: auto; display: flex; gap: 15px; align-items: center;">
            <c:choose>
                <c:when test="${sessionScope.theme == 'dark'}">
                    <a href="${pageContext.request.contextPath}/theme?mode=white" id="theme-btn">☀️</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/theme?mode=dark" id="theme-btn">🌕</a>
                </c:otherwise>
            </c:choose>

            <div style="display: flex; gap: 10px;">
                <a href="${pageContext.request.contextPath}/lang?lang=ru" style="color: ${sessionScope.lang == 'en' ? '#aaa' : 'white'}; font-weight: ${sessionScope.lang == 'en' ? 'normal' : 'bold'}">RU</a>
                <a href="${pageContext.request.contextPath}/lang?lang=en" style="color: ${sessionScope.lang == 'en' ? 'white' : '#aaa'}; font-weight: ${sessionScope.lang == 'en' ? 'bold' : 'normal'}">EN</a>
            </div>
        </div>
    </nav>
    <main>
        <h1><fmt:message key="title.products" /></h1>

        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <section class="card">
                <h2><fmt:message key="admin.add_title" /></h2>
                <form action="${pageContext.request.contextPath}/products" method="post">
                    <input type="hidden" name="action" value="add">
                    <input name="name" placeholder="<fmt:message key='admin.name_placeholder' />" required>
                    <input name="description" placeholder="<fmt:message key='admin.desc_placeholder' />">
                    <input type="number" step="0.01" min="0" name="price" placeholder="<fmt:message key='admin.price_placeholder' />" required>
                    <input type="number" min="0" name="quantity" placeholder="<fmt:message key='admin.qty_placeholder' />" required>
                    <button><fmt:message key="admin.add_btn" /></button>
                </form>
            </section>
        </c:if>

        <section class="grid">
            <c:forEach var="p" items="${products}">
                <article class="card">
                    <h2>${p.name}</h2>
                    <p>${p.description}</p>
                    <p><b>${p.price}</b> ₽</p>
                    <p><fmt:message key="product.quantity" />: ${p.quantity}</p>

                    <c:if test="${p.quantity > 0}">
                        <form action="${pageContext.request.contextPath}/orders" method="post">
                            <input type="hidden" name="action" value="create">
                            <input type="hidden" name="productId" value="${p.id}">
                            <input type="number" name="quantity" min="1" max="${p.quantity}" value="1">
                            <button><fmt:message key="product.order_btn" /></button>
                        </form>
                    </c:if>

                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                        <form action="${pageContext.request.contextPath}/products" method="post">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${p.id}">
                            <button class="danger"><fmt:message key="product.delete_btn" /></button>
                        </form>
                    </c:if>
                </article>
            </c:forEach>
        </section>

        <c:if test="${noOfPages > 1}">
            <div class="card" id="pagination">
                <c:if test="${currentPage != 1}">
                    <a href="${pageContext.request.contextPath}/products?page=${currentPage - 1}"><fmt:message key="nav.back" /></a>
                </c:if>

                <span><fmt:message key="nav.page" /> ${currentPage} <fmt:message key="nav.from" /> ${noOfPages}</span>

                <c:if test="${currentPage < noOfPages}">
                    <a href="${pageContext.request.contextPath}/products?page=${currentPage + 1}"><fmt:message key="nav.next" /></a>
                </c:if>
            </div>
        </c:if>
    </main>
</body>
</html>
