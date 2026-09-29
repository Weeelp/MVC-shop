package com.shop.controller;

import com.shop.dao.UserDao;
import com.shop.exception.ShopException;
import com.shop.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(LoginServlet.class);
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String usernameParam = request.getParameter("username");
            String passwordParam = request.getParameter("password");

            User user = null;

            User dbUser = userDao.findByUsername(usernameParam);

            if (dbUser != null && dbUser.getPassword().equals(passwordParam)) {
                if ("admin".equals(usernameParam) && "ADMIN".equals(dbUser.getRole())) {
                    user = dbUser;
                } else {
                    user = dbUser;
                }
            }

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/products");
            } else {
                request.setAttribute("error", "Неверный логин или пароль");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        } catch (ShopException e) {
            logger.error("Критический сбой при попытке авторизации пользователя: ", e);
            request.setAttribute("error", "На сервере произошел сбой. Пожалуйста, попробуйте позже.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
