package repository;

import models.Product;

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

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public void add(Product product) {

    }

    @Override
    public void update(Product product) {

    }

    @Override
    public void deleteById(UUID id) {

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