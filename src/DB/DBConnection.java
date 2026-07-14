package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/ums_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // <-- change this to your real MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Quick standalone test - run this file directly to check the connection
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connected to database successfully!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
