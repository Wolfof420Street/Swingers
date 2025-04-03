// src/ui/AdminDashboard.java
package ui;

import db.DatabaseConnection;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminDashboard extends JFrame {
    private User user;

    public AdminDashboard(User user) {
        this.user = user;
        setTitle("Admin Dashboard - " + user.getUsername());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Users tab
        JPanel usersPanel = new JPanel(new BorderLayout());
        JTextArea usersText = new JTextArea();
        usersText.setEditable(false);
        loadUsers(usersText);
        usersPanel.add(new JScrollPane(usersText), BorderLayout.CENTER);
        tabbedPane.addTab("All Users", usersPanel);

        // Courses tab (placeholder for now)
        JPanel coursesPanel = new JPanel();
        coursesPanel.add(new JLabel("Course management coming soon!"));
        tabbedPane.addTab("Courses", coursesPanel);

        add(tabbedPane);
        setVisible(true);
    }

    private void loadUsers(JTextArea textArea) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT user_id, username, role, email FROM Users";
            ResultSet rs = stmt.executeQuery(sql);
            StringBuilder sb = new StringBuilder();
            sb.append("ID\tUsername\tRole\tEmail\n");
            while (rs.next()) {
                sb.append(rs.getInt("user_id")).append("\t")
                  .append(rs.getString("username")).append("\t")
                  .append(rs.getString("role")).append("\t")
                  .append(rs.getString("email")).append("\n");
            }
            textArea.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            textArea.setText("Error loading users.");
        }
    }
}