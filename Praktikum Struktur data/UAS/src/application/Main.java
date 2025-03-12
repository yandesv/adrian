package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Menu Bar
        MenuBar menuBar = new MenuBar();

        Menu menuData = new Menu("Data");
        MenuItem CustomerMenu = new MenuItem("Customer");
        MenuItem TransactionMenu = new MenuItem("Transaction");
        MenuItem CarMenu = new MenuItem("Car");

        menuData.getItems().addAll(CustomerMenu, TransactionMenu, CarMenu);

        menuBar.getMenus().add(menuData);

        // Root Layout
        VBox root = new VBox(menuBar);

        // Menambahkan gambar sebagai latar belakang
        Image image = new Image(getClass().getResourceAsStream("img/rsgm.jpg"));
        BackgroundImage backgroundImage = new BackgroundImage(image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
        root.setBackground(new Background(backgroundImage));

        // Scene
        Scene scene = new Scene(root, 800, 600);

        // Event Handlers
        CustomerMenu.setOnAction(e -> CustomerCRUD.show());
        CarMenu.setOnAction(e -> CarCRUD.show());
        TransactionMenu.setOnAction(e -> TransactionCRUD.show());

        primaryStage.setTitle("Jual Beli Mobil");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
