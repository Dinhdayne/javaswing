package view;

import model.Department;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DepartmentView extends JPanel {
    private JTable departmentTable;
    private JTextField deptIdField, deptNameField, headTeacherIdField, phoneField, emailField;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    public DepartmentView() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"Department ID", "Name", "Head Teacher ID", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        departmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(departmentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        deptIdField = new JTextField(20);
        deptNameField = new JTextField(20);
        headTeacherIdField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        inputPanel.add(new JLabel("Department ID:"));
        inputPanel.add(deptIdField);
        inputPanel.add(new JLabel("Department Name:"));
        inputPanel.add(deptNameField);
        inputPanel.add(new JLabel("Head Teacher ID:"));
        inputPanel.add(headTeacherIdField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Search:"));
        inputPanel.add(searchField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Table row selection
        departmentTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = departmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                deptIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                deptNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                headTeacherIdField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void setUpdateButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void setDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void setSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public Department getDepartmentFromFields() {
        return new Department(
            deptIdField.getText(),
            deptNameField.getText(),
            headTeacherIdField.getText(),
            phoneField.getText(),
            emailField.getText()
        );
    }

    public String getSelectedDepartmentId() {
        int selectedRow = departmentTable.getSelectedRow();
        return selectedRow >= 0 ? tableModel.getValueAt(selectedRow, 0).toString() : null;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void updateTable(List<Department> departments) {
        tableModel.setRowCount(0);
        for (Department dept : departments) {
            tableModel.addRow(new Object[]{
                dept.getDepartmentId(),
                dept.getDepartmentName(),
                dept.getHeadTeacherId(),
                dept.getPhone(),
                dept.getEmail()
            });
        }
    }

    public void clearFields() {
        deptIdField.setText("");
        deptNameField.setText("");
        headTeacherIdField.setText("");
        phoneField.setText("");
        emailField.setText("");
        searchField.setText("");
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}