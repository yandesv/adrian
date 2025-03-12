package application2;
import javafx.beans.property.*;
public class Pemeriksaan {
	private IntegerProperty id;
	private StringProperty tanggal;
	private IntegerProperty pasienId;
	private StringProperty diagnosa;
	private IntegerProperty dokterId;
	//variabel laporan pemeriksaan
	private StringProperty namapasien;
	private StringProperty namadokter;
	private ObjectProperty<java.sql.Date>tanggalperiksa;
	public Pemeriksaan (int id, String tanggal, int pasienId, String diagnosa, int dokterId) {
		this.id = new SimpleIntegerProperty(id);
		this.tanggal = new SimpleStringProperty(tanggal);
		this.pasienId = new SimpleIntegerProperty(pasienId);
		this.diagnosa = new SimpleStringProperty(diagnosa);
		this.dokterId = new SimpleIntegerProperty(dokterId);
	}
	public Pemeriksaan (int id, int pasienId, int dokterId, java.sql.Date tanggal, String diagnosa,String dokterNama, String pasienNama) {
		this.id = new SimpleIntegerProperty(id);
		this.dokterId = new SimpleIntegerProperty(dokterId);
		this.tanggalperiksa = new SimpleObjectProperty<>(tanggal);
		this.pasienId = new SimpleIntegerProperty(pasienId);
		this.diagnosa = new SimpleStringProperty(diagnosa);
		this.namadokter = new SimpleStringProperty(dokterNama);
		this.namapasien = new SimpleStringProperty(pasienNama);
	}
	public int getId() {
		return id.get();
	}
	public IntegerProperty idProperty() {
		return id;
	}
	public String getTanggal() {
		return tanggal.get();
	}
	public StringProperty tanggalProperty() {
		return tanggal;
	}
	public int getPasienId() {
		return pasienId.get();
	}
	public IntegerProperty pasienIdProperty() { 
		return pasienId;
	}
	public String getDiagnosa() {
		return diagnosa.get();
	}
	public StringProperty diagnosaProperty() {
		return diagnosa;
	}
	public int getDokterId() {
		return dokterId.get();
	}
	public IntegerProperty dokterIdProperty() {
		return dokterId;
	}
	//================= pemeriksaan laporan
	public StringProperty pasienNamaProperty() {
		return namapasien;
	}
	public StringProperty dokterNamaProperty() {
		return namadokter;
	}
	public ObjectProperty <java.sql.Date>tanggalPeriksaProperty() {
		return tanggalperiksa;
	}
}
