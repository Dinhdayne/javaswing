package model;

public class Account {
    private int accountId;
    private String username;
    private String password;
    private String role;
    private String userId;
    private String createdAt;
    private String lastLogin;

    // Constructor
    public Account(int accountId, String username, String password, String role, String userId, String createdAt, String lastLogin) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.userId = userId;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    // Getters and Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getLastLogin() { return lastLogin; }
    public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }
}