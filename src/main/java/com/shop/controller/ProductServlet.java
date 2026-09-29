package com.shop.controller;

import com.shop.dao.ProductDao;
import com.shop.exception.ShopException;
import com.shop.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private static final Logger Log = LogManager.getLogger(ProductServlet.class);
    private static final int RECORDS_PER_PAGE = 5;
    private final ProductDao dao = new ProductDao();

    private User getUserFromSession(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            int rows = dao.countAll();
            int noOfPages = (int) Math.ceil((double) rows / RECORDS_PER_PAGE);

            request.setAttribute("products", dao.findAllPaginated(page, RECORDS_PER_PAGE));
            request.setAttribute("noOfPages", noOfPages);
            request.setAttribute("currentPage", page);

            request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Неверный формат номера страницы.");
            request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);

        } catch (ShopException e) {
            Log.error("Критический сбой при чтении списка товаров: ", e);
            request.setAttribute("error", "Не удалось загрузить каталог товаров. Сервер временно недоступен.");
            request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getUserFromSession(request);

        if (user == null || !"ADMIN".equals(user.getRole())) {
            response.sendError(403);
            return;
        }

        try {
            String action = request.getParameter("action");

            if ("add".equals(action)) {
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                BigDecimal price = new BigDecimal(request.getParameter("price"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                dao.create(name, description, price, quantity);
            } else if ("delete".equals(action)) {
                long id = Long.parseLong(request.getParameter("id"));
                dao.delete(id);
            }

            response.sendRedirect(request.getContextPath() + "/products");

        } catch (NumberFormatException | ArithmeticException e) {
            request.setAttribute("error", "Ошибка ввода: убедитесь, что цена и количество введены корректно.");
            doGet(request, response);

        } catch (ShopException e) {
            Log.error("Критический сбой базы данных при изменении товаров: ", e);
            request.setAttribute("error", "Произошла системная ошибка при сохранении данных.");
            doGet(request, response);
        }
    }
}
