package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class PatientManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private PatientDAO patientDAO;
    private Staff currentUser;
    private LoggerDAO loggerDAO;

    public PatientManagementPanel(PatientDAO patientDAO, LoggerDAO loggerDAO, Staff currentUser) {
        this.patientDAO = patientDAO;
        this.loggerDAO = loggerDAO;
        this.currentUser = currentUser;

        setLayout(new BorderLayout());

        // Таблица
        model = new DefaultTableModel(new Object[]{"ID", "Имя", "Дата рождения", "Телефон"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Панель кнопок
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Редактировать");
        JButton deleteBtn = new JButton("Удалить");
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadPatients();

        // Кнопка добавления
        addBtn.addActionListener(e -> {
            Patient p = showPatientDialog(null);
            if (p != null) {
                try {
                    patientDAO.addPatient(p);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                loggerDAO.log(currentUser.getId(), "CREATE", "patients", "Добавлен пациент: " + p.getName());
                loadPatients();
            }
        });

        // Кнопка редактирования
        editBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) model.getValueAt(selectedRow, 0);
                String name = (String) model.getValueAt(selectedRow, 1);
                String birth = (String) model.getValueAt(selectedRow, 2);
                String phone = (String) model.getValueAt(selectedRow, 3);
                Patient p = new Patient(id, name, birth, phone);
                Patient updated = showPatientDialog(p);
                if (updated != null) {
                    try {
                        patientDAO.updatePatient(updated);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    loggerDAO.log(currentUser.getId(), "UPDATE", "patients", "Изменён пациент: " + updated.getName());
                    loadPatients();
                }
            }
        });

        // Кнопка удаления
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) model.getValueAt(selectedRow, 0);
                String name = (String) model.getValueAt(selectedRow, 1);
                int confirm = JOptionPane.showConfirmDialog(this, "Удалить пациента " + name + "?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        patientDAO.deletePatient(id);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    loggerDAO.log(currentUser.getId(), "DELETE", "patients", "Удалён пациент: " + name);
                    loadPatients();
                }
            }
        });
    }

    private void loadPatients() {
        model.setRowCount(0);
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient p : patients) {
            model.addRow(new Object[]{p.getId(), p.getName(), p.getBirthDate(), p.getPhone()});
        }
    }

    private Patient showPatientDialog(Patient existing) {
        JTextField nameField = new JTextField();
        JTextField birthField = new JTextField();
        JTextField phoneField = new JTextField();

        if (existing != null) {
            nameField.setText(existing.getName());
            birthField.setText(existing.getBirthDate());
            phoneField.setText(existing.getPhone());
        }

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Имя:")); panel.add(nameField);
        panel.add(new JLabel("Дата рождения:")); panel.add(birthField);
        panel.add(new JLabel("Телефон:")); panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this, panel, existing == null ? "Добавить пациента" : "Редактировать пациента", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String birth = birthField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Имя не может быть пустым!");
                return null;
            }

            if (existing == null)
                return new Patient(0, name, birth, phone);
            else {
                existing.setName(name);
                existing.setBirthDate(birth);
                existing.setPhone(phone);
                return existing;
            }
        }
        return null;
    }
}
