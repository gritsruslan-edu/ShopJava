import controller.ProductController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.ProductRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Головний клас JavaFX-додатку.
 */
public class ProductApp extends Application {

    private ProductRepository productRepository;

    /**
     * Ініціалізує додаток перед запуском головного вікна.
     */
    @Override
    public void init() throws Exception {
        Parameters parameters = getParameters();

        if (parameters.getRaw().isEmpty()) {
            throw new IllegalArgumentException("Потрібно передати шлях до db.properties через args.");
        }

        String propertiesPath = parameters.getRaw().get(0);
        Properties properties = loadProperties(propertiesPath);

        productRepository = new ProductRepository(properties);
    }

    /**
     * Завантажує головне вікно додатку.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                ProductApp.class.getResource("/view/product.fxml")
        );

        Scene scene = new Scene(loader.load(), 900, 600);

        ProductController controller = loader.getController();
        controller.setProductRepository(productRepository);

        stage.setTitle("Products");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Завантажує налаштування бази даних із файлу.
     */
    private Properties loadProperties(String path) throws IOException {
        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
        }

        return properties;
    }

    /**
     * Запускає JavaFX-додаток.
     */
    public static void main(String[] args) {
        launch(args);
    }
}