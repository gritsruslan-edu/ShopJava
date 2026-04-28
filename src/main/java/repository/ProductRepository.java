package repository;

import models.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Репозиторій для роботи з таблицею products у базі даних.
 */
public class ProductRepository implements IProductRepository {

    private final String url;
    private final String user;
    private final String password;

    /**
     * Створює репозиторій і отримує налаштування бази даних.
     */
    public ProductRepository(Properties properties) {
        this.url = properties.getProperty("db.url");
        this.user = properties.getProperty("db.user");
        this.password = properties.getProperty("db.password");

        validateProperties();
    }

    /**
     * Перевіряє наявність основних налаштувань бази даних.
     */
    private void validateProperties() {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("У db.properties відсутнє значення db.url.");
        }

        if (user == null || user.trim().isEmpty()) {
            throw new IllegalArgumentException("У db.properties відсутнє значення db.user.");
        }

        if (password == null) {
            throw new IllegalArgumentException("У db.properties відсутнє значення db.password.");
        }
    }

    /**
     * Створює підключення до бази даних.
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Повертає всі товари з бази даних.
     */
    @Override
    public List<Product> findAll() {
        String sql = """
                SELECT id, name, price, type, quantity
                FROM products
                ORDER BY name
                """;

        List<Product> products = new ArrayList<Product>();

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getString("name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("type"),
                        resultSet.getInt("quantity")
                );

                product.setId(UUID.fromString(resultSet.getString("id")));

                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при отриманні списку товарів.", e);
        }

        return products;
    }

    /**
     * Додає новий товар у базу даних.
     */
    @Override
    public void add(Product product) {
        String sql = """
                INSERT INTO products (id, name, price, type, quantity)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, product.getId());
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getPrice());
            statement.setString(4, product.getType());
            statement.setInt(5, product.getQuantity());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при додаванні товару.", e);
        }
    }

    /**
     * Оновлює інформацію про товар.
     */
    @Override
    public void update(Product product) {
        String sql = """
            UPDATE products
            SET name = ?, price = ?, type = ?, quantity = ?
            WHERE id = ?
            """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setString(3, product.getType());
            statement.setInt(4, product.getQuantity());
            statement.setObject(5, product.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при оновленні товару.", e);
        }
    }

    /**
     * Видаляє товар за ідентифікатором.
     */
    @Override
    public void deleteById(UUID id) {
        String sql = """
            DELETE FROM products
            WHERE id = ?
            """;

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setObject(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Помилка при видаленні товару.", e);
        }
    }

    @Override
    public Product findById(UUID id) {
        return null;
    }

    @Override
    public List<Product> searchByNameAndType(String name, String type) {
        return List.of();
    }
}