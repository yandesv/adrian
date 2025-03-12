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
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResepCRUD {
	private static TableView<Resep> table = new TableView<>();
	private static ObservableList<Resep> resepList = FXCollections.observableArrayList();
	private static ObservableList<Obat> obatList = FXCollections.observableArrayList();
	private static ObservableList<Pemeriksaan> pemeriksaanList = FXCollections.observableArrayList();

	public static void show() {
		Stage stage = new Stage();
		VBox root = new VBox();
		root.setPadding(new Insets(10));
		root.setSpacing(10);

		// Tabel untuk menampilkan data resep
		table = new TableView<>();
		TableColumn<Resep, Integer> colId = new TableColumn<>("ID");
		colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());

		TableColumn<Resep, Integer> colPemeriksaanID = new TableColumn<>("Pemeriksaan ID");
		colPemeriksaanID.setCellValueFactory(data -> data.getValue().pemeriksaanIdProperty().asObject());

		TableColumn<Resep, String> colObatNama = new TableColumn<>("Nama Obat");
		colObatNama.setCellValueFactory(data -> data.getValue().obatNamaProperty());

		TableColumn<Resep, Integer> colJumlah = new TableColumn<>("Jumlah");
		colJumlah.setCellValueFactory(data -> data.getValue().jumlahProperty().asObject());

		table.getColumns().addAll(colId, colPemeriksaanID, colObatNama, colJumlah);
		table.setItems(resepList);

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

		Scene scene = new Scene(root, 800, 400);
		stage.setScene(scene);
		stage.setTitle("CRUD Resep");
		stage.show();

		loadObatData();
		loadPemeriksaanData();
		loadData();
	}

	private static void loadData() {
		resepList.clear();
		String query = """
				    SELECT r.id, r.id_pemeriksaan, r.id_obat, r.jumlah, o.nama AS namaobat
				    FROM resep r
				    JOIN obat o ON r.id_obat = o.id
				""";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Resep resep = new Resep(
						rs.getInt("id"),
						rs.getInt("id_pemeriksaan"),
						rs.getInt("id_obat"),
						rs.getInt("jumlah"),
						rs.getString("namaobat")
						);
				resepList.add(resep);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void loadObatData() {
		obatList.clear();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM obat")) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Obat obat = new Obat(rs.getInt("id"), rs.getString("nama"), rs.getInt("stok"), rs.getDouble("harga"));
				obatList.add(obat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void loadPemeriksaanData() {
		pemeriksaanList.clear();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pemeriksaan")) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Pemeriksaan pemeriksaan = new Pemeriksaan(rs.getInt("id"),
						rs.getString("tanggal"),
						rs.getInt("id_pasien"),
						rs.getString("keluhan"),
						rs.getInt("id_dokter"));
				pemeriksaanList.add(pemeriksaan);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void showAddForm() {
		Stage formStage = new Stage();
		formStage.setTitle("Tambah Resep");

		GridPane grid = createFormLayout();

		TextField jumlahField = new TextField();
		ComboBox<Obat> obatComboBox = new ComboBox<>(obatList);
		ComboBox<Pemeriksaan> pemeriksaanComboBox = new ComboBox<>(pemeriksaanList);

		// Tampilkan ID dan nama obat di ComboBox
		obatComboBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(Obat obat) {
				return obat != null ? obat.getId() + " - " + obat.getNama() : "";
			}

			@Override
			public Obat fromString(String string) {
				return null; // Tidak diperlukan
			}
		});

		// Tampilkan ID dan nama pemeriksaan di ComboBox
		pemeriksaanComboBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(Pemeriksaan pemeriksaan) {
				return pemeriksaan != null ? pemeriksaan.getId() + " - " + pemeriksaan.getDiagnosa() : "";
			}

			@Override
			public Pemeriksaan fromString(String string) {
				return null; // Tidak diperlukan
			}
		});

		grid.add(new Label("Pemeriksaan (ID - Keluhan):"), 0, 0);
		grid.add(pemeriksaanComboBox, 1, 0);
		grid.add(new Label("Obat (ID - Nama):"), 0, 1);
		grid.add(obatComboBox, 1, 1);
		grid.add(new Label("Jumlah:"), 0, 2);
		grid.add(jumlahField, 1, 2);

		Button saveButton = new Button("Simpan");
		saveButton.setOnAction(e -> {
			Pemeriksaan selectedPemeriksaan = pemeriksaanComboBox.getSelectionModel().getSelectedItem();
			Obat selectedObat = obatComboBox.getSelectionModel().getSelectedItem();
			int jumlah = Integer.parseInt(jumlahField.getText());

			if (selectedPemeriksaan == null || selectedObat == null) {
				showAlert("Kesalahan", "Pilih pemeriksaan dan obat yang valid.");
				return;
			}

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(
							"INSERT INTO resep (id_pemeriksaan, id_obat, jumlah) VALUES (?, ?, ?)")) {
				stmt.setInt(1, selectedPemeriksaan.getId());
				stmt.setInt(2, selectedObat.getId());
				stmt.setInt(3, jumlah);
				stmt.executeUpdate();
				loadData();
				formStage.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});

		VBox formRoot = new VBox(10, grid, saveButton);
		formRoot.setPadding(new Insets(10));

		Scene scene = new Scene(formRoot, 400, 300);
		formStage.setScene(scene);
		formStage.show();
	}

	private static void deleteData() {
		Resep selected = table.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Pilih Resep", "Silakan pilih resep yang akan dihapus.");
			return;
		}
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM resep WHERE id = ?")) {
			stmt.setInt(1, selected.getId());
			stmt.executeUpdate();
			loadData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void showEditForm() {
		Resep selected = table.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Pilih Resep", "Silakan pilih resep yang akan diedit.");
			return;
		}

		Stage formStage = new Stage();
		formStage.setTitle("Edit Resep");

		GridPane grid = createFormLayout();

		TextField jumlahField = new TextField(String.valueOf(selected.getJumlah()));
		ComboBox<Obat> obatComboBox = new ComboBox<>(obatList);
		ComboBox<Pemeriksaan> pemeriksaanComboBox = new ComboBox<>(pemeriksaanList);

		// Set nilai awal ComboBox untuk Obat
		obatComboBox.setValue(obatList.stream()
				.filter(o -> o.getId() == selected.getobatId())
				.findFirst()
				.orElse(null));
		obatComboBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(Obat obat) {
				return obat != null ? obat.getId() + " - " + obat.getNama() : "";
			}

			@Override
			public Obat fromString(String string) {
				return null; // Tidak diperlukan
			}
		});

		// Set nilai awal ComboBox untuk Pemeriksaan
		pemeriksaanComboBox.setValue(pemeriksaanList.stream()
				.filter(p -> p.getId() == selected.getpemeriksaanId())
				.findFirst()
				.orElse(null));
		pemeriksaanComboBox.setConverter(new StringConverter<>() {
			@Override
			public String toString(Pemeriksaan pemeriksaan) {
				return pemeriksaan != null ? pemeriksaan.getId() + " - " + pemeriksaan.getDiagnosa() : "";
			}

			@Override
			public Pemeriksaan fromString(String string) {
				return null; // Tidak diperlukan
			}
		});

		grid.add(new Label("Pemeriksaan (ID - Keluhan):"), 0, 0);
		grid.add(pemeriksaanComboBox, 1, 0);
		grid.add(new Label("Obat (ID - Nama):"), 0, 1);
		grid.add(obatComboBox, 1, 1);
		grid.add(new Label("Jumlah:"), 0, 2);
		grid.add(jumlahField, 1, 2);

		Button saveButton = new Button("Simpan");
		saveButton.setOnAction(e -> {
			Pemeriksaan selectedPemeriksaan = pemeriksaanComboBox.getSelectionModel().getSelectedItem();
			Obat selectedObat = obatComboBox.getSelectionModel().getSelectedItem();
			int jumlah = Integer.parseInt(jumlahField.getText());

			if (selectedPemeriksaan == null || selectedObat == null) {
				showAlert("Kesalahan", "Pilih pemeriksaan dan obat yang valid.");
				return;
			}

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(
							"UPDATE resep SET id_pemeriksaan = ?, id_obat = ?, jumlah = ? WHERE id = ?")) {
				stmt.setInt(1, selectedPemeriksaan.getId());
				stmt.setInt(2, selectedObat.getId());
				stmt.setInt(3, jumlah);
				stmt.setInt(4, selected.getId());
				stmt.executeUpdate();
				loadData();
				formStage.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});

		VBox formRoot = new VBox(10, grid, saveButton);
		formRoot.setPadding(new Insets(10));

		Scene scene = new Scene(formRoot, 400, 300);
		formStage.setScene(scene);
		formStage.show();
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
