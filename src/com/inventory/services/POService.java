package com.inventory.services;

import com.inventory.db.DatabaseManager;
import com.inventory.models.PurchaseOrder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class POService {

    public boolean addOrder(PurchaseOrder po) {
        String sql = "INSERT INTO purchase_orders (uuid, item_uuid, supplier_name, quantity, status, order_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, po.getUuid());
            stmt.setString(2, po.getItemUuid());
            stmt.setString(3, po.getSupplierName());
            stmt.setInt(4, po.getQuantity());
            stmt.setString(5, po.getStatus());
            stmt.setTimestamp(6, po.getOrderDate());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error during addOrder.");
            e.printStackTrace();
        }
        return false;
    }

    public int getPendingArrivalsCount() {
        String sql = "SELECT SUM(quantity) FROM purchase_orders WHERE status = 'Pending'";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Database error during getPendingArrivalsCount.");
            e.printStackTrace();
        }
        return 0;
    }
}
