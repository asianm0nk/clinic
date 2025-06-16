package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MainUI extends JFrame {
    private Staff currentUser;
    private Connection conn;

    public MainUI(Staff staff, Connection conn) {
        this.currentUser = staff;
        this.conn = conn;

        setTitle("Медицинская система - пользователь: " + staff.getName());
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        PatientDAO patientDAO = new PatientDAO(conn);
        AppointmentDAO appointmentDAO = new AppointmentDAO(conn);
        StaffDAO staffDAO = new StaffDAO(conn);
        LoggerDAO loggerDAO = new LoggerDAO(conn);

        tabbedPane.add("Пациенты", new PatientManagementPanel(patientDAO, loggerDAO, currentUser));
        tabbedPane.add("Приёмы", new AppointmentManagementPanel(appointmentDAO, patientDAO, staffDAO, loggerDAO, currentUser));

        add(tabbedPane);
    }
}
