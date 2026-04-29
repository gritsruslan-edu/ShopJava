package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Product;
import repository.IProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Контролер головної сторінки керування товарами.
 */
public class ProductController {

    private IProductRepository productRepository;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, UUID> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, String> typeColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TextField searchNameField;

    @FXML
    private TextField searchTypeField;

    /**
     * Ініціалізує таблицю після завантаження FXML.
     */
    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<Product, UUID>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("type"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
    }

    /**
     * Встановлює репозиторій товарів.
     */
    public void setProductRepository(IProductRepository productRepository) {
        this.productRepository = productRepository;
        loadAllProducts();
    }

    /**
     * Завантажує всі товари з бази даних.
     */
    private void loadAllProducts() {
        List<Product> products = productRepository.findAll();
        ObservableList<Product> observableProducts = FXCollections.observableArrayList(products);
        productsTable.setItems(observableProducts);
    }

    /**
     * Обробляє натискання кнопки показу всіх товарів.
     */
    @FXML
    private void handleLoadAllProducts() {
        searchNameField.clear();
        searchTypeField.clear();
        loadAllProducts();
    }

    /**
     * Обробляє пошук товарів.
     */
    @FXML
    private void handleSearchProducts() {
        try {
            String name = searchNameField.getText();
            String type = searchTypeField.getText();

            if ((name == null || name.trim().isEmpty())
                    && (type == null || type.trim().isEmpty())) {
                showInfo("Введіть назву або тип товару для пошуку.");
                return;
            }

            List<Product> products = productRepository.searchByNameAndType(name, type);

            if (products.isEmpty()) {
                showInfo("За вашим запитом товари не знайдено.");
                return;
            }

            ObservableList<Product> observableProducts = FXCollections.observableArrayList(products);
            productsTable.setItems(observableProducts);
        } catch (RuntimeException e) {
            showError("Помилка при пошуку товарів.");
        }
    }

    /**
     * Перевіряє, чи зараз використовується режим пошуку.
     */
    private boolean isSearchMode() {
        String name = searchNameField.getText();
        String type = searchTypeField.getText();

        return (name != null && !name.trim().isEmpty())
                || (type != null && !type.trim().isEmpty());
    }

    /**
     * Оновлює таблицю з урахуванням поточного режиму.
     */
    private void refreshCurrentTableState() {
        if (isSearchMode()) {
            String name = searchNameField.getText();
            String type = searchTypeField.getText();

            List<Product> products = productRepository.searchByNameAndType(name, type);
            ObservableList<Product> observableProducts = FXCollections.observableArrayList(products);

            productsTable.setItems(observableProducts);
        } else {
            loadAllProducts();
        }
    }

    /**
     * Відкриває вікно додавання товару.
     */
    @FXML
    private void handleOpenAddProductWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/add-product.fxml")
            );

            Scene scene = new Scene(loader.load());

            AddProductController controller = loader.getController();
            controller.setProductRepository(productRepository);

            Stage stage = new Stage();
            stage.setTitle("Додати товар");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshCurrentTableState();
        } catch (IOException e) {
            showError("Помилка відкриття вікна додавання товару.");
        }
    }

    /**
     * Відкриває вікно оновлення товару.
     */
    @FXML
    private void handleOpenUpdateProductWindow() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showError("Спочатку виберіть товар для оновлення.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/update-product.fxml")
            );

            Scene scene = new Scene(loader.load());

            UpdateProductController controller = loader.getController();
            controller.setProductRepository(productRepository);
            controller.setSelectedProduct(selectedProduct);

            Stage stage = new Stage();
            stage.setTitle("Оновити товар");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshCurrentTableState();
        } catch (IOException e) {
            showError("Помилка відкриття вікна оновлення товару.");
        }
    }

    /**
     * Обробляє видалення товару.
     */
    @FXML
    private void handleDeleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showError("Спочатку виберіть товар для видалення.");
            return;
        }

        try {
            productRepository.deleteById(selectedProduct.getId());
            refreshCurrentTableState();
            showInfo("Товар успішно видалено.");
        } catch (RuntimeException e) {
            showError("Помилка при видаленні товару.");
        }
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

    /**
     * Показує інформаційне повідомлення.
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Повідомлення");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}