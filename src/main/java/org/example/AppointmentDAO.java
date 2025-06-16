package org.example;

import java.sql.*;
import java.util.*;

public class AppointmentDAO {
    private Connection conn;

    public AppointmentDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("staff_id"),
                        rs.getString("date"),
                        rs.getString("reason")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Connection getConn(){ return conn; }

    public void addAppointment(Appointment a) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, staff_id, date, reason) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getPatientId());
            stmt.setInt(2, a.getStaffId());
            stmt.setString(3, a.getDate());
            stmt.setString(4, a.getReason());
            stmt.executeUpdate();
        }
    }

    public void updateAppointment(Appointment a) throws SQLException {
        String sql = "UPDATE appointments SET patient_id = ?, staff_id = ?, date = ?, reason = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getPatientId());
            stmt.setInt(2, a.getStaffId());
            stmt.setString(3, a.getDate());
            stmt.setString(4, a.getReason());
            stmt.setInt(5, a.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteAppointment(int id) throws SQLException {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
