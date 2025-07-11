package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PersonalInfoView extends JPanel {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JTextField dateOfBirthField;
    private JComboBox<String> genderCombo;
    private JLabel userIdLabel;
    private JLabel roleLabel;
    private JLabel usernameLabel;
    
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    
    private JButton updateInfoButton;
    private JButton changePasswordButton;
    private JButton cancelButton;
    
    public PersonalInfoView() {
        setLayout(new BorderLayout());
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Main panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Personal Information Tab
        JPanel personalInfoPanel = createPersonalInfoPanel();
        tabbedPane.addTab("Thông tin cá nhân", personalInfoPanel);
        
        // Password Change Tab
        JPanel passwordPanel = createPasswordPanel();
        tabbedPane.addTab("Đổi mật khẩu", passwordPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Account Information (Read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        usernameLabel = new JLabel();
        panel.add(usernameLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1;
        roleLabel = new JLabel();
        panel.add(roleLabel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Mã người dùng:"), gbc);
        gbc.gridx = 1;
        userIdLabel = new JLabel();
        panel.add(userIdLabel, gbc);
        
        // Personal Information (Editable)
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        panel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Ngày sinh:"), gbc);
        gbc.gridx = 1;
        dateOfBirthField = new JTextField(20);
        panel.add(dateOfBirthField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        genderCombo = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        panel.add(genderCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1;
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(addressArea);
        panel.add(scrollPane, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        updateInfoButton = new JButton("Cập nhật thông tin");
        cancelButton = new JButton("Hủy");
        buttonPanel.add(updateInfoButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Mật khẩu hiện tại:"), gbc);
        gbc.gridx = 1;
        currentPasswordField = new JPasswordField(20);
        panel.add(currentPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Mật khẩu mới:"), gbc);
        gbc.gridx = 1;
        newPasswordField = new JPasswordField(20);
        panel.add(newPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        panel.add(confirmPasswordField, gbc);
        
        // Buttons
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        changePasswordButton = new JButton("Đổi mật khẩu");
        JButton cancelPasswordButton = new JButton("Hủy");
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(cancelPasswordButton);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    // Getters for form fields
    public String getName() { return nameField.getText(); }
    public String getEmail() { return emailField.getText(); }
    public String getPhone() { return phoneField.getText(); }
    public String getAddress() { return addressArea.getText(); }
    public String getDateOfBirth() { return dateOfBirthField.getText(); }
    public String getGender() { return (String) genderCombo.getSelectedItem(); }
    public String getCurrentPassword() { return new String(currentPasswordField.getPassword()); }
    public String getNewPassword() { return new String(newPasswordField.getPassword()); }
    public String getConfirmPassword() { return new String(confirmPasswordField.getPassword()); }
    
    // Setters for form fields
    public void setName(String name) { nameField.setText(name); }
    public void setEmail(String email) { emailField.setText(email); }
    public void setPhone(String phone) { phoneField.setText(phone); }
    public void setAddress(String address) { addressArea.setText(address); }
    public void setDateOfBirth(String dateOfBirth) { dateOfBirthField.setText(dateOfBirth); }
    public void setGender(String gender) { genderCombo.setSelectedItem(gender); }
    public void setUsername(String username) { usernameLabel.setText(username); }
    public void setRole(String role) { roleLabel.setText(role); }
    public void setUserId(String userId) { userIdLabel.setText(userId); }
    
    // Clear password fields
    public void clearPasswordFields() {
        currentPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
    }
    
    // Button listeners
    public void setUpdateInfoButtonListener(ActionListener listener) {
        updateInfoButton.addActionListener(listener);
    }
    
    public void setChangePasswordButtonListener(ActionListener listener) {
        changePasswordButton.addActionListener(listener);
    }
    
    public void setCancelButtonListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
    
    // Show messages
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}