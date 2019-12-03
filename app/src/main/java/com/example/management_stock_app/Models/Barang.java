package com.example.management_stock_app.Models;

public class Barang {
    private String code;
    private String nama;
    private int stock;
    private String gambar;

    public Barang() {
    }

    public Barang(String code , String nama, int stock) {
        this.nama = nama;
        this.stock = stock;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getNama() {
        return nama;
    }

    public int getStock() {
        return stock;
    }

    public String getGambar() {
        return gambar;
    }
}
