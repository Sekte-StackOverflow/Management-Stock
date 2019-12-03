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

    public void inputStock(int i) {
        if (currentStock > 0) {
            this.currentStock += i;
        }
    }

    public void outputStock(int i) {
        if (currentStock > 0) {
            this.currentStock -= i;
        }
    }
}
