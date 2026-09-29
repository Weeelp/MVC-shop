package com.shop.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Locale;

@WebFilter("/*")
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        httpRequest.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        jakarta.servlet.http.HttpSession session = httpRequest.getSession(true);

        String lang = (String) session.getAttribute("lang");
        if (lang == null) { lang = "ru"; session.setAttribute("lang", "ru");}
        Locale locale = Locale.forLanguageTag(lang);
        request.setAttribute("jakarta.servlet.jsp.jstl.fmt.locale", locale);

        String theme = (String) session.getAttribute("theme");
        if (theme == null) { theme = "white"; session.setAttribute("theme", "white");}

        chain.doFilter(new XssWrapper(httpRequest), response);
    }

    private static class XssWrapper extends HttpServletRequestWrapper {
        public XssWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            if (value == null) {
                return null;
            }
            return value.replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\"", "&quot;")
                        .replace("'", "&#x27;");
        }
    }
}
