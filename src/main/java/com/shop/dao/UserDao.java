package com.shop.dao;

import com.shop.exception.ShopException;
import com.shop.model.User;
import com.shop.util.DatabaseConnection;
import java.sql.*;

public class UserDao {

    public User findByUsername(String username) throws ShopException {
        String sql = "SELECT id, username, password, email, role FROM users WHERE username = ?";
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, username);
            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) {
                    return null;
                }
                return map(r);
            }
        } catch (SQLException e) {
            throw new ShopException("Ошибка при поиске пользователя по username: " + username, e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
    }

    public User findByEmail(String email) throws ShopException {
        String sql = "SELECT id, username, password, email, role FROM users WHERE email = ?";
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, email);
            try (ResultSet r = s.executeQuery()) {
                if (!r.next()) {
                    return null;
                }
                return map(r);
            }
        } catch (SQLException e) {
            throw new ShopException("Ошибка при поиске пользователя по email: " + email, e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
    }

    public void create(User u) throws ShopException {
        String sql = "INSERT INTO users(username, password, email, role) VALUES(?, ?, ?, 'USER')";
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, u.getUsername());
            s.setString(2, u.getPassword());
            s.setString(3, u.getEmail());
            s.executeUpdate();
        } catch (SQLException e) {
            throw new ShopException("Ошибка при создании пользователя: " + u.getUsername(), e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
    }

    public void updateEmail(long id, String email) throws ShopException {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        Connection c = DatabaseConnection.INSTANCE.getConnection();

        try (PreparedStatement s = c.prepareStatement(sql)) {
            s.setString(1, email);
            s.setLong(2, id);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new ShopException("Ошибка при обновлении email для пользователя с ID: " + id, e);
        } finally {
            DatabaseConnection.INSTANCE.releaseConnection(c);
        }
    }

    private User map(ResultSet r) throws SQLException {
        User u = new User();
        u.setId(r.getLong("id"));
        u.setUsername(r.getString("username"));
        u.setPassword(r.getString("password"));
        u.setEmail(r.getString("email"));
        u.setRole(r.getString("role"));
        return u;
    }
}
