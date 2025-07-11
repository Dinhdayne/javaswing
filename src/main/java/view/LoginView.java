package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginView() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"Admin", "Teacher", "Student"});
        panel.add(roleComboBox);
        loginButton = new JButton("Login");
        panel.add(new JLabel(""));
        panel.add(loginButton);

        statusLabel = new JLabel("");
        add(panel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
    }

    public void setLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getRole() {
        return (String) roleComboBox.getSelectedItem();
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        statusLabel.setText("");
    }

    public void showMessage(String invalid_username_or_password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}