package view;

import model.Class;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClassView extends JPanel {
    private JTable classTable;
    private JTextField classIdField, classNameField, deptIdField, academicYearField, teacherIdField;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    public ClassView() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"Class ID", "Class Name", "Department ID", "Academic Year", "Teacher ID"};
        tableModel = new DefaultTableModel(columns, 0);
        classTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(classTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        classIdField = new JTextField(20);
        classNameField = new JTextField(20);
        deptIdField = new JTextField(20);
        academicYearField = new JTextField(20);
        teacherIdField = new JTextField(20);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        inputPanel.add(new JLabel("Class ID:"));
        inputPanel.add(classIdField);
        inputPanel.add(new JLabel("Class Name:"));
        inputPanel.add(classNameField);
        inputPanel.add(new JLabel("Department ID:"));
        inputPanel.add(deptIdField);
        inputPanel.add(new JLabel("Academic Year:"));
        inputPanel.add(academicYearField);
        inputPanel.add(new JLabel("Teacher ID:"));
        inputPanel.add(teacherIdField);
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
        classTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = classTable.getSelectedRow();
            if (selectedRow >= 0) {
                classIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                classNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                deptIdField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                academicYearField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                teacherIdField.setText(tableModel.getValueAt(selectedRow, 4).toString());
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

    public Class getClassFromFields() {
        return new Class(
            classIdField.getText(),
            classNameField.getText(),
            deptIdField.getText(),
            academicYearField.getText(),
            teacherIdField.getText()
        );
    }

    public String getSelectedClassId() {
        int selectedRow = classTable.getSelectedRow();
        return selectedRow >= 0 ? tableModel.getValueAt(selectedRow, 0).toString() : null;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void updateTable(List<Class> classes) {
        tableModel.setRowCount(0);
        for (Class cls : classes) {
            tableModel.addRow(new Object[]{
                cls.getClassId(),
                cls.getClassName(),
                cls.getDepartmentId(),
                cls.getAcademicYear(),
                cls.getTeacherId()
            });
        }
    }

    public void clearFields() {
        classIdField.setText("");
        classNameField.setText("");
        deptIdField.setText("");
        academicYearField.setText("");
        teacherIdField.setText("");
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