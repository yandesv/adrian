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

public class CustomerCRUD {

    private static TableView<Customer> table = new TableView<>();
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public static void show() {
        Stage stage = new Stage();
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        // Table to display customer data
        table = new TableView<>();
        TableColumn<Customer, Integer> colCustomerId = new TableColumn<>("ID");
        colCustomerId.setCellValueFactory(data -> data.getValue().customerIdProperty().asObject());

        TableColumn<Customer, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<Customer, String> colAddress = new TableColumn<>("Address");
        colAddress.setCellValueFactory(data -> data.getValue().addressProperty());

        TableColumn<Customer, String> colPhoneNumber = new TableColumn<>("Phone Number");
        colPhoneNumber.setCellValueFactory(data -> data.getValue().phoneNumberProperty());

        TableColumn<Customer, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());

        table.getColumns().addAll(colCustomerId, colName, colAddress, colPhoneNumber, colEmail);
        table.setItems(customerList);

        // Buttons for CRUD operations
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);

        // Event handlers
        addButton.setOnAction(e -> showAddForm());
        editButton.setOnAction(e -> showEditForm());
        deleteButton.setOnAction(e -> deleteData());

        root.getChildren().addAll(table, buttonBox);

        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.setTitle("Customer CRUD");
        stage.show();

        loadData();
    }

    private static void loadData() {
        customerList.clear();
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getString("email")
                );
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showAddForm() {
        Stage formStage = new Stage();
        formStage.setTitle("Add Customer");

        GridPane grid = createFormLayout();

        TextField nameField = new TextField();
        TextField addressField = new TextField();
        TextField phoneNumberField = new TextField();
        TextField emailField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Address:"), 0, 1);
        grid.add(addressField, 1, 1);
        grid.add(new Label("Phone Number:"), 0, 2);
        grid.add(phoneNumberField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();

            try (Connection conn = DBConnection.getConnection(); 
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO customers (name, address, phone_number, email) VALUES (?, ?, ?, ?)")) {
                stmt.setString(1, name);
                stmt.setString(2, address);
                stmt.setString(3, phoneNumber);
                stmt.setString(4, email);
                stmt.executeUpdate();
                loadData();
                formStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox formRoot = new VBox(10, grid, saveButton);
        formRoot.setPadding(new Insets(10));

        Scene scene = new Scene(formRoot, 300, 250);
        formStage.setScene(scene);
        formStage.show();
    }

    private static void showEditForm() {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select Customer", "Please select a customer to edit.");
            return;
        }

        Stage formStage = new Stage();
        formStage.setTitle("Edit Customer");

        GridPane grid = createFormLayout();

        TextField nameField = new TextField(selected.getName());
        TextField addressField = new TextField(selected.getAddress());
        TextField phoneNumberField = new TextField(selected.getPhoneNumber());
        TextField emailField = new TextField(selected.getEmail());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Address:"), 0, 1);
        grid.add(addressField, 1, 1);
        grid.add(new Label("Phone Number:"), 0, 2);
        grid.add(phoneNumberField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String address = addressField.getText();
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();

            try (Connection conn = DBConnection.getConnection(); 
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE customers SET name = ?, address = ?, phone_number = ?, email = ? WHERE customer_id = ?")) {
                stmt.setString(1, name);
                stmt.setString(2, address);
                stmt.setString(3, phoneNumber);
                stmt.setString(4, email);
                stmt.setInt(5, selected.getCustomerId());
                stmt.executeUpdate();
                loadData();
                formStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox formRoot = new VBox(10, grid, saveButton);
        formRoot.setPadding(new Insets(10));

        Scene scene = new Scene(formRoot, 300, 250);
        formStage.setScene(scene);
        formStage.show();
    }

    private static void deleteData() {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select Customer", "Please select a customer to delete.");
            return;
        }

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM customers WHERE customer_id = ?")) {
            stmt.setInt(1, selected.getCustomerId());
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
