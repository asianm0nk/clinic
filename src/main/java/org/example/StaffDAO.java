package org.example;

import java.sql.*;

public class StaffDAO {
    private Connection conn;

    public StaffDAO(Connection conn) {
        this.conn = conn;
    }

    public Staff login(String username, String password) {
        String sql = "SELECT * FROM staff WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Staff(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getInt("hospital_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
