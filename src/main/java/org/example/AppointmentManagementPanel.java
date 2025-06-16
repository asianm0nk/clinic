package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class AppointmentManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private AppointmentDAO appointmentDAO;
    private PatientDAO patientDAO;
    private StaffDAO staffDAO;
    private LoggerDAO loggerDAO;
    private Staff currentUser;

    public AppointmentManagementPanel(AppointmentDAO appointmentDAO, PatientDAO patientDAO, StaffDAO staffDAO,
                                      LoggerDAO loggerDAO, Staff currentUser) {
        this.appointmentDAO = appointmentDAO;
        this.patientDAO = patientDAO;
        this.staffDAO = staffDAO;
        this.loggerDAO = loggerDAO;
        this.currentUser = currentUser;

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Пациент", "Сотрудник", "Дата", "Причина"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadAppointments();

        addBtn.addActionListener(e -> {
            Appointment a = showAppointmentDialog(null);
            if (a != null) {
                try {
                    appointmentDAO.addAppointment(a);
                    loggerDAO.log(currentUser.getId(), "CREATE", "appointments", "Добавлен приём для пациента ID " + a.getPatientId());
                    loadAppointments();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int patientId = getPatientIdByName((String) model.getValueAt(row, 1));
                int staffId = getStaffIdByName((String) model.getValueAt(row, 2));
                String date = (String) model.getValueAt(row, 3);
                String reason = (String) model.getValueAt(row, 4);
                Appointment a = new Appointment(id, patientId, staffId, date, reason);

                Appointment updated = showAppointmentDialog(a);
                if (updated != null) {
                    try {
                        appointmentDAO.updateAppointment(updated);
                        loggerDAO.log(currentUser.getId(), "UPDATE", "appointments", "Изменён приём ID " + updated.getId());
                        loadAppointments();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Удалить приём ID " + id + "?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        appointmentDAO.deleteAppointment(id);
                        loggerDAO.log(currentUser.getId(), "DELETE", "appointments", "Удалён приём ID " + id);
                        loadAppointments();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadAppointments() {
        model.setRowCount(0);
        List<Appointment> list = appointmentDAO.getAllAppointments();
        for (Appointment a : list) {
            String patientName = getPatientNameById(a.getPatientId());
            String staffName = getStaffNameById(a.getStaffId());
            model.addRow(new Object[]{a.getId(), patientName, staffName, a.getDate(), a.getReason()});
        }
    }

    private String getPatientNameById(int id) {
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient p : patients) {
            if (p.getId() == id) return p.getName();
        }
        return "Неизвестен";
    }

    private int getPatientIdByName(String name) {
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient p : patients) {
            if (p.getName().equals(name)) return p.getId();
        }
        return -1;
    }

    private String getStaffNameById(int id) {
        // Можно оптимизировать, если заранее загрузить всех сотрудников
        try {
            String sql = "SELECT name FROM staff WHERE id = ?";
            try (PreparedStatement stmt = appointmentDAO.getConn().prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Неизвестен";
    }

    private int getStaffIdByName(String name) {
        try {
            String sql = "SELECT id FROM staff WHERE name = ?";
            try (PreparedStatement stmt = appointmentDAO.getConn().prepareStatement(sql)) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Appointment showAppointmentDialog(Appointment existing) {
        JComboBox<String> patientCombo = new JComboBox<>();
        for (Patient p : patientDAO.getAllPatients()) patientCombo.addItem(p.getName());

        JComboBox<String> staffCombo = new JComboBox<>();
        try {
            Statement stmt = appointmentDAO.getConn().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM staff");
            while (rs.next()) staffCombo.addItem(rs.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTextField dateField = new JTextField();
        JTextField reasonField = new JTextField();

        if (existing != null) {
            patientCombo.setSelectedItem(getPatientNameById(existing.getPatientId()));
            staffCombo.setSelectedItem(getStaffNameById(existing.getStaffId()));
            dateField.setText(existing.getDate());
            reasonField.setText(existing.getReason());
        }

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Пациент:")); panel.add(patientCombo);
        panel.add(new JLabel("Сотрудник:")); panel.add(staffCombo);
        panel.add(new JLabel("Дата (YYYY-MM-DD):")); panel.add(dateField);
        panel.add(new JLabel("Причина:")); panel.add(reasonField);

        int res = JOptionPane.showConfirmDialog(this, panel, existing == null ? "Добавить приём" : "Редактировать приём", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            int patientId = getPatientIdByName((String) patientCombo.getSelectedItem());
            int staffId = getStaffIdByName((String) staffCombo.getSelectedItem());
            String date = dateField.getText().trim();
            String reason = reasonField.getText().trim();
            if (existing == null) {
                return new Appointment(0, patientId, staffId, date, reason);
            } else {
                return new Appointment(existing.getId(), patientId, staffId, date, reason);
            }
        }
        return null;
    }
}
