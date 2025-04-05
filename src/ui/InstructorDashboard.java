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

public class InstructorDashboard extends JFrame {
    private User user;
    private JTable coursesTable;
    private JLabel welcomeLabel;

    public InstructorDashboard(User user) {
        this.user = user;

        // Frame setup
        setTitle("Instructor Dashboard");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0,
                    new Color(75, 54, 99), 0, getHeight(),
                    new Color(44, 32, 59));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        mainPanel.setLayout(new BorderLayout(25, 25));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Sidebar panel
        JPanel sidebarPanel = createSidebarPanel();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Close button
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

        welcomeLabel = new JLabel("Professor " + user.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        welcomeLabel.setForeground(new Color(240, 223, 255));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Table setup
        String[] columnNames = {"ID", "Course Name", "Credits"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        coursesTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Style table
        coursesTable.setRowHeight(40);
        coursesTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        coursesTable.setForeground(new Color(225, 210, 240));
        coursesTable.setBackground(new Color(60, 43, 82));
        coursesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        coursesTable.getTableHeader().setBackground(new Color(75, 54, 99));
        coursesTable.getTableHeader().setForeground(new Color(240, 223, 255));
        coursesTable.setGridColor(new Color(115, 93, 141));
        coursesTable.setSelectionBackground(new Color(95, 71, 120));

        loadCourses(model);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(60, 43, 82));

        // Title
        JLabel titleLabel = new JLabel("My Courses");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(240, 223, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setOpaque(false);
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        
        JLabel profileLabel = new JLabel("Profile");
        styleSidebarButton(profileLabel);
        profileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Profile feature coming soon!");
            }
        });

        JLabel statsLabel = new JLabel("Statistics");
        styleSidebarButton(statsLabel);
        statsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Stats feature coming soon!");
            }
        });

        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(profileLabel);
        sidebarPanel.add(Box.createVerticalStrut(15));
        sidebarPanel.add(statsLabel);
        sidebarPanel.add(Box.createVerticalGlue());

        return sidebarPanel;
    }

    private void styleSidebarButton(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(new Color(240, 223, 255));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setMaximumSize(new Dimension(180, 40));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(new Color(173, 146, 209));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(new Color(240, 223, 255));
            }
        });
    }

    private JLabel createCloseButton() {
        JLabel closeLabel = new JLabel("×");
        closeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        closeLabel.setForeground(new Color(240, 223, 255));
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
                closeLabel.setForeground(new Color(240, 223, 255));
            }
        });
        return closeLabel;
    }

    private void loadCourses(DefaultTableModel model) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT course_id, course_name, credits FROM Courses WHERE instructor_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, user.getUserId());
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0); // Clear existing rows
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getInt("credits")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.setRowCount(0);
            model.addRow(new Object[]{"Error", "Loading failed", ""});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User(1, "testInstructor", "password", "Instructor", "test@example.com");
            new InstructorDashboard(testUser);
        });
    }
}