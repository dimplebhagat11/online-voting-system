package com.example.voting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Creates connections to the local MySQL database. */
public class DBConnection {

    // Change these values to match the MySQL installation.
    private static final String URL =
            "jdbc:mysql://localhost:3307/online_voting?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Placeholder Password"; // This must be changed to connect with the actual db!

    private DBConnection() {
        // Utility class. So, do not create an object of this class.
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver was not found. Check the Maven dependency.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
