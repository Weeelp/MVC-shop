package com.shop.service;

import com.shop.dao.OrderDao;
import com.shop.dao.ProductDao;
import com.shop.exception.ShopException;
import com.shop.model.Order;
import com.shop.model.Product;
import java.math.BigDecimal;
import java.util.List;

public class OrderService {

    private final OrderDao orders = new OrderDao();
    private final ProductDao products = new ProductDao();

    public void create(long user, long product, int qty) throws ShopException {
        Product p = products.findById(product);
        if (p == null) {
            throw new IllegalArgumentException("Товар не найден");
        }

        if (qty <= 0) {
            throw new IllegalArgumentException("Количество товара должно быть больше нуля");
        }

        if (qty > p.getQuantity()) {
            throw new IllegalArgumentException("Недостаточное количество товара на складе");
        }

        BigDecimal total = p.getPrice().multiply(BigDecimal.valueOf(qty));
        orders.create(user, product, qty, total);
    }

    public List<Order> findByUser(long id) throws ShopException {
        return orders.findByUser(id);
    }

    public void cancel(long order, long user) throws ShopException {
        orders.cancel(order, user);
    }
}
