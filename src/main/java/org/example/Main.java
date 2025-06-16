package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.SwingUtilities;


public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres"; // замените на свою БД
        String user = "postgres";
        String password = "postgres";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);

            SwingUtilities.invokeLater(() -> {
                new LoginForm(conn).setVisible(true);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
