package ui;

import db.DatabaseConnection;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("University Course Management - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> authenticate());
        panel.add(loginButton);

        add(panel);
        setVisible(true);
    }

    private void authenticate() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // In production, hash this!
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("email")
                );
                dispose(); // Close login window
                openDashboard(user);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!");
        }
    }

    private void openDashboard(User user) {
        switch (user.getRole()) {
            case "Admin":
                new AdminDashboard(user);
                break;
            case "Instructor":
                new InstructorDashboard(user);
                break;
            case "Student":
                new StudentDashboard(user);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unknown role!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}