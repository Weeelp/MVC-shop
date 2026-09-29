package com.shop.dao;

import com.shop.exception.ShopException;
import com.shop.model.Product;
import com.shop.util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class ProductDao {

    public List<Product> findAll() throws ShopException {
        List<Product> x = new ArrayList<>();
        String q = "SELECT id, name, description, price, quantity FROM products ORDER BY id";
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(q);
             ResultSet r = s.executeQuery()) {

            while (r.next()) {
                x.add(map(r));
            }
        } catch (SQLException e) {
            throw new ShopException("Ошибка БД при получении всех товаров", e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
        return x;
    }

    public List<Product> findAllPaginated(int page, int recordsPerPage) throws ShopException {
        List<Product> x = new ArrayList<>();
        String q = "SELECT id, name, description, price, quantity FROM products ORDER BY id LIMIT ? OFFSET ?";

        int offset = (page - 1) * recordsPerPage;
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(q)) {
            s.setInt(1, recordsPerPage);
            s.setInt(2, offset);
            try (ResultSet r = s.executeQuery()) {
                while (r.next()) {
                    x.add(map(r));
                }
            }
        } catch (SQLException e) {
            throw new ShopException("Ошибка БД при получении товаров для страницы: " + page, e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
        return x;
    }

    public int countAll() throws ShopException {
        String q = "SELECT COUNT(*) FROM products";
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(q);
             ResultSet r = s.executeQuery()) {
            if (r.next()) {
                return r.getInt(1);
            }
        } catch (SQLException e) {
            throw new ShopException("Ошибка БД при подсчете общего количества товаров", e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
        return 0;
    }

    public Product findById(long id) throws ShopException {
        String q = "SELECT id, name, description, price, quantity FROM products WHERE id = ?";
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(q)) {
            s.setLong(1, id);
            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) {
                    return null;
                }
                return map(r);
            }
        } catch (SQLException e) {
            throw new ShopException("Ошибка БД при поиске товара по ID: " + id, e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
    }

    public void create(String n, String d, java.math.BigDecimal p, int q) throws ShopException {
        String sql = "INSERT INTO products(name, description, price, quantity) VALUES(?, ?, ?, ?)";
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, n);
            s.setString(2, d);
            s.setBigDecimal(3, p);
            s.setInt(4, q);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new ShopException("Ошибка БД при добавлении товара: " + n, e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
    }

    public void delete(long id) throws ShopException {
        String sql = "DELETE FROM products WHERE id = ?";
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(sql)) {
            s.setLong(1, id);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new ShopException("Ошибка БД при удалении товара по ID: " + id, e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
    }

    private Product map(ResultSet r) throws SQLException {
        Product p = new Product();
        p.setId(r.getLong("id"));
        p.setName(r.getString("name"));
        p.setDescription(r.getString("description"));
        p.setPrice(r.getBigDecimal("price"));
        p.setQuantity(r.getInt("quantity"));
        return p;
    }
}
