package view;

import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentView extends JPanel {
    private JTable studentTable;
    private JTextField studentIdField, nameField, dobField, genderField, emailField, phoneField, addressField, classIdField, enrollmentDateField;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    public StudentView() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"Student ID", "Name", "Date of Birth", "Gender", "Email", "Phone", "Address", "Class ID", "Enrollment Date"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(10, 2));
        studentIdField = new JTextField(20);
        nameField = new JTextField(20);
        dobField = new JTextField(20);
        genderField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(20);
        classIdField = new JTextField(20);
        enrollmentDateField = new JTextField(20);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(studentIdField);
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
        inputPanel.add(new JLabel("Class ID:"));
        inputPanel.add(classIdField);
        inputPanel.add(new JLabel("Enrollment Date:"));
        inputPanel.add(enrollmentDateField);
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
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow >= 0) {
                studentIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                dobField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                genderField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                addressField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                classIdField.setText(tableModel.getValueAt(selectedRow, 7).toString());
                enrollmentDateField.setText(tableModel.getValueAt(selectedRow, 8).toString());
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

    public Student getStudentFromFields() {
        return new Student(
            studentIdField.getText(),
            nameField.getText(),
            dobField.getText(),
            genderField.getText(),
            emailField.getText(),
            phoneField.getText(),
            addressField.getText(),
            classIdField.getText(),
            enrollmentDateField.getText()
        );
    }

    public String getSelectedStudentId() {
        int selectedRow = studentTable.getSelectedRow();
        return selectedRow >= 0 ? tableModel.getValueAt(selectedRow, 0).toString() : null;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void updateTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getStudentId(),
                student.getName(),
                student.getDateOfBirth(),
                student.getGender(),
                student.getEmail(),
                student.getPhone(),
                student.getAddress(),
                student.getClassId(),
                student.getEnrollmentDate()
            });
        }
    }

    public void clearFields() {
        studentIdField.setText("");
        nameField.setText("");
        dobField.setText("");
        genderField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        classIdField.setText("");
        enrollmentDateField.setText("");
        searchField.setText("");
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    // Methods to enable/disable buttons based on user permissions
    public void setAddButtonEnabled(boolean enabled) {
        addButton.setEnabled(enabled);
    }
    
    public void setUpdateButtonEnabled(boolean enabled) {
        updateButton.setEnabled(enabled);
    }
    
    public void setDeleteButtonEnabled(boolean enabled) {
        deleteButton.setEnabled(enabled);
    }
    
    public void setSearchButtonEnabled(boolean enabled) {
        searchButton.setEnabled(enabled);
    }
}