package controller;

import dao.AccountDAO;
import view.LoginView;
import view.MainView;
import model.Account;
import model.UserSession;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

public class LoginController {
    private LoginView loginView;
    private AccountDAO accountDAO;
    private MainView mainView;
    private UserSession userSession;

    public LoginController(LoginView loginView, AccountDAO accountDAO, MainView mainView) {
        this.loginView = loginView;
        this.accountDAO = accountDAO;
        this.mainView = mainView;
        this.userSession = UserSession.getInstance();

        loginView.setLoginButtonListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        try {
            Account account = accountDAO.getAccount(username);
            if (account != null && account.getPassword().equals(password)) {
                // Set current user session
                userSession.setCurrentUser(account);
                
                loginView.clearFields();
                loginView.setVisible(false);
                mainView.setVisible(true);

                // Configure UI based on role permissions
                AuthorizationService.configureUIForRole(mainView.getTabbedPane());
                
                // Update user info display
                mainView.updateUserInfo();
                
            } else {
                loginView.showMessage("Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        } catch (SQLException e) {
            loginView.showMessage("Lỗi kết nối cơ sở dữ liệu!");
        }
    }
}