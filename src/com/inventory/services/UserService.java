package com.inventory.services;

import com.inventory.db.DatabaseManager;
import com.inventory.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    /**
     * Authenticates a user with a username and password.
     * 
     * @param username The username.
     * @param password The password.
     * @return User object if successful, null if failed.
     */
    public User authenticate(String username, String password) {
        String sql = "SELECT id, uuid, username, display_name, role FROM users WHERE username = ? AND password_hash = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("uuid"),
                        rs.getString("username"),
                        rs.getString("display_name"),
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during authentication.");
            e.printStackTrace();
        }
        return null;
    }
}
