package application2;

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

public class PasienCRUD {

	private static TableView<Pasien> table = new TableView<>();
	private static ObservableList<Pasien> pasienList = FXCollections.observableArrayList();

	public static void show() {
		Stage stage = new Stage();
		VBox root = new VBox();
		root.setPadding(new Insets(10));
		root.setSpacing(10);

		// Tabel untuk menampilkan data pasien
		table = new TableView<>();
		TableColumn<Pasien, Integer> colld = new TableColumn<>("ID");
		colld.setCellValueFactory(data -> data.getValue().idProperty().asObject());

		TableColumn<Pasien, String> colNama = new TableColumn<>("Nama");
		colNama.setCellValueFactory(data -> data.getValue().namaProperty());

		TableColumn<Pasien, String> colAlamat = new TableColumn<>("Alamat");
		colAlamat.setCellValueFactory(data -> data.getValue().alamatProperty());

		TableColumn<Pasien, String> colNoTelp = new TableColumn<>("No. Telepon");
		colNoTelp.setCellValueFactory(data -> data.getValue().noTelpProperty());

		table.getColumns().addAll(colld, colNama, colAlamat, colNoTelp);
		table.setItems(pasienList);

		// Tombol CRUD
		Button addButton = new Button("Tambah");
		Button editButton = new Button("Edit");
		Button deleteButton = new Button("Hapus");

		HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);

		// Event handlers
		addButton.setOnAction(e -> showAddForm());
		editButton.setOnAction(e -> showEditForm());
		deleteButton.setOnAction(e -> deleteData());

		root.getChildren().addAll(table, buttonBox);

		Scene scene = new Scene(root, 600, 400);
		stage.setScene(scene);
		stage.setTitle("CRUD Pasien");
		stage.show();

		loadData();

	}

	private static void loadData() {
		pasienList.clear();
		try (Connection conn = DBConnection.getConnection(); 
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pasien")){
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	        	Pasien pasien = new Pasien(
	                    rs.getInt("id"),
	                    rs.getString("nama"),
	                    rs.getString("alamat"),
	                    rs.getString("no_telp")
	                );
	                pasienList.add(pasien);	
	        }
	        
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void showAddForm() {
		Stage formStage = new Stage();
	    formStage.setTitle("Tambah Pasien");
	    
	    GridPane grid = createFormLayout();

	    TextField namaField = new TextField();
	    TextField alamatField = new TextField();
	    TextField noTelpField = new TextField();

	    grid.add(new Label("Nama:"), 0, 0);
	    grid.add(namaField, 1, 0);
	    grid.add(new Label("Alamat:"), 0, 1);
	    grid.add(alamatField, 1, 1);
	    grid.add(new Label("No. Telepon:"), 0, 2);
	    grid.add(noTelpField, 1, 2);
	    
	    Button saveButton = new Button("Simpan");
	    saveButton.setOnAction(e -> {
	    	String nama = namaField.getText();
	        String alamat = alamatField.getText();
	        String noTelp = noTelpField.getText();
	        
	        try (Connection conn = DBConnection.getConnection(); 
	        	PreparedStatement stmt = conn.prepareStatement(
	                    "INSERT INTO pasien (nama, alamat, no_telp) VALUES (?, ?, ?)"
	                )){
	        	stmt.setString(1, nama);
	            stmt.setString(2, alamat);
	            stmt.setString(3, noTelp);
	            stmt.executeUpdate();
	            loadData();
	            formStage.close();
	        }catch (SQLException ex) {
	        	ex.printStackTrace();
	        }
	    });
	    
	    VBox formRoot = new VBox(10, grid, saveButton);
	    formRoot.setPadding(new Insets(10));

	    Scene scene = new Scene(formRoot, 300, 200);
	    formStage.setScene(scene);
	    formStage.show();
	    
	}	
	
	private static void showEditForm() {
		// Implementation for the edit form goes here.
		Pasien selected = table.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Pilih Pasien", "Silakan pilih pasien yang akan diedit.");
		    return;
			
		}
		
		
		Stage formStage = new Stage();
		formStage.setTitle("Edit Pasien");

		GridPane grid = createFormLayout();

		TextField namaField = new TextField(selected.getNama());
		TextField alamatField = new TextField(selected.getAlamat());
		TextField noTelpField = new TextField(selected.getNoTelp());

		grid.add(new Label("Nama:"), 0, 0);
		grid.add(namaField, 1, 0);
		grid.add(new Label("Alamat:"), 0, 1);
		grid.add(alamatField, 1, 1);
		grid.add(new Label("No. Telepon:"), 0, 2);
		grid.add(noTelpField, 1, 2);
		
		Button saveButton = new Button("Simpan");
		saveButton.setOnAction(e -> {
			String nama = namaField.getText();
		    String alamat = alamatField.getText();
		    String noTelp = noTelpField.getText();

		    try (Connection conn = DBConnection.getConnection();
		    	PreparedStatement stmt = conn.prepareStatement(
		                "UPDATE pasien SET nama = ?, alamat = ?, no_telp = ? WHERE id = ?"
		            )){
		            stmt.setString(1, nama);
		            stmt.setString(2, alamat);
		            stmt.setString(3, noTelp);
		            stmt.setInt(4, selected.getId());
		            stmt.executeUpdate();
		            loadData();
		            formStage.close();
		    }catch (SQLException ex) {
		    	ex.printStackTrace();
		    }
		});
		
		VBox formRoot = new VBox(10, grid, saveButton);
		formRoot.setPadding(new Insets(10));

		Scene scene = new Scene(formRoot, 300, 200);

		formStage.setScene(scene);
		formStage.show();
	
		
	}
	
	private static void deleteData() {
		Pasien selected = table.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Pilih Pasien", "Silakan pilih pasien yang akan dihapus.");
	        return;
		}
		
		try (Connection conn = DBConnection.getConnection(); 
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM pasien WHERE id = ?")){
	        stmt.setInt(1, selected.getId());
	        stmt.executeUpdate();
	        loadData();
		}catch (SQLException e) {
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