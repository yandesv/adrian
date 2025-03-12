package application2;
import javafx.collections.FXCollections;
import javafx.collections. ObservableList;
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

public class ObatCRUD {
	private static TableView<Obat> table = new TableView<>();
	private static ObservableList<Obat> ObatList = FXCollections.observableArrayList();
	public static void show() {
		Stage stage = new Stage();
		VBox root = new VBox();
		root.setPadding(new Insets(10));
		root.setSpacing(10);

		// Tabel untuk menampilkan data Obat
		table = new TableView<>();
		TableColumn<Obat, Integer> colld = new TableColumn<>("ID");
		colld.setCellValueFactory(data -> data.getValue().idProperty().asObject());

		TableColumn<Obat, String> colNama = new TableColumn<>("Nama"); 
		colNama.setCellValueFactory(data -> data.getValue().namaProperty());

		TableColumn<Obat, Integer> colStok = new TableColumn<>("Stok");
		colStok.setCellValueFactory(data -> data.getValue().stokProperty().asObject());

		TableColumn<Obat, Double> colHarga = new TableColumn<>("Harga"); 
		colHarga.setCellValueFactory(data -> data.getValue().hargaProperty().asObject());

		table.getColumns().addAll(colld, colNama, colStok, colHarga); 
		table.setItems(ObatList);

		// Tombol CRUD
		Button addButton = new Button("Tambah");
		Button editButton = new Button("Edit");
		Button deleteButton = new Button("Hapus");

		HBox buttonBox = new HBox(10, addButton, editButton, deleteButton);

		// Event handlers 
		addButton.setOnAction(e-> showAddForm()); 
		editButton.setOnAction(e -> showEditForm()); 
		deleteButton.setOnAction(e -> deleteData());

		root.getChildren().addAll(table, buttonBox);

		Scene scene = new Scene(root, 600, 400);
		stage.setScene(scene);
		stage.setTitle("CRUD Obat");
		stage.show();
		loadData();
	}

	private static void loadData() {
		ObatList.clear();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Obat")) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Obat Obat = new Obat(
						rs.getInt("id"),
						rs.getString("nama"),
						rs.getInt("stok"),
						rs.getDouble("harga")
						);
				ObatList.add(Obat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void showAddForm() { 
		Stage formStage = new Stage(); 
		formStage.setTitle("Tambah Obat");

		GridPane grid = createFormLayout();

		TextField namaField = new TextField(); 
		TextField stokField = new TextField(); 
		TextField hargaField = new TextField();

		grid.add(new Label("Nama:"), 0, 0);
		grid.add(namaField, 1, 0);
		grid.add(new Label("Stok:"), 0, 1);
		grid.add(stokField, 1, 1);
		grid.add(new Label("Harga:"), 0, 2);
		grid.add(hargaField, 1, 2);

		Button saveButton = new Button("Simpan");
		saveButton.setOnAction(e -> {
			String nama = namaField.getText();
			String Stok = stokField.getText();
			String Harga = hargaField.getText();
			try (Connection conn = DBConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(
							"INSERT INTO Obat (nama, Stok, harga) VALUES (?, ?, ?)"
							)){
				stmt.setString(1, nama);
				stmt.setString(2, Stok);
				stmt.setString(3, Harga);
				stmt.executeUpdate(); 
				loadData(); 
				formStage.close(); 
			} 
			catch (SQLException ex) { 
				ex.printStackTrace();
			}
		});

		VBox formRoot = new VBox(10, grid, saveButton); formRoot.setPadding(new Insets (10));
		Scene scene = new Scene (formRoot, 300, 200); formStage.setScene(scene);
		formStage.show();
	}

	private static void showEditForm() {
		Obat selected = table.getSelectionModel().getSelectedItem(); 
		if (selected == null) {
			showAlert("Pilih Obat", "Silakan pilih Obat yang akan diedit."); 
			return;
		}
		Stage formStage = new Stage(); 
		formStage.setTitle("Edit Obat");

		GridPane grid = createFormLayout();

		TextField namaField = new TextField(selected.getNama()); 
		TextField stokField = new TextField(); 
		TextField hargaField = new TextField();

		grid.add(new Label("Nama:"), 0, 0);
		grid.add(namaField, 1, 0);
		grid.add(new Label("Stok:"), 0, 1);
		grid.add(stokField, 1, 1);
		grid.add(new Label("Harga:"), 0, 2);
		grid.add(hargaField, 1, 2);

		Button saveButton = new Button("Simpan");
		saveButton.setOnAction(e -> {
			String nama = namaField.getText(); 
			String stok = stokField.getText();
			String harga = hargaField.getText();

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(
							"UPDATE Obat SET nama = ?, stok = ?, harga = ? WHERE id = ?"
							)) {
				stmt.setString(1, nama);
				stmt.setString(2, stok);
				stmt.setString(3, harga);
				stmt.setInt(4, selected.getId());
				stmt.executeUpdate();
				loadData();
				formStage.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});

		VBox formRoot = new VBox(10, grid, saveButton); formRoot.setPadding(new Insets(10));
		Scene scene = new Scene(formRoot, 300, 200);
		formStage.setScene(scene);
		formStage.show();
	}

	private static void deleteData() {
		Obat selected = table.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Pilih Obat", "Silakan pilih Obat yang akan dihapus.");
			return;
		}

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM Obat WHERE id = ?"))
		{
			stmt.setInt(1, selected.getId());
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
		Alert alert = new Alert(Alert.AlertType. INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}
}