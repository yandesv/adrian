package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarCRUD {

    private static TableView<Car> table = new TableView<>();
    private static ObservableList<Car> carList = FXCollections.observableArrayList();

    public static void show() {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        // Table
        TableColumn<Car, Integer> colCarId = new TableColumn<>("Car ID");
        colCarId.setCellValueFactory(data -> data.getValue().carIdProperty().asObject());

        TableColumn<Car, String> colMake = new TableColumn<>("Make");
        colMake.setCellValueFactory(data -> data.getValue().makeProperty());

        TableColumn<Car, String> colModel = new TableColumn<>("Model");
        colModel.setCellValueFactory(data -> data.getValue().modelProperty());

        TableColumn<Car, Integer> colYear = new TableColumn<>("Year");
        colYear.setCellValueFactory(data -> data.getValue().yearProperty().asObject());

        TableColumn<Car, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(data -> data.getValue().priceProperty().asObject());

        TableColumn<Car, String> colColor = new TableColumn<>("Color");
        colColor.setCellValueFactory(data -> data.getValue().colorProperty());

        TableColumn<Car, Integer> colMileage = new TableColumn<>("Mileage");
        colMileage.setCellValueFactory(data -> data.getValue().mileageProperty().asObject());

        TableColumn<Car, Boolean> colIsSold = new TableColumn<>("Sold");
        colIsSold.setCellValueFactory(data -> data.getValue().isSoldProperty());

        table.getColumns().addAll(colCarId, colMake, colModel, colYear, colPrice, colColor, colMileage, colIsSold);
        table.setItems(carList);

        // Buttons
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);

        addButton.setOnAction(e -> showAddForm());
        editButton.setOnAction(e -> showEditForm());
        deleteButton.setOnAction(e -> deleteData());

        root.getChildren().addAll(table, buttonBox);

        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Car CRUD");
        stage.show();

        loadData();
    }

    private static void loadData() {
        carList.clear();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cars")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("car_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        rs.getString("color"),
                        rs.getInt("mileage"),
                        rs.getBoolean("is_sold")
                );
                carList.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showAddForm() {
        Stage formStage = new Stage();
        formStage.setTitle("Add Car");

        GridPane grid = createFormLayout();

        TextField makeField = new TextField();
        TextField modelField = new TextField();
        TextField yearField = new TextField();
        TextField priceField = new TextField();
        TextField colorField = new TextField();
        TextField mileageField = new TextField();
        CheckBox isSoldField = new CheckBox();

        grid.add(new Label("Make:"), 0, 0);
        grid.add(makeField, 1, 0);
        grid.add(new Label("Model:"), 0, 1);
        grid.add(modelField, 1, 1);
        grid.add(new Label("Year:"), 0, 2);
        grid.add(yearField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Color:"), 0, 4);
        grid.add(colorField, 1, 4);
        grid.add(new Label("Mileage:"), 0, 5);
        grid.add(mileageField, 1, 5);
        grid.add(new Label("Sold:"), 0, 6);
        grid.add(isSoldField, 1, 6);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO cars (make, model, year, price, color, mileage, is_sold) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, makeField.getText());
                stmt.setString(2, modelField.getText());
                stmt.setInt(3, Integer.parseInt(yearField.getText()));
                stmt.setDouble(4, Double.parseDouble(priceField.getText()));
                stmt.setString(5, colorField.getText());
                stmt.setInt(6, Integer.parseInt(mileageField.getText()));
                stmt.setBoolean(7, isSoldField.isSelected());
                stmt.executeUpdate();
                loadData();
                formStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox formRoot = new VBox(10, grid, saveButton);
        formRoot.setPadding(new Insets(10));

        Scene scene = new Scene(formRoot, 400, 350);
        formStage.setScene(scene);
        formStage.show();
    }

    private static void showEditForm() {
        Car selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select Car", "Please select a car to edit.");
            return;
        }

        Stage formStage = new Stage();
        formStage.setTitle("Edit Car");

        GridPane grid = createFormLayout();

        TextField makeField = new TextField(selected.getMake());
        TextField modelField = new TextField(selected.getModel());
        TextField yearField = new TextField(String.valueOf(selected.getYear()));
        TextField priceField = new TextField(String.valueOf(selected.getPrice()));
        TextField colorField = new TextField(selected.getColor());
        TextField mileageField = new TextField(String.valueOf(selected.getMileage()));
        CheckBox isSoldField = new CheckBox();
        isSoldField.setSelected(selected.isSold());

        grid.add(new Label("Make:"), 0, 0);
        grid.add(makeField, 1, 0);
        grid.add(new Label("Model:"), 0, 1);
        grid.add(modelField, 1, 1);
        grid.add(new Label("Year:"), 0, 2);
        grid.add(yearField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Color:"), 0, 4);
        grid.add(colorField, 1, 4);
        grid.add(new Label("Mileage:"), 0, 5);
        grid.add(mileageField, 1, 5);
        grid.add(new Label("Sold:"), 0, 6);
        grid.add(isSoldField, 1, 6);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE cars SET make = ?, model = ?, year = ?, price = ?, color = ?, mileage = ?, is_sold = ? WHERE car_id = ?")) {
                stmt.setString(1, makeField.getText());
                stmt.setString(2, modelField.getText());
                stmt.setInt(3, Integer.parseInt(yearField.getText()));
                stmt.setDouble(4, Double.parseDouble(priceField.getText()));
                stmt.setString(5, colorField.getText());
                stmt.setInt(6, Integer.parseInt(mileageField.getText()));
                stmt.setBoolean(7, isSoldField.isSelected());
                stmt.setInt(8, selected.getCarId());
                stmt.executeUpdate();
                loadData();
                formStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox formRoot = new VBox(10, grid, saveButton);
        formRoot.setPadding(new Insets(10));

        Scene scene = new Scene(formRoot, 400, 350);
        formStage.setScene(scene);
        formStage.show();
    }

    private static void deleteData() {
        Car selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select Car", "Please select a car to delete.");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM cars WHERE car_id = ?")) {
            stmt.setInt(1, selected.getCarId());
            stmt.executeUpdate();
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static GridPane createFormLayout() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        return grid;
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
