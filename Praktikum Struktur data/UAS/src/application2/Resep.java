package application2;
import javafx.beans.property.*;
public class Resep {
	private IntegerProperty id;
	private IntegerProperty pemeriksaanId;
	private IntegerProperty obatId;
	private IntegerProperty jumlah;
	private StringProperty namaobat;

	public Resep (int id, int pemeriksaanId, int obatId, int jumlah) {
		this.id = new SimpleIntegerProperty(id);
		this.pemeriksaanId = new SimpleIntegerProperty(pemeriksaanId);
		this.obatId = new SimpleIntegerProperty(obatId);
		this.jumlah = new SimpleIntegerProperty(jumlah);
	}
	
	public Resep (int id, int pemeriksaanId, int obatId, int jumlah, String obatNama) {
		this.id = new SimpleIntegerProperty(id);
		this.pemeriksaanId = new SimpleIntegerProperty(pemeriksaanId);
		this.obatId = new SimpleIntegerProperty(obatId);
		this.jumlah = new SimpleIntegerProperty(jumlah);
		this.namaobat = new SimpleStringProperty(obatNama);
	}
	
	public int getId() { 
		return id.get();
	}
	
	public IntegerProperty idProperty() {
		return id;
	}
	
	public int getpemeriksaanId() {
		return pemeriksaanId.get();
	}
	
	public IntegerProperty pemeriksaanIdProperty() {
		return pemeriksaanId;
	}
	
	public int getobatId() {
		return obatId.get();
	}
	
	public IntegerProperty obatIdProperty() {
		return obatId;
	}
	
	public int getJumlah() {
		return jumlah.get();
	}
	
	public IntegerProperty jumlahProperty() {
		return jumlah;
	}
	
//	Resep laporan
	public StringProperty obatNamaProperty() {
		return namaobat;
	}
	
}