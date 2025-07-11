package view;

import model.Teacher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TeacherView extends JPanel {
    private JTable teacherTable;
    private JTextField teacherIdField, nameField, dobField, genderField, emailField, phoneField, addressField, deptIdField, hireDateField;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    public TeacherView() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"Teacher ID", "Name", "Date of Birth", "Gender", "Email", "Phone", "Address", "Department ID", "Hire Date"};
        tableModel = new DefaultTableModel(columns, 0);
        teacherTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(10, 2));
        teacherIdField = new JTextField(20);
        nameField = new JTextField(20);
        dobField = new JTextField(20);
        genderField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(20);
        deptIdField = new JTextField(20);
        hireDateField = new JTextField(20);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        inputPanel.add(new JLabel("Teacher ID:"));
        inputPanel.add(teacherIdField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Date of Birth:"));
        inputPanel.add(dobField);
        inputPanel.add(new JLabel("Gender:"));
        inputPanel.add(genderField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);
        inputPanel.add(new JLabel("Department ID:"));
        inputPanel.add(deptIdField);
        inputPanel.add(new JLabel("Hire Date:"));
        inputPanel.add(hireDateField);
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
        teacherTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = teacherTable.getSelectedRow();
            if (selectedRow >= 0) {
                teacherIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                dobField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                genderField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                addressField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                deptIdField.setText(tableModel.getValueAt(selectedRow, 7).toString());
                hireDateField.setText(tableModel.getValueAt(selectedRow, 8).toString());
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

    public Teacher getTeacherFromFields() {
        return new Teacher(
            teacherIdField.getText(),
            nameField.getText(),
            dobField.getText(),
            genderField.getText(),
            emailField.getText(),
            phoneField.getText(),
            addressField.getText(),
            deptIdField.getText(),
            hireDateField.getText()
        );
    }

    public String getSelectedTeacherId() {
        int selectedRow = teacherTable.getSelectedRow();
        return selectedRow >= 0 ? tableModel.getValueAt(selectedRow, 0).toString() : null;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void updateTable(List<Teacher> teachers) {
        tableModel.setRowCount(0);
        for (Teacher teacher : teachers) {
            tableModel.addRow(new Object[]{
                teacher.getTeacherId(),
                teacher.getName(),
                teacher.getDateOfBirth(),
                teacher.getGender(),
                teacher.getEmail(),
                teacher.getPhone(),
                teacher.getAddress(),
                teacher.getDepartmentId(),
                teacher.getHireDate()
            });
        }
    }

    public void clearFields() {
        teacherIdField.setText("");
        nameField.setText("");
        dobField.setText("");
        genderField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        deptIdField.setText("");
        hireDateField.setText("");
        searchField.setText("");
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}