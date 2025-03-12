package com.musicstore;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        StoreController store = new StoreController();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Menu Aplikasi Kasir ===");
            System.out.println("1. Tambah Barang");
            System.out.println("2. Lihat Daftar Barang");
            System.out.println("3. Cari Barang");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine(); // Konsumsi newline

            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan kode barang: ");
                    String kode = scanner.nextLine();
                    System.out.print("Masukkan nama barang: ");
                    String nama = scanner.nextLine();
                    System.out.print("Masukkan harga barang: ");
                    double harga = scanner.nextDouble();
                    System.out.print("Masukkan stok barang: ");
                    int stok = scanner.nextInt();
                    Barang barang = new Barang(kode, nama, harga, stok);
                    store.tambahBarang(barang);
                    System.out.println("Barang berhasil ditambahkan.");
                    break;

                case 2:
                    System.out.println("=== Daftar Barang ===");
                    store.tampilkanBarang();
                    break;

                case 3:
                    System.out.print("Masukkan kode barang yang dicari: ");
                    String kodeCari = scanner.nextLine();
                    Barang barangCari = store.cariBarang(kodeCari);
                    if (barangCari != null) {
                        System.out.println("Barang ditemukan: " + barangCari);
                    } else {
                        System.out.println("Barang tidak ditemukan.");
                    }
                    break;

                case 4:
                    System.out.println("Terima kasih telah menggunakan aplikasi.");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }
}
