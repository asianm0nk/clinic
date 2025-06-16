package org.example;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private StaffDAO staffDAO;
    private Connection conn;

    public LoginForm(Connection conn) {
        this.conn = conn;
        this.staffDAO = new StaffDAO(conn);

        setTitle("Вход в систему");
        setSize(300, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.add(new JLabel("Логин:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Пароль:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginBtn = new JButton("Войти");
        panel.add(new JLabel(""));
        panel.add(loginBtn);

        add(panel);

        loginBtn.addActionListener(e -> login());
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        Staff staff = staffDAO.login(username, password);
        if (staff != null) {
            JOptionPane.showMessageDialog(this, "Добро пожаловать, " + staff.getName());
            dispose();
            new MainUI(staff, conn).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Неверный логин или пароль", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
