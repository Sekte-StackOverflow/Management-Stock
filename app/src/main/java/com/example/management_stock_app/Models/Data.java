package com.example.management_stock_app.Models;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private User user = new User();
    private List<Barang> barangList = new ArrayList<>();
    private List<Transaksi> transaksiList = new ArrayList<>();
    private String tanggal;

    public Data() {
    }

    public Data(User user, List<Barang> barangList, List<Transaksi> transaksiList, String tanggal) {
        this.user = user;
        this.barangList = barangList;
        this.transaksiList = transaksiList;
        this.tanggal = tanggal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Barang> getBarangList() {
        return barangList;
    }

    public void setBarangList(List<Barang> barangList) {
        this.barangList = barangList;
    }

    public List<Transaksi> getTransaksiList() {
        return transaksiList;
    }

    public void setTransaksiList(List<Transaksi> transaksiList) {
        this.transaksiList = transaksiList;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
