package model;

public class UserSession {
    private static UserSession instance;
    private Account currentAccount;
    private String currentUserId;
    
    private UserSession() {}
    
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void setCurrentUser(Account account) {
        this.currentAccount = account;
        this.currentUserId = account.getUserId();
    }
    
    public Account getCurrentAccount() {
        return currentAccount;
    }
    
    public String getCurrentUserId() {
        return currentUserId;
    }
    
    public String getCurrentRole() {
        return currentAccount != null ? currentAccount.getRole() : null;
    }
    
    public String getCurrentUsername() {
        return currentAccount != null ? currentAccount.getUsername() : null;
    }
    
    public boolean isLoggedIn() {
        return currentAccount != null;
    }
    
    public void logout() {
        currentAccount = null;
        currentUserId = null;
    }
}