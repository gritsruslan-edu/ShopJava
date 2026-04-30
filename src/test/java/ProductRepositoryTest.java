import models.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import repository.ProductRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для ProductRepository з використанням PostgreSQL Testcontainers.
 */
@Testcontainers
class ProductRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:18")
                    .withDatabaseName("shop_test")
                    .withUsername("test")
                    .withPassword("test");

    private ProductRepository productRepository;

    /**
     * Створює таблицю products один раз для всіх тестів.
     */
    @BeforeAll
    static void createTable() throws Exception {
        try (
                Connection connection = DriverManager.getConnection(
                        postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword()
                );
                Statement statement = connection.createStatement()
        ) {
            statement.execute("""
                    CREATE TABLE products (
                        id UUID PRIMARY KEY,
                        name VARCHAR(150) NOT NULL,
                        price DOUBLE PRECISION NOT NULL,
                        type VARCHAR(100) NOT NULL,
                        quantity INTEGER NOT NULL
                    )
                    """);
        }
    }

    /**
     * Очищає таблицю перед кожним тестом.
     */
    @BeforeEach
    void setUp() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("db.url", postgres.getJdbcUrl());
        properties.setProperty("db.user", postgres.getUsername());
        properties.setProperty("db.password", postgres.getPassword());

        productRepository = new ProductRepository(properties);

        try (
                Connection connection = DriverManager.getConnection(
                        postgres.getJdbcUrl(),
                        postgres.getUsername(),
                        postgres.getPassword()
                );
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("TRUNCATE TABLE products");
        }
    }

    /**
     * Перевіряє додавання товару та пошук за ідентифікатором.
     */
    @Test
    void shouldAddProductAndFindItById() {
        Product product = new Product("Ноутбук", 25000.0, "Електроніка", 5);

        productRepository.add(product);

        Product foundProduct = productRepository.findById(product.getId());

        assertNotNull(foundProduct);
        assertEquals(product.getId(), foundProduct.getId());
        assertEquals("Ноутбук", foundProduct.getName());
        assertEquals(25000.0, foundProduct.getPrice());
        assertEquals("Електроніка", foundProduct.getType());
        assertEquals(5, foundProduct.getQuantity());
    }

    /**
     * Перевіряє отримання всіх товарів.
     */
    @Test
    void shouldFindAllProducts() {
        Product firstProduct = new Product("Ноутбук", 25000.0, "Електроніка", 5);
        Product secondProduct = new Product("Молоко", 45.0, "Продукти", 20);

        productRepository.add(firstProduct);
        productRepository.add(secondProduct);

        List<Product> products = productRepository.findAll();

        assertEquals(2, products.size());
        assertEquals("Молоко", products.get(0).getName());
        assertEquals("Ноутбук", products.get(1).getName());
    }

    /**
     * Перевіряє оновлення товару.
     */
    @Test
    void shouldUpdateProduct() {
        Product product = new Product("Ноутбук", 25000.0, "Електроніка", 5);
        productRepository.add(product);

        Product updatedProduct = new Product("Ігровий ноутбук", 32000.0, "Комп'ютери", 3);
        updatedProduct.setId(product.getId());

        productRepository.update(updatedProduct);

        Product foundProduct = productRepository.findById(product.getId());

        assertNotNull(foundProduct);
        assertEquals(product.getId(), foundProduct.getId());
        assertEquals("Ігровий ноутбук", foundProduct.getName());
        assertEquals(32000.0, foundProduct.getPrice());
        assertEquals("Комп'ютери", foundProduct.getType());
        assertEquals(3, foundProduct.getQuantity());
    }

    /**
     * Перевіряє видалення товару за ідентифікатором.
     */
    @Test
    void shouldDeleteProductById() {
        Product product = new Product("Ноутбук", 25000.0, "Електроніка", 5);
        productRepository.add(product);

        productRepository.deleteById(product.getId());

        Product foundProduct = productRepository.findById(product.getId());

        assertNull(foundProduct);
    }

    /**
     * Перевіряє пошук товарів за назвою та типом.
     */
    @Test
    void shouldSearchProductsByNameAndType() {
        Product firstProduct = new Product("Ноутбук Lenovo", 25000.0, "Електроніка", 5);
        Product secondProduct = new Product("Ноутбук Asus", 28000.0, "Електроніка", 4);
        Product thirdProduct = new Product("Молоко", 45.0, "Продукти", 20);

        productRepository.add(firstProduct);
        productRepository.add(secondProduct);
        productRepository.add(thirdProduct);

        List<Product> products = productRepository.searchByNameAndType("ноутбук", "електроніка");

        assertEquals(2, products.size());
        assertEquals("Ноутбук Asus", products.get(0).getName());
        assertEquals("Ноутбук Lenovo", products.get(1).getName());
    }
}