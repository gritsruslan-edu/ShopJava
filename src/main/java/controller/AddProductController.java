package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Product;
import repository.IProductRepository;

/**
 * Контролер вікна додавання товару.
 */
public class AddProductController {

    private IProductRepository productRepository;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField quantityField;

    /**
     * Встановлює репозиторій товарів.
     */
    public void setProductRepository(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Обробляє додавання товару.
     */
    @FXML
    private void handleAddProduct() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String type = typeField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            Product product = new Product(name, price, type, quantity);

            productRepository.add(product);

            closeWindow();
        } catch (NumberFormatException e) {
            showError("Ціна та кількість повинні бути числами.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (RuntimeException e) {
            showError("Помилка при додаванні товару.");
        }
    }

    /**
     * Закриває вікно додавання товару.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Закриває поточне вікно.
     */
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    /**
     * Показує повідомлення про помилку.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}