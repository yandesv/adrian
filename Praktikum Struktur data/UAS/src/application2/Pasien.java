package application2;

import javafx.beans.property.*;

public class Pasien {
	private IntegerProperty id;
	private StringProperty nama;
	private StringProperty alamat;
	private StringProperty noTelp;
	
	public Pasien(int id, String nama, String alamat, String noTelp) {
		this.id = new SimpleIntegerProperty(id);
		this.nama = new SimpleStringProperty(nama);
		this.alamat = new SimpleStringProperty(alamat);
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

	public String getAlamat() { 
		return alamat.get();
	}


	public StringProperty alamatProperty() {
		return alamat;
	}

	public String getNoTelp() {
		return noTelp.get();
	}

	public StringProperty noTelpProperty() {
		return noTelp;
	}

}
