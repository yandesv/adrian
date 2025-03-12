package application2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class Pemeriksaanlaporan {

    private static TableView<Pemeriksaan> table = new TableView<>();
    private static ObservableList<Pemeriksaan> laporanList = FXCollections.observableArrayList();

    public static void show() {

        Stage stage = new Stage();
        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        // Tabel untuk menampilkan laporan pemeriksaan
        table = new TableView<>();

        // Kolom untuk ID Pemeriksaan
        TableColumn<Pemeriksaan, Integer> colId = new TableColumn<>("ID Pemeriksaan");
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());

        // Kolom untuk Nama Pasien
        TableColumn<Pemeriksaan, String> colPasienNama = new TableColumn<>("Nama Pasien");
        colPasienNama.setCellValueFactory(data -> data.getValue().pasienNamaProperty());

        // Kolom untuk Nama Dokter
        TableColumn<Pemeriksaan, String> colDokterNama = new TableColumn<>("Nama Dokter");
        colDokterNama.setCellValueFactory(data -> data.getValue().dokterNamaProperty());

        // Kolom untuk Tanggal Pemeriksaan
        TableColumn<Pemeriksaan, Date> colTanggal = new TableColumn<>("Tanggal");
        colTanggal.setCellValueFactory(data -> data.getValue().tanggalPeriksaProperty());

        // Kolom untuk Diagnosa
        TableColumn<Pemeriksaan, String> colDiagnosa = new TableColumn<>("Keluhan");
        colDiagnosa.setCellValueFactory(data -> data.getValue().diagnosaProperty());

        table.getColumns().addAll(colId, colPasienNama, colDokterNama, colTanggal, colDiagnosa);
        table.setItems(laporanList);

        // Button untuk memuat data laporan
        Button loadButton = new Button("Muatan Laporan");
        loadButton.setOnAction(e -> loadLaporanData());

        root.getChildren().addAll(table, loadButton);

        Scene scene = new Scene(root, 800, 400);
        stage.setScene(scene);
        stage.setTitle("Laporan Pemeriksaan");
        stage.show();
    }

    private static void loadLaporanData() {
        laporanList.clear();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT p.*, pa.nama AS pasien_nama, d.nama AS dokter_nama " +
                             "FROM pemeriksaan p " +
                             "JOIN pasien pa ON p.id_pasien = pa.id " +
                             "JOIN dokter d ON p.id_dokter = d.id")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pemeriksaan pemeriksaan = new Pemeriksaan(
                        rs.getInt("id"),
                        rs.getInt("id_pasien"),
                        rs.getInt("id_dokter"),
                        rs.getDate("tanggal"),
                        rs.getString("keluhan"),
                        rs.getString("pasien_nama"),
                        rs.getString("dokter_nama")
                );
                laporanList.add(pemeriksaan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}