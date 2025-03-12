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

public class PemeriksaanCRUD {

	private static TableView<Pemeriksaan> table = new TableView<>();
	private static ObservableList<Pemeriksaan> pemeriksaanList = FXCollections.observableArrayList();
	private static ObservableList<Dokter> dokterList = FXCollections.observableArrayList();
	private static ObservableList<Pasien> pasienList = FXCollections.observableArrayList();
	
	public static void show() {
		Stage stage = new Stage();
		VBox root = new VBox();
		root.setPadding(new Insets(10));
		root.setSpacing(10);
		
		// Tabel untuk menampilkan data pemeriksaan
		table = new TableView<>();
		TableColumn<Pemeriksaan, Integer> colId = new TableColumn<>("ID");
		colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());

		TableColumn<Pemeriksaan, String> colTanggal = new TableColumn("Tanggal"); 
		colTanggal.setCellValueFactory(data -> data.getValue().tanggalProperty());

		TableColumn<Pemeriksaan, Integer> colPasienId = new TableColumn<>("Pasien ID"); 
		colPasienId.setCellValueFactory(data -> data.getValue().pasienIdProperty().asObject());

		TableColumn<Pemeriksaan, String> coIdiagnosa = new TableColumn<>("Diagnosa"); coIdiagnosa.setCellValueFactory(data -> data.getValue().diagnosaProperty());
		TableColumn<Pemeriksaan, Integer> coIdokterId = new TableColumn<>("Dokter ID"); coIdokterId.setCellValueFactory(data -> data.getValue().dokterIdProperty().asObject());
		table.getColumns().addAll(colId, colTanggal, colPasienId, coIdiagnosa, coIdokterId);
		table.setItems(pemeriksaanList);
		//Tombol CRUD
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
		stage.setTitle("CRUD Pemeriksaan");
		stage.show();
		loadDokterData();
		loadPasienData();
		loadData();
	}

	private static void loadData() {
		pemeriksaanList.clear();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pemeriksaan")) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Pemeriksaan pemeriksaan = new Pemeriksaan(
						rs.getInt("id"),
						rs.getString("tanggal"),
						rs.getInt("id_pasien"), 
						rs.getString("keluhan"), 
						rs.getInt("id_dokter")
						);
				pemeriksaanList.add(pemeriksaan);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void loadDokterData() {
		dokterList.clear();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM dokter")) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Dokter dokter = new Dokter(rs.getInt("id"), rs.getString("nama"), rs.getString("spesialisasi"), rs.getString("no_Telp"));
				dokterList.add(dokter);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void loadPasienData() {
		pasienList.clear();
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pasien")) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Pasien pasien = new Pasien(rs.getInt("id"), rs.getString("nama"), rs.getString("alamat"),
						rs.getString("no_telp"));
				pasienList.add(pasien);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void showAddForm() {
	    Stage formStage = new Stage();
	    formStage.setTitle("Tambah Pemeriksaan");
	    GridPane grid = createFormLayout();
	    
	    // Input untuk tanggal pemeriksaan
	    TextField tanggalField = new TextField();
	    
	    // ComboBox untuk memilih pasien
	    ComboBox<Pasien> pasienComboBox = new ComboBox<>(pasienList);
	    pasienComboBox.setCellFactory(param -> new ListCell<Pasien>() {
	        @Override
	        protected void updateItem(Pasien item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                setText(item.getId() + " - " + item.getNama());
	            }
	        }
	    });
	    pasienComboBox.setButtonCell(new ListCell<Pasien>() {
	        @Override
	        protected void updateItem(Pasien item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                setText(item.getId() + " - " + item.getNama());
	            }
	        }
	    });

	    // Input untuk diagnosa
	    TextField diagnosaField = new TextField();
	    
	    // ComboBox untuk memilih dokter
	    ComboBox<Dokter> dokterComboBox = new ComboBox<>(dokterList);
	    dokterComboBox.setCellFactory(param -> new ListCell<Dokter>() {
	        @Override
	        protected void updateItem(Dokter item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                setText(item.getId() + " - " + item.getNama());
	            }
	        }
	    });
	    dokterComboBox.setButtonCell(new ListCell<Dokter>() {
	        @Override
	        protected void updateItem(Dokter item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                setText(item.getId() + " - " + item.getNama());
	            }
	        }
	    });

	    // Menambahkan elemen ke Grid
	    grid.add(new Label("Tanggal (YYYY-MM-DD):"), 0, 0);
	    grid.add(tanggalField, 1, 0);
	    grid.add(new Label("Pasien:"), 0, 1);
	    grid.add(pasienComboBox, 1, 1);
	    grid.add(new Label("Diagnosa:"), 0, 2);
	    grid.add(diagnosaField, 1, 2);
	    grid.add(new Label("Dokter:"), 0, 3);
	    grid.add(dokterComboBox, 1, 3);

	    // Tombol simpan
	    Button saveButton = new Button("Simpan");
	    saveButton.setOnAction(e -> {
	        String tanggal = tanggalField.getText();
	        Pasien selectedPasien = pasienComboBox.getSelectionModel().getSelectedItem();
	        String diagnosa = diagnosaField.getText();
	        Dokter selectedDokter = dokterComboBox.getSelectionModel().getSelectedItem();
	        if (selectedDokter != null && selectedPasien != null) {
	            try (Connection conn = DBConnection.getConnection();
	                 PreparedStatement stmt = conn.prepareStatement(
	                         "INSERT INTO pemeriksaan (tanggal, id_pasien, keluhan, id_dokter) VALUES (?, ?, ?, ?)")) {
	                stmt.setString(1, tanggal);
	                stmt.setInt(2, selectedPasien.getId());
	                stmt.setString(3, diagnosa);
	                stmt.setInt(4, selectedDokter.getId());
	                stmt.executeUpdate();
	                loadData();
	                formStage.close();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    });

	    // Menyusun layout form
	    VBox formRoot = new VBox(10, grid, saveButton);
	    formRoot.setPadding(new Insets(10));
	    Scene scene = new Scene(formRoot, 400, 300);
	    formStage.setScene(scene);
	    formStage.show();
	}
	
	private static void showEditForm() {
	    Pemeriksaan selected = table.getSelectionModel().getSelectedItem();
	    if (selected == null) {
	        showAlert("Pilih Pemeriksaan", "Silakan pilih pemeriksaan yang akan diedit."); 
	        return;
	    }

	    Stage formStage = new Stage();
	    formStage.setTitle("Edit Pemeriksaan");
	    GridPane grid = createFormLayout();
	    
	    // Input untuk tanggal pemeriksaan
	    TextField tanggalField = new TextField(selected.getTanggal());
	    
	    // ComboBox untuk memilih pasien
	    ComboBox<Pasien> pasienComboBox = new ComboBox<>(pasienList);
	    pasienComboBox.setCellFactory(param -> new ListCell<Pasien>() {
	        @Override
	        protected void updateItem(Pasien item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                setText(item.getId() + " - " + item.getNama());
	            }
	        }
	    });
	    pasienComboBox.setButtonCell(new ListCell<Pasien>() {
	        @Override
	        protected void updateItem(Pasien item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                setText(item.getId() + " - " + item.getNama());
	            }
	        }
	    });

	    // ComboBox untuk memilih dokter
	    ComboBox<Dokter> dokterComboBox = new ComboBox<>(dokterList);
	    dokterComboBox.setCellFactory(param -> new ListCell<Dokter>() {
	        @Override
	        protected void updateItem(Dokter item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                setText(item.getId() + " - " + item.getNama());
	            }
	        }
	    });
	    dokterComboBox.setButtonCell(new ListCell<Dokter>() {
	        @Override
	        protected void updateItem(Dokter item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty || item == null) {
	                setText(null);
	            } else {
	                setText(item.getId() + " - " + item.getNama());
	            }
	        }
	    });

	    // Preselect pasien dan dokter yang sudah dipilih pada pemeriksaan yang dipilih
	    pasienComboBox.getSelectionModel().select(selected.getPasienId());
	    dokterComboBox.getSelectionModel().select(selected.getDokterId());

	    // Input untuk diagnosa
	    TextField diagnosaField = new TextField(selected.getDiagnosa());

	    // Menambahkan elemen ke Grid
	    grid.add(new Label("Tanggal (YYYY-MM-DD):"), 0, 0);
	    grid.add(tanggalField, 1, 0);
	    grid.add(new Label("Pasien:"), 0, 1);
	    grid.add(pasienComboBox, 1, 1);
	    grid.add(new Label("Diagnosa:"), 0, 2);
	    grid.add(diagnosaField, 1, 2);
	    grid.add(new Label("Dokter:"), 0, 3);
	    grid.add(dokterComboBox, 1, 3);

	    // Tombol simpan
	    Button saveButton = new Button("Simpan");
	    saveButton.setOnAction(e -> {
	        String tanggal = tanggalField.getText();
	        Pasien selectedPasien = pasienComboBox.getSelectionModel().getSelectedItem();
	        String diagnosa = diagnosaField.getText();
	        Dokter selectedDokter = dokterComboBox.getSelectionModel().getSelectedItem();
	        if (selectedDokter != null && selectedPasien != null) {
	            try (Connection conn = DBConnection.getConnection();
	                 PreparedStatement stmt = conn.prepareStatement(
	                         "UPDATE pemeriksaan SET tanggal=?, id_pasien=?, keluhan=?, id_dokter=? WHERE id = ?")) {
	                stmt.setString(1, tanggal);
	                stmt.setInt(2, selectedPasien.getId());
	                stmt.setString(3, diagnosa);
	                stmt.setInt(4, selectedDokter.getId());
	                stmt.setInt(5, selected.getId());
	                stmt.executeUpdate();
	                loadData();
	                formStage.close();
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    });

	    // Menyusun layout form
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
	private static void deleteData() {
		Pemeriksaan selected = table.getSelectionModel().getSelectedItem();
		if (selected == null) { 
			showAlert("Pilih Pemeriksaan", "Silakan pilih pemeriksaan yang akan dihapus."); 
			return; 
		}
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM pemeriksaan WHERE id = ?"))
		{
			stmt.setInt(1, selected.getId());
			stmt.executeUpdate();
			loadData();
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