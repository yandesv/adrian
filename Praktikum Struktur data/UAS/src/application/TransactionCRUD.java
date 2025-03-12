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

public class TransactionCRUD {
    private static TableView<Transaction> table = new TableView<>();
    private static ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    public static void show() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Table setup
        TableColumn<Transaction, Integer> colTransactionId = new TableColumn<>("Transaction ID");
        colTransactionId.setCellValueFactory(data -> data.getValue().transactionIdProperty().asObject());

        TableColumn<Transaction, Integer> colCustomerId = new TableColumn<>("Customer ID");
        colCustomerId.setCellValueFactory(data -> data.getValue().customerIdProperty().asObject());

        TableColumn<Transaction, Integer> colCarId = new TableColumn<>("Car ID");
        colCarId.setCellValueFactory(data -> data.getValue().carIdProperty().asObject());

        TableColumn<Transaction, java.sql.Date> colSaleDate = new TableColumn<>("Sale Date");
        colSaleDate.setCellValueFactory(data -> data.getValue().saleDateProperty());

        TableColumn<Transaction, Double> colSalePrice = new TableColumn<>("Sale Price");
        colSalePrice.setCellValueFactory(data -> data.getValue().salePriceProperty().asObject());

        table.getColumns().addAll(colTransactionId, colCustomerId, colCarId, colSaleDate, colSalePrice);
        table.setItems(transactionList);

        // Buttons
        Button addButton = new Button("Add");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);

        addButton.setOnAction(e -> showAddForm());
        editButton.setOnAction(e -> showEditForm());
        deleteButton.setOnAction(e -> deleteTransaction());

        root.getChildren().addAll(table, buttonBox);

        // Load data
        loadTransactions();

        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Transaction Management");
        stage.show();
    }

    private static void loadTransactions() {
        transactionList.clear();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("car_id"),
                        rs.getDate("sale_date"),
                        rs.getDouble("sale_price")
                );
                transactionList.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showAddForm() {
        Stage formStage = new Stage();
        formStage.setTitle("Add Transaction");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Inputs
        TextField customerIdField = new TextField();
        TextField carIdField = new TextField();
        TextField saleDateField = new TextField();
        TextField salePriceField = new TextField();

        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(customerIdField, 1, 0);
        grid.add(new Label("Car ID:"), 0, 1);
        grid.add(carIdField, 1, 1);
        grid.add(new Label("Sale Date (YYYY-MM-DD):"), 0, 2);
        grid.add(saleDateField, 1, 2);
        grid.add(new Label("Sale Price:"), 0, 3);
        grid.add(salePriceField, 1, 3);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO transactions (customer_id, car_id, sale_date, sale_price) VALUES (?, ?, ?, ?)")) {
                stmt.setInt(1, Integer.parseInt(customerIdField.getText()));
                stmt.setInt(2, Integer.parseInt(carIdField.getText()));
                stmt.setDate(3, java.sql.Date.valueOf(saleDateField.getText()));
                stmt.setDouble(4, Double.parseDouble(salePriceField.getText()));
                stmt.executeUpdate();
                loadTransactions();
                formStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox formRoot = new VBox(10, grid, saveButton);
        formRoot.setPadding(new Insets(10));
        formStage.setScene(new Scene(formRoot, 400, 300));
        formStage.show();
    }

    private static void showEditForm() {
        Transaction selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a transaction to edit.");
            return;
        }

        Stage formStage = new Stage();
        formStage.setTitle("Edit Transaction");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField customerIdField = new TextField(String.valueOf(selected.getCustomerId()));
        TextField carIdField = new TextField(String.valueOf(selected.getCarId()));
        TextField saleDateField = new TextField(selected.getSaleDate().toString());
        TextField salePriceField = new TextField(String.valueOf(selected.getSalePrice()));

        grid.add(new Label("Customer ID:"), 0, 0);
        grid.add(customerIdField, 1, 0);
        grid.add(new Label("Car ID:"), 0, 1);
        grid.add(carIdField, 1, 1);
        grid.add(new Label("Sale Date (YYYY-MM-DD):"), 0, 2);
        grid.add(saleDateField, 1, 2);
        grid.add(new Label("Sale Price:"), 0, 3);
        grid.add(salePriceField, 1, 3);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE transaction SET customer_id = ?, car_id = ?, sale_date = ?, sale_price = ? WHERE transaction_id = ?")) {
                stmt.setInt(1, Integer.parseInt(customerIdField.getText()));
                stmt.setInt(2, Integer.parseInt(carIdField.getText()));
                stmt.setDate(3, java.sql.Date.valueOf(saleDateField.getText()));
                stmt.setDouble(4, Double.parseDouble(salePriceField.getText()));
                stmt.setInt(5, selected.getTransactionId());
                stmt.executeUpdate();
                loadTransactions();
                formStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        VBox formRoot = new VBox(10, grid, saveButton);
        formRoot.setPadding(new Insets(10));
        formStage.setScene(new Scene(formRoot, 400, 300));
        formStage.show();
    }

    private static void deleteTransaction() {
        Transaction selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a transaction to delete.");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM transaction WHERE transaction_id = ?")) {
            stmt.setInt(1, selected.getTransactionId());
            stmt.executeUpdate();
            loadTransactions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
