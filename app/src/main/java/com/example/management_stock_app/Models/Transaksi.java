package com.example.management_stock_app.Models;

public class Transaksi {
    private String id;
    private String date;
    private int currentStock;
    private String status;

    public Transaksi() {
    }

    public Transaksi(String id, String date, int currentStock, String status) {
        this.id = id;
        this.currentStock = currentStock;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public String getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
