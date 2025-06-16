package org.example;

import java.sql.*;
import java.time.LocalDateTime;

public class LoggerDAO {
    private Connection conn;

    public LoggerDAO(Connection conn) {
        this.conn = conn;
    }

    public void log(int userId, String action, String tableName, String details) {
        String sql = "INSERT INTO logs (user_id, action, table_name, timestamp, details) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, action);
            stmt.setString(3, tableName);
            stmt.setString(4, LocalDateTime.now().toString());
            stmt.setString(5, details);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
