package com.inventory.services;

import com.inventory.db.DatabaseManager;
import com.inventory.models.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemService {

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT id, uuid, name, category, quantity, price, status FROM items ORDER BY id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                items.add(extractItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("Database error during getAllItems.");
            e.printStackTrace();
        }
        return items;
    }

    public List<Item> searchItems(String query) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT id, uuid, name, category, quantity, price, status FROM items WHERE name LIKE ? OR category LIKE ? ORDER BY id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String wildQuery = "%" + query + "%";
            stmt.setString(1, wildQuery);
            stmt.setString(2, wildQuery);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(extractItem(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during searchItems.");
            e.printStackTrace();
        }
        return items;
    }

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (uuid, name, category, quantity, price, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, item.getUuid());
            stmt.setString(2, item.getName());
            stmt.setString(3, item.getCategory());
            stmt.setInt(4, item.getQuantity());
            stmt.setDouble(5, item.getPrice());
            stmt.setString(6, item.getStatus());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        item.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database error during addItem.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateItem(Item item) {
        String sql = "UPDATE items SET name = ?, category = ?, quantity = ?, price = ?, status = ? WHERE uuid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getPrice());
            stmt.setString(5, item.getStatus());
            stmt.setString(6, item.getUuid());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error during updateItem.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteItemByUuid(String uuid) {
        String sql = "DELETE FROM items WHERE uuid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, uuid);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error during deleteItemByUuid.");
            e.printStackTrace();
        }
        return false;
    }

    private Item extractItem(ResultSet rs) throws SQLException {
        return new Item(
            rs.getInt("id"),
            rs.getString("uuid"),
            rs.getString("name"),
            rs.getString("category"),
            rs.getInt("quantity"),
            rs.getDouble("price"),
            rs.getString("status")
        );
    }
}
