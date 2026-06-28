package com.inventory.models;

import java.util.UUID;

public class Item {
    private int id;
    private String uuid;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private String status;

    // Constructor for loading existing items from database
    public Item(int id, String uuid, String name, String category, int quantity, double price, String status) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    // Constructor for creating new items (auto-generates UUID)
    public Item(String name, String category, int quantity, double price, String status) {
        this.id = 0;
        this.uuid = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
