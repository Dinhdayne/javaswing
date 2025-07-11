package dao;

import model.DatabaseConnection;

import java.sql.*;
import model.Account;

public class AccountDAO {
    public boolean authenticate(String username, String password, String role) throws SQLException {
        String query = "SELECT * FROM Accounts WHERE username = ? AND password = ? AND role = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In production, use hashed password comparison
            pstmt.setString(3, role);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    public Account getAccount(String username) throws SQLException {
    String query = "SELECT * FROM accounts WHERE username = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return new Account(
                rs.getInt("account_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("user_id"),
                rs.getString("created_at"),
                rs.getString("last_login")
            );
        }
        return null; // Trả về null nếu không tìm thấy tài khoản
    }
}
    
}