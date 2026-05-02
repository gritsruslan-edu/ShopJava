package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Product;
import repository.IProductRepository;

import java.util.UUID;

/**
 * Контролер вікна оновлення товару.
 */
public class UpdateProductController {

    private IProductRepository productRepository;
    private Product selectedProduct;

    @FXML
    private TextField idField;

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
     * Встановлює товар для оновлення та заповнює форму його даними.
     */
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        fillForm();
    }

    /**
     * Заповнює форму даними вибраного товару.
     */
    private void fillForm() {
        if (selectedProduct == null) {
            return;
        }

        idField.setText(String.valueOf(selectedProduct.getId()));
        nameField.setText(selectedProduct.getName());
        priceField.setText(String.valueOf(selectedProduct.getPrice()));
        typeField.setText(selectedProduct.getType());
        quantityField.setText(String.valueOf(selectedProduct.getQuantity()));
    }

    /**
     * Обробляє оновлення товару.
     */
    @FXML
    private void handleUpdateProduct() {
        try {
            UUID id = UUID.fromString(idField.getText());
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String type = typeField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            Product product = new Product(name, price, type, quantity);
            product.setId(id);

            productRepository.update(product);

            closeWindow();
        } catch (NumberFormatException e) {
            showError("Ціна та кількість повинні бути числами.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (RuntimeException e) {
            showError("Помилка при оновленні товару.");
        }
    }

    /**
     * Закриває вікно оновлення товару.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Закриває поточне вікно.
     */
    private void closeWindow() {
        Stage stage = (Stage) idField.getScene().getWindow();
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