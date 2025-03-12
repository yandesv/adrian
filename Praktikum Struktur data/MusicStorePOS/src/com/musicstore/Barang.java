package com.musicstore;

public class Barang {
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    // Constructor
    public Barang(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    // Getter dan Setter
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    @Override
    public String toString() {
        return kode + " - " + nama + " (Rp " + harga + ", Stok: " + stok + ")";
    }
}
