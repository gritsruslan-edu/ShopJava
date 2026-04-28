package models;

import java.util.UUID;

/**
 * Клас товару в системі.
 */
public class Product {
    private UUID id;
    private String name;
    private double price;
    private String type;
    private int quantity;

    /**
     * Створює новий товар.
     */
    public Product(String name, Double price, String type, Integer quantity) {
        this.id = UUID.randomUUID();
        setName(name);
        setPrice(price);
        setType(type);
        setQuantity(quantity);
    }

    /**
     * Повертає ідентифікатор товару.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор товару.
     */
    public void setId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Ідентифікатор товару не може бути пустим.");
        }

        this.id = id;
    }

    /**
     * Повертає назву товару.
     */
    public String getName() {
        return name;
    }

    /**
     * Встановлює назву товару.
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва товару не може бути пустою.");
        }

        if (name.length() > 150) {
            throw new IllegalArgumentException("Назва товару не може бути довшою за 150 символів.");
        }

        this.name = name;
    }

    /**
     * Повертає ціну товару.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Встановлює ціну товару.
     */
    public void setPrice(Double price) {
        if (price == null) {
            throw new IllegalArgumentException("Ціна товару не може бути пустою.");
        }

        if (price < 0) {
            throw new IllegalArgumentException("Ціна товару не може бути меншою за 0.");
        }

        this.price = price;
    }

    /**
     * Повертає тип товару.
     */
    public String getType() {
        return type;
    }

    /**
     * Встановлює тип товару.
     */
    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Тип товару не може бути пустим.");
        }

        if (type.length() > 100) {
            throw new IllegalArgumentException("Тип товару не може бути довшим за 100 символів.");
        }

        this.type = type;
    }

    /**
     * Повертає кількість товару.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Встановлює кількість товару.
     */
    public void setQuantity(Integer quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Кількість товару не може бути пустою.");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Кількість товару не може бути меншою за 0.");
        }

        this.quantity = quantity;
    }

    /**
     * Повертає текстове представлення товару.
     */
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
