package application2;

import javafx.beans.property.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Obat {

	private IntegerProperty id;
	private StringProperty nama;
	private IntegerProperty stok;
	private DoubleProperty harga;
	
	public Obat(int id, String nama, int stok, double harga) {
		this.id = new SimpleIntegerProperty(id);
		this.nama = new SimpleStringProperty(nama);
		this.stok = new SimpleIntegerProperty(stok);
		this.harga = new SimpleDoubleProperty(harga);
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

	public Integer getStok() { 
		return stok.get();
	}

	public IntegerProperty stokProperty() {
		return stok;
	}

	public Double getHarga() {
		return harga.get();
	}

	public DoubleProperty hargaProperty() {
		return harga;
	}

}


