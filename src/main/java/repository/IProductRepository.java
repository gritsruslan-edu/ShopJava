package repository;

import model.Product;

import java.util.List;
import java.util.UUID;

/**
 * Інтерфейс для роботи з товарами у сховищі даних.
 */
public interface IProductRepository {

    /**
     * Повертає всі товари.
     */
    List<Product> findAll();

    /**
     * Додає новий товар.
     */
    void add(Product product);

    /**
     * Оновлює інформацію про товар.
     */
    void update(Product product);

    /**
     * Видаляє товар за ідентифікатором.
     */
    void deleteById(UUID id);

    /**
     * Знаходить товар за ідентифікатором.
     */
    Product findById(UUID id);

    /**
     * Шукає товари за назвою та типом.
     */
    List<Product> searchByNameAndType(String name, String type);
}