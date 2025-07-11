package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView extends JFrame {
    private JTabbedPane tabbedPane;

    public MainView() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Add Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to add tabs
    public void addTab(String title, JComponent component) {
        tabbedPane.addTab(title, component);
    }

    // Method to set logout button listener
    public void setLogoutButtonListener(ActionListener listener) {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                for (Component button : ((JPanel) comp).getComponents()) {
                    if (button instanceof JButton && ((JButton) button).getText().equals("Logout")) {
                        ((JButton) button).addActionListener(listener);
                        return;
                    }
                }
            }
        }
    }

    // Method to get tabbed pane (used in StudentManagementApp.java)
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}