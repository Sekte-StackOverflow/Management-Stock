package com.example.management_stock_app.Models;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private String name;
    private String email;
    private List<Barang> barangList = new ArrayList<>();
    private List<Transaksi> transaksiList = new ArrayList<>();
    private String tanggal;

    public Data() {
    }

    public Data(String name, String email, List<Barang> barangList, List<Transaksi> transaksiList, String tanggal) {
        this.name = name;
        this.email = email;
        this.barangList = barangList;
        this.transaksiList = transaksiList;
        this.tanggal = tanggal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
