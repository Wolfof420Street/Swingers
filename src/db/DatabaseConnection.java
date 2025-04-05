package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String USER = "root";
    private static final String PASSWORD = "42069";

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful.");
            return connection;
        } catch (ClassNotFoundException ex) {
            System.err.println("MySQL JDBC Driver not found: " + ex.getMessage());
            ex.printStackTrace(); // Add stack trace for debugging
            JOptionPane.showMessageDialog(null, 
                "MySQL JDBC Driver not found. Please add the driver to your classpath.\n" +
                "Error: " + ex.getMessage());
            return null;
        } catch (SQLException ex) {
            System.err.println("Database connection error:");
            System.err.println("Error Code: " + ex.getErrorCode());
            System.err.println("SQL State: " + ex.getSQLState());
            System.err.println("Error Message: " + ex.getMessage());
            ex.printStackTrace(); // Add stack trace for debugging
            JOptionPane.showMessageDialog(null, 
                "Failed to connect to database.\n" +
                "Error: " + ex.getMessage() + "\n" +
                "Please check your connection details.");
            return null;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException ex) {
                System.err.println("Error closing database connection: " + ex.getMessage());
            }
        }
    }
}