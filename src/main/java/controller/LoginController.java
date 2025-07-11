package controller;

import dao.AccountDAO;
import view.LoginView;
import view.MainView;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import model.Account;

public class LoginController {
    private LoginView loginView;
    private AccountDAO accountDAO;
    private MainView mainView;

    public LoginController(LoginView loginView, AccountDAO accountDAO, MainView mainView) {
        this.loginView = loginView;
        this.accountDAO = accountDAO;
        this.mainView = mainView;

        loginView.setLoginButtonListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        try {
            Account account = accountDAO.getAccount(username);
            if (account != null && account.getPassword().equals(password)) {
                loginView.clearFields();
                loginView.setVisible(false);
                mainView.setVisible(true);

                // Kiểm tra role và giới hạn quyền
                if ("Student".equals(account.getRole())) {
                    restrictStudentAccess(account.getUsername());
                } else if ("Teacher".equals(account.getRole())) {
                    // Logic cho Teacher (có thể mở rộng sau)
                } else if ("Admin".equals(account.getRole())) {
                    // Admin có tất cả quyền
                }
            } else {
                loginView.showMessage("Invalid username or password!");
            }
        } catch (SQLException e) {
        }
        }

    private void restrictStudentAccess(String username) {
        
        mainView.getTabbedPane().setEnabledAt(0, false); // Departments
        mainView.getTabbedPane().setEnabledAt(1, false); // Teachers
        mainView.getTabbedPane().setEnabledAt(2, true);  // Students (chỉ xem)
        mainView.getTabbedPane().setEnabledAt(3, false); // Classes
        mainView.getTabbedPane().setEnabledAt(4, false); // Courses
    }

    
}