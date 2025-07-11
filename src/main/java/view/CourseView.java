package view;

import model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CourseView extends JPanel {
    private JTable courseTable;
    private JTextField courseIdField, courseNameField, creditsField, deptIdField, teacherIdField, semesterField;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    public CourseView() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"Course ID", "Course Name", "Credits", "Department ID", "Teacher ID", "Semester"};
        tableModel = new DefaultTableModel(columns, 0);
        courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        courseIdField = new JTextField(20);
        courseNameField = new JTextField(20);
        creditsField = new JTextField(20);
        deptIdField = new JTextField(20);
        teacherIdField = new JTextField(20);
        semesterField = new JTextField(20);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        inputPanel.add(new JLabel("Course ID:"));
        inputPanel.add(courseIdField);
        inputPanel.add(new JLabel("Course Name:"));
        inputPanel.add(courseNameField);
        inputPanel.add(new JLabel("Credits:"));
        inputPanel.add(creditsField);
        inputPanel.add(new JLabel("Department ID:"));
        inputPanel.add(deptIdField);
        inputPanel.add(new JLabel("Teacher ID:"));
        inputPanel.add(teacherIdField);
        inputPanel.add(new JLabel("Semester:"));
        inputPanel.add(semesterField);
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
        courseTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                courseIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                courseNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                creditsField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                deptIdField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                teacherIdField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                semesterField.setText(tableModel.getValueAt(selectedRow, 5).toString());
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

    public Course getCourseFromFields() {
        return new Course(
            courseIdField.getText(),
            courseNameField.getText(),
            Integer.parseInt(creditsField.getText()),
            deptIdField.getText(),
            teacherIdField.getText(),
            semesterField.getText()
        );
    }

    public String getSelectedCourseId() {
        int selectedRow = courseTable.getSelectedRow();
        return selectedRow >= 0 ? tableModel.getValueAt(selectedRow, 0).toString() : null;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void updateTable(List<Course> courses) {
        tableModel.setRowCount(0);
        for (Course course : courses) {
            tableModel.addRow(new Object[]{
                course.getCourseId(),
                course.getCourseName(),
                course.getCredits(),
                course.getDepartmentId(),
                course.getTeacherId(),
                course.getSemester()
            });
        }
    }

    public void clearFields() {
        courseIdField.setText("");
        courseNameField.setText("");
        creditsField.setText("");
        deptIdField.setText("");
        teacherIdField.setText("");
        semesterField.setText("");
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