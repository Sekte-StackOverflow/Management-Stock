package com.example.management_stock_app.Models;

public class Transaksi {
    private String id;
    private int currentStock;

    public Transaksi() {
    }

    public Transaksi(String id, int currentStock) {
        this.id = id;
        this.currentStock = currentStock;
    }

    public String getId() {
        return id;
    }

    public int getCurrentStock() {
        return currentStock;
    }

}
