package ui;

import db.DatabaseConnection;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDashboard extends JFrame {
    private User user;
    private JTable enrollmentsTable;
    private JLabel welcomeLabel;

    public StudentDashboard(User user) {
        this.user = user;
        
        // Frame setup
        setTitle("Student Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Main panel with gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0,
                    new Color(46, 64, 83), 0, getHeight(),
                    new Color(28, 40, 51));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add close button
        JLabel closeLabel = createCloseButton();
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.setOpaque(false);
        closePanel.add(closeLabel);
        mainPanel.add(closePanel, BorderLayout.NORTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        welcomeLabel = new JLabel("Welcome, " + user.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 0));
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Table setup
        String[] columnNames = {"Course Name", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        enrollmentsTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Style table
        enrollmentsTable.setRowHeight(35);
        enrollmentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        enrollmentsTable.setForeground(Color.WHITE);
        enrollmentsTable.setBackground(new Color(44, 62, 80));
        enrollmentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        enrollmentsTable.getTableHeader().setBackground(new Color(52, 73, 94));
        enrollmentsTable.getTableHeader().setForeground(Color.WHITE);
        enrollmentsTable.setGridColor(new Color(98, 114, 164));
        
        loadEnrollments(model);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(enrollmentsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(44, 62, 80));
        
        // Title
        JLabel titleLabel = new JLabel("My Enrollments");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        return contentPanel;
    }

    private JLabel createCloseButton() {
        JLabel closeLabel = new JLabel("×");
        closeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        closeLabel.setForeground(Color.WHITE);
        closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                closeLabel.setForeground(new Color(231, 76, 60));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                closeLabel.setForeground(Color.WHITE);
            }
        });
        return closeLabel;
    }

    private void loadEnrollments(DefaultTableModel model) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT c.course_name, e.grade FROM Enrollments e " +
                        "JOIN Courses c ON e.course_id = c.course_id " +
                        "WHERE e.student_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getUserId());
            ResultSet rs = stmt.executeQuery();
            
            model.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                Object[] row = {
                    rs.getString("course_name"),
                    rs.getObject("grade") != null ? String.format("%.1f", rs.getDouble("grade")) : "N/A"
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.setRowCount(0);
            model.addRow(new Object[]{"Error loading enrollments", ""});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User(1, "testStudent", "password", "Student", "test@example.com");
            new StudentDashboard(testUser);
        });
    }
}