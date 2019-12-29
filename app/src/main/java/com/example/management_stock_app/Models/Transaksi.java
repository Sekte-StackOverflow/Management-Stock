package com.example.management_stock_app.Models;

public class Transaksi {
    private String id;
    private String name;
    private String date;
    private int currentStock;
    private String status;

    public Transaksi() {
    }

    public Transaksi(String id, String name, String date, int currentStock, String status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.currentStock = currentStock;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStatus() {
        return status;
    }
}
