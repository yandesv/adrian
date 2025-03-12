package application2;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Dokter {

	private IntegerProperty id;
	private StringProperty nama;
	private StringProperty spesialisasi;
	private StringProperty noTelp;
	
	public Dokter(int id, String nama, String spesialisasi, String noTelp) {
		this.id = new SimpleIntegerProperty(id);
		this.nama = new SimpleStringProperty(nama);
		this.spesialisasi = new SimpleStringProperty(spesialisasi);
		this.noTelp = new SimpleStringProperty(noTelp);
	}
	public int getId() {
		return id.get();
	}
	
	public IntegerProperty idProperty() {
		return id;
	}
	
	public String getNama() {
		return nama.get();
	}


	public StringProperty namaProperty() {
		return nama;
	}

	public String getSpesialisasi() { 
		return spesialisasi.get();
	}


	public StringProperty spesialisasiProperty() {
		return spesialisasi;
	}

	public String getNoTelp() {
		return noTelp.get();
	}

	public StringProperty noTelpProperty() {
		return noTelp;
	}

}


