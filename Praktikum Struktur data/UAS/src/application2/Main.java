package application2;

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
        MenuItem pasienMenu = new MenuItem("Pasien");
        MenuItem dokterMenu = new MenuItem("Dokter");
        MenuItem pemeriksaanMenu = new MenuItem("Pemeriksaan");
        MenuItem obatMenu = new MenuItem("Obat");
        MenuItem resepMenu = new MenuItem("Resep");

        menuData.getItems().addAll(pasienMenu, dokterMenu, pemeriksaanMenu, obatMenu, resepMenu);

        Menu menuLaporan = new Menu("Laporan");
        MenuItem laporanMenu = new MenuItem("Laporan Rekam Medik");
        menuLaporan.getItems().add(laporanMenu);

        menuBar.getMenus().addAll(menuData, menuLaporan);

        //Root Layout
        VBox root = new VBox(menuBar);
        //Menambahkan gambar sebagi latar belakang
        Image image = new Image(getClass().getResourceAsStream("img/rsgm.jpg"));
        BackgroundImage backgroundImage = new BackgroundImage(image,
        		BackgroundRepeat.NO_REPEAT,
        		BackgroundRepeat.NO_REPEAT,
        		BackgroundPosition.CENTER,
        		new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,false,false,true,false));
        root.setBackground(new Background(backgroundImage));
        //Scene
        Scene scene = new Scene(root, 800, 600);

        
        //Event Handlers 
        pasienMenu.setOnAction(e -> PasienCRUD.show());
        dokterMenu.setOnAction(e -> DokterCRUD.show());
        pemeriksaanMenu.setOnAction(e -> PemeriksaanCRUD.show());
        obatMenu.setOnAction(e -> ObatCRUD.show());  
        resepMenu.setOnAction(e ->ResepCRUD.show());
         
       laporanMenu.setOnAction(e -> Pemeriksaanlaporan.show());
        
        
        primaryStage.setTitle("Sistem Rekam Medik");
        primaryStage.setScene(scene);
        primaryStage.show();
}

	public static void main(String[] args) {
		launch(args);
	}
}