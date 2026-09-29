package com.shop.controller;

import com.shop.exception.ShopException;
import com.shop.model.User;
import com.shop.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private static final Logger Log = LogManager.getLogger(OrderServlet.class);
    private final OrderService service = new OrderService();

    private User getUserFromSession(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = getUserFromSession(request);

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            request.setAttribute("orders", service.findByUser(user.getId()));
            request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);
        } catch (ShopException e) {
            Log.error("Критический сбой при получении списка заказов: ", e);
            request.setAttribute("error", "Не удалось загрузить ваши заказы. Сервер временно недоступен.");
            request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = getUserFromSession(request);

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String action = request.getParameter("action");

            if ("create".equals(action)) {
                long productId = Long.parseLong(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                service.create(user.getId(), productId, quantity);
            }

            if ("cancel".equals(action)) {
                long orderId = Long.parseLong(request.getParameter("id"));
                service.cancel(orderId, user.getId());
            }

            response.sendRedirect(request.getContextPath() + "/orders");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Некорректный формат ввода данных.");
            doGet(request, response);

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);

        } catch (ShopException e) {
            Log.error("Критический сбой при обработке заказа: ", e);
            request.setAttribute("error", "На сервере произошла ошибка. Пожалуйста, попробуйте позже.");
            doGet(request, response);
        }
    }
}
