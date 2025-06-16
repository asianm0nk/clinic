package org.example;

import java.sql.*;
import java.util.*;

public class PatientDAO {
    private Connection conn;

    public PatientDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("birth_date"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public void addPatient(Patient p) throws SQLException {
        String sql = "INSERT INTO patients (name, birth_date, phone) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getBirthDate());
            stmt.setString(3, p.getPhone());
            stmt.executeUpdate();
        }
    }

    public void updatePatient(Patient p) throws SQLException {
        String sql = "UPDATE patients SET name = ?, birth_date = ?, phone = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getBirthDate());
            stmt.setString(3, p.getPhone());
            stmt.setInt(4, p.getId());
            stmt.executeUpdate();
        }
    }

    public void deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
