import models.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести для класу Product.
 */
class ProductTest {

    /**
     * Перевіряє, що конструктор створює товар з коректними даними.
     */
    @Test
    void shouldCreateProductWithValidData() {
        Product product = new Product("Ноутбук", 25000.0, "Електроніка", 10);

        assertNotNull(product.getId());
        assertEquals("Ноутбук", product.getName());
        assertEquals(25000.0, product.getPrice());
        assertEquals("Електроніка", product.getType());
        assertEquals(10, product.getQuantity());
    }

    /**
     * Перевіряє, що setter ідентифікатора не приймає null.
     */
    @Test
    void shouldThrowExceptionWhenIdIsNull() {
        Product product = new Product("Ноутбук", 25000.0, "Електроніка", 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> product.setId(null)
        );

        assertEquals("Ідентифікатор товару не може бути пустим.", exception.getMessage());
    }

    /**
     * Перевіряє, що setter назви не приймає пусте значення.
     */
    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        Product product = new Product("Ноутбук", 25000.0, "Електроніка", 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> product.setName("")
        );

        assertEquals("Назва товару не може бути пустою.", exception.getMessage());
    }

    /**
     * Перевіряє, що setter ціни не приймає від'ємне значення.
     */
    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        Product product = new Product("Ноутбук", 25000.0, "Електроніка", 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> product.setPrice(-1.0)
        );

        assertEquals("Ціна товару не може бути меншою за 0.", exception.getMessage());
    }

    /**
     * Перевіряє, що setter кількості не приймає від'ємне значення.
     */
    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        Product product = new Product("Ноутбук", 25000.0, "Електроніка", 10);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> product.setQuantity(-1)
        );

        assertEquals("Кількість товару не може бути меншою за 0.", exception.getMessage());
    }
}