package com.example.management_stock_app.Models;

public class Admin {
    private String username;
    private String email;

    public Admin() {
    }
    public Admin(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
