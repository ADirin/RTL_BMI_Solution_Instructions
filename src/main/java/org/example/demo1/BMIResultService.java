package org.example.demo1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BMIResultService {

    // Environment variables with Docker-friendly defaults
    private static final String DB_HOST =
            System.getenv().getOrDefault("DB_HOST", "db");

    private static final String DB_PORT =
            System.getenv().getOrDefault("DB_PORT", "3306");

    private static final String DB_NAME =
            System.getenv().getOrDefault("DB_NAME", "bmi_localization");

    private static final String DB_USER =
            System.getenv().getOrDefault("DB_USER", "root");

    private static final String DB_PASSWORD =
            System.getenv().getOrDefault("DB_PASSWORD", "Test12");

    private static final String DB_URL =
            "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    static {
        try {
            // Ensure MySQL driver loads properly
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static void saveResult(double weight, double height, double bmi, String language) {

        String query = "INSERT INTO bmi_results (weight, height, bmi, language) VALUES (?, ?, ?, ?)";

        System.out.println("🔎 Attempting DB connection to: " + DB_URL);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, weight);
            stmt.setDouble(2, height);
            stmt.setDouble(3, bmi);
            stmt.setString(4, language);

            stmt.executeUpdate();

            System.out.println("✅ BMI result saved successfully.");

        } catch (SQLException e) {
            System.err.println("❌ Database connection failed.");
            System.err.println("URL: " + DB_URL);
            System.err.println("User: " + DB_USER);
            e.printStackTrace();
        }
    }
}