package cn.restraunt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.restraunt.model.Admin;

public class AdminDao {
    private Connection connection;

    // Constructor to initialize connection
    public AdminDao(Connection connection) {
        this.connection = connection;
    }

    // Method to handle admin login
    public Admin adminLogin(String email, String password) throws SQLException {
        String query = "SELECT * FROM admin WHERE email = ? AND password = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Use 'username' instead of 'name'
                return new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("email"), rs.getString("password"));
            }
        }
        return null;
    }
}