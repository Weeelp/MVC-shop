package com.shop.controller;

import com.shop.dao.UserDao;
import com.shop.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
  private static final Logger Log = LogManager.getLogger();

    private final UserDao dao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");

            if (dao.findByUsername(username) != null) {
                request.setAttribute("error", "Пользователь уже существует");
                doGet(request, response);
                return;
            }
            if (dao.findByEmail(email) != null) {
              request.setAttribute("error", "Пользователь с таким Email уже существует");
              doGet(request, response);
              return;
            }

            dao.create(new User(username, password, email));
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception ex) {
            Log.error("Критический сбой при регистрации пользователя: ", ex);
            request.setAttribute("error", "На сервере произошел сбой. Пожалуйста, попробуйте позже.");
            doGet(request, response);
        }
    }
}
