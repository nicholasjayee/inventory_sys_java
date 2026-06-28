package com.inventory.models;

public class User {
    private int id;
    private String uuid;
    private String username;
    private String displayName;
    private String role;

    public User(int id, String uuid, String username, String displayName, String role) {
        this.id = id;
        this.uuid = uuid;
        this.username = username;
        this.displayName = displayName;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }
}
