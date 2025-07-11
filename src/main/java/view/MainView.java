package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import model.UserSession;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;
    private JLabel userInfoLabel;

    public MainView() {
        setTitle("Hệ thống Quản lý Sinh viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Create bottom panel with user info and logout button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // User info panel
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userInfoLabel = new JLabel();
        userInfoPanel.add(new JLabel("Người dùng: "));
        userInfoPanel.add(userInfoLabel);
        
        // Logout button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Đăng xuất");
        buttonPanel.add(logoutButton);
        
        bottomPanel.add(userInfoPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method to add tabs
    public void addTab(String title, JComponent component) {
        tabbedPane.addTab(title, component);
    }

    // Method to set logout button listener
    public void setLogoutButtonListener(ActionListener listener) {
        Component bottomPanel = getContentPane().getComponent(1); // Bottom panel
        if (bottomPanel instanceof JPanel) {
            JPanel rightPanel = (JPanel) ((JPanel) bottomPanel).getComponent(1); // Right panel with logout button
            if (rightPanel != null && rightPanel.getComponentCount() > 0) {
                JButton logoutButton = (JButton) rightPanel.getComponent(0);
                logoutButton.addActionListener(listener);
            }
        }
    }
    
    // Method to update user info display
    public void updateUserInfo() {
        UserSession session = UserSession.getInstance();
        if (session.isLoggedIn()) {
            String username = session.getCurrentUsername();
            String role = session.getCurrentRole();
            userInfoLabel.setText(username + " (" + role + ")");
        } else {
            userInfoLabel.setText("");
        }
    }

    // Method to get tabbed pane (used in StudentManagementApp.java)
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}