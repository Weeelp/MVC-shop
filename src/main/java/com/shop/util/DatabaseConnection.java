package com.shop.util;

import com.shop.exception.ShopException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public enum DatabaseConnection {

    INSTANCE;

    private static final int POOL_SIZE = 10;
    private final LinkedBlockingQueue<Connection> freeConnections = new LinkedBlockingQueue<>(POOL_SIZE);

    private ShopException initializationException = null;

    DatabaseConnection() {
        Properties properties = new Properties();
        try (InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new ShopException("Конфигурационный файл db.properties не найден");
            }
            properties.load(in);
            Class.forName("org.postgresql.Driver");

            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
                );
                freeConnections.offer(connection);
            }
        } catch (Exception e) {
            this.initializationException = new ShopException("Критическая ошибка инициализации пула соединений", e);
        }
    }

    public Connection getConnection() throws ShopException {
        if (initializationException != null) {
            throw initializationException;
        }

        try {
            return freeConnections.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ShopException("Поток был прерван во время ожидания соединения из пула", e);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            freeConnections.offer(connection);
        }
    }
}
