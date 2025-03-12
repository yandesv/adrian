package com.musicstore;

import java.util.ArrayList;
import java.util.List;

public class StoreController {
    private List<Barang> barangList = new ArrayList<>();

    // Tambah barang
    public void tambahBarang(Barang barang) {
        barangList.add(barang);
    }

    // Cari barang berdasarkan kode
    public Barang cariBarang(String kode) {
        for (Barang b : barangList) {
            if (b.getKode().equalsIgnoreCase(kode)) {
                return b;
            }
        }
        return null;
    }

    // Tampilkan semua barang
    public void tampilkanBarang() {
        for (Barang b : barangList) {
            System.out.println(b);
        }
    }
}
