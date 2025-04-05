package ui;

import db.DatabaseConnection;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AdminDashboard extends JFrame {
    private User user;
    private JTable usersTable;
    private JTable coursesTable;
    private JTextField courseNameField;
    private JTextField creditsField;
    private JComboBox<String> instructorCombo;

    public AdminDashboard(User user) {
        this.user = user;

        // Frame setup
        setTitle("Admin Dashboard");
        setSize(900, 700);
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
                    new Color(192, 57, 43), 0, getHeight(),
                    new Color(120, 36, 27));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabbedPane = createTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

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
        
        JLabel welcomeLabel = new JLabel("Admin " + user.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(255, 235, 230));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 0));
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setForeground(new Color(255, 235, 230));
        tabbedPane.setBackground(new Color(149, 44, 33));

        // Users tab
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.setOpaque(false);
        usersTable = createUsersTable();
        usersPanel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
        tabbedPane.addTab("All Users", usersPanel);

        // Courses tab
        JPanel coursesPanel = new JPanel(new BorderLayout());
        coursesPanel.setOpaque(false);
        
        // Courses table
        coursesTable = createCoursesTable();
        coursesPanel.add(new JScrollPane(coursesTable), BorderLayout.CENTER);
        
        // Add course panel
        JPanel addCoursePanel = createAddCoursePanel();
        coursesPanel.add(addCoursePanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Courses", coursesPanel);

        return tabbedPane;
    }

    private JTable createUsersTable() {
        String[] columnNames = {"ID", "Username", "Role", "Email"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        styleTable(table);
        loadUsers(model);
        return table;
    }

    private JTable createCoursesTable() {
        String[] columnNames = {"ID", "Name", "Credits", "Instructor"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        styleTable(table);
        loadCourses(model);
        return table;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setForeground(new Color(255, 235, 230));
        table.setBackground(new Color(149, 44, 33));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(192, 57, 43));
        table.getTableHeader().setForeground(new Color(255, 235, 230));
        table.setGridColor(new Color(231, 76, 60));
        table.setSelectionBackground(new Color(170, 50, 38));
    }

    private JPanel createAddCoursePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Labels and fields
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Course Name:");
        styleLabel(nameLabel);
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        courseNameField = new JTextField(15);
        styleTextField(courseNameField);
        panel.add(courseNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel creditsLabel = new JLabel("Credits:");
        styleLabel(creditsLabel);
        panel.add(creditsLabel, gbc);

        gbc.gridx = 1;
        creditsField = new JTextField(5);
        styleTextField(creditsField);
        panel.add(creditsField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel instructorLabel = new JLabel("Instructor:");
        styleLabel(instructorLabel);
        panel.add(instructorLabel, gbc);

        gbc.gridx = 1;
        instructorCombo = new JComboBox<>();
        styleComboBox(instructorCombo);
        loadInstructors();
        panel.add(instructorCombo, gbc);

        gbc.gridx = 2; gbc.gridy = 2;
        JButton addButton = createStyledButton("Add Course");
        addButton.addActionListener(e -> addCourse());
        panel.add(addButton, gbc);

        return panel;
    }

    private void styleLabel(JLabel label) {
        label.setForeground(new Color(255, 235, 230));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(255, 235, 230, 200));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(new Color(255, 235, 230, 200));
        combo.setForeground(Color.BLACK);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0,
                    new Color(231, 76, 60), 0, getHeight(),
                    new Color(192, 57, 43));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        button.setForeground(new Color(255, 235, 230));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }

    private JLabel createCloseButton() {
        JLabel closeLabel = new JLabel("×");
        closeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        closeLabel.setForeground(new Color(255, 235, 230));
        closeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                closeLabel.setForeground(new Color(255, 102, 102));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                closeLabel.setForeground(new Color(255, 235, 230));
            }
        });
        return closeLabel;
    }

    private void loadUsers(DefaultTableModel model) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT user_id, username, role, email FROM Users";
            ResultSet rs = stmt.executeQuery(sql);
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    rs.getString("email")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.setRowCount(0);
            model.addRow(new Object[]{"Error", "Loading failed", "", ""});
        }
    }

    private void loadCourses(DefaultTableModel model) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT c.course_id, c.course_name, c.credits, u.username " +
                        "FROM Courses c LEFT JOIN Users u ON c.instructor_id = u.user_id";
            ResultSet rs = stmt.executeQuery(sql);
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getInt("credits"),
                    rs.getString("username") != null ? rs.getString("username") : "Unassigned"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            model.setRowCount(0);
            model.addRow(new Object[]{"Error", "Loading failed", "", ""});
        }
    }

    private void loadInstructors() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT user_id, username FROM Users WHERE role = 'Instructor'";
            ResultSet rs = stmt.executeQuery(sql);
            instructorCombo.removeAllItems();
            instructorCombo.addItem("Unassigned");
            while (rs.next()) {
                instructorCombo.addItem(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            instructorCombo.removeAllItems();
            instructorCombo.addItem("Error loading instructors");
        }
    }

    private void addCourse() {
        String courseName = courseNameField.getText().trim();
        String creditsText = creditsField.getText().trim();
        String instructor = (String) instructorCombo.getSelectedItem();

        if (courseName.isEmpty() || creditsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsText);
            if (credits <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Credits must be a positive number!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql;
            PreparedStatement stmt;
            if ("Unassigned".equals(instructor)) {
                sql = "INSERT INTO Courses (course_name, credits) VALUES (?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, courseName);
                stmt.setInt(2, credits);
            } else {
                sql = "INSERT INTO Courses (course_name, credits, instructor_id) " +
                      "VALUES (?, ?, (SELECT user_id FROM Users WHERE username = ?))";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, courseName);
                stmt.setInt(2, credits);
                stmt.setString(3, instructor);
            }

            stmt.executeUpdate();
            loadCourses((DefaultTableModel) coursesTable.getModel());
            courseNameField.setText("");
            creditsField.setText("");
            instructorCombo.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Course added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding course: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User(1, "testAdmin", "password", "Admin", "admin@example.com");
            new AdminDashboard(testUser);
        });
    }
}