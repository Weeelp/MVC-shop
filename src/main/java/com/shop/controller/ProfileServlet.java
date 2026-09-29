package com.shop.controller;

import com.shop.dao.UserDao;
import com.shop.exception.ShopException;
import com.shop.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final Logger Log = LogManager.getLogger(ProfileServlet.class);
    private final UserDao dao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("user");
            String email = request.getParameter("email");

            dao.updateEmail(user.getId(), email);
            user.setEmail(email);

            response.sendRedirect(request.getContextPath() + "/profile");

        } catch (ShopException e) {
            Log.error("Критический сбой при обновлении email пользователя: ", e);
            request.setAttribute("error", "Не удалось сохранить изменения. Попробуйте позже.");
            doGet(request, response);
        }
    }
}
