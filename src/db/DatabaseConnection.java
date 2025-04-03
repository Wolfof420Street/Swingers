package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String USER = "root";
    private static final String PASSWORD = "42069";

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            // Explicitly load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Attempt to establish the database connection
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful.");
            return connection;
        } catch (ClassNotFoundException ex) {
            // Handle driver not found error
            System.err.println("MySQL JDBC Driver not found: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, 
                "MySQL JDBC Driver not found. Please add the driver to your classpath.");
            return null;
        } catch (SQLException ex) {
            // Provide more detailed error information
            System.err.println("Database connection error:");
            System.err.println("Error Code: " + ex.getErrorCode());
            System.err.println("SQL State: " + ex.getSQLState());
            System.err.println("Error Message: " + ex.getMessage());
            
            // Show a more informative error dialog
            JOptionPane.showMessageDialog(null, 
                "Failed to connect to database.\n" +
                "Error: " + ex.getMessage() + "\n" +
                "Please check your connection details.");
            return null;
        }
    }

    // Utility method to safely close the connection
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