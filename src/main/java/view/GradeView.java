package view;

import model.Grade;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GradeView extends JPanel {
    private JTable gradeTable;
    private JTextField gradeIdField, studentIdField, courseIdField, semesterField, midtermGradeField, finalGradeField, overallGradeField, statusField, notesField;
    private JButton addButton, updateButton, deleteButton, searchButton;
    private JTextField searchField;
    private DefaultTableModel tableModel;

    public GradeView() {
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"Grade ID", "Student ID", "Course ID", "Semester", "Midterm Grade", "Final Grade", "Overall Grade", "Status", "Notes"};
        tableModel = new DefaultTableModel(columns, 0);
        gradeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(10, 2));
        gradeIdField = new JTextField(20);
        gradeIdField.setEditable(false);
        studentIdField = new JTextField(20);
        courseIdField = new JTextField(20);
        semesterField = new JTextField(20);
        midtermGradeField = new JTextField(20);
        finalGradeField = new JTextField(20);
        overallGradeField = new JTextField(20);
        statusField = new JTextField(20);
        notesField = new JTextField(20);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        inputPanel.add(new JLabel("Grade ID:"));
        inputPanel.add(gradeIdField);
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(studentIdField);
        inputPanel.add(new JLabel("Course ID:"));
        inputPanel.add(courseIdField);
        inputPanel.add(new JLabel("Semester:"));
        inputPanel.add(semesterField);
        inputPanel.add(new JLabel("Midterm Grade:"));
        inputPanel.add(midtermGradeField);
        inputPanel.add(new JLabel("Final Grade:"));
        inputPanel.add(finalGradeField);
        inputPanel.add(new JLabel("Overall Grade:"));
        inputPanel.add(overallGradeField);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusField);
        inputPanel.add(new JLabel("Notes:"));
        inputPanel.add(notesField);
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
        gradeTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = gradeTable.getSelectedRow();
            if (selectedRow >= 0) {
                gradeIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                studentIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                courseIdField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                semesterField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                midtermGradeField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                finalGradeField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                overallGradeField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                statusField.setText(tableModel.getValueAt(selectedRow, 7).toString());
                notesField.setText(tableModel.getValueAt(selectedRow, 8).toString());
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

    public Grade getGradeFromFields() {
        return new Grade(
            Integer.parseInt(gradeIdField.getText().isEmpty() ? "0" : gradeIdField.getText()),
            studentIdField.getText(),
            courseIdField.getText(),
            semesterField.getText(),
            Double.parseDouble(midtermGradeField.getText().isEmpty() ? "0" : midtermGradeField.getText()),
            Double.parseDouble(finalGradeField.getText().isEmpty() ? "0" : finalGradeField.getText()),
            Double.parseDouble(overallGradeField.getText().isEmpty() ? "0" : overallGradeField.getText()),
            statusField.getText(),
            notesField.getText()
        );
    }

    public int getSelectedGradeId() {
        int selectedRow = gradeTable.getSelectedRow();
        return selectedRow >= 0 ? Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()) : -1;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void updateTable(List<Grade> grades) {
        tableModel.setRowCount(0);
        for (Grade grade : grades) {
            tableModel.addRow(new Object[]{
                grade.getGradeId(),
                grade.getStudentId(),
                grade.getCourseId(),
                grade.getSemester(),
                grade.getMidtermGrade(),
                grade.getFinalGrade(),
                grade.getOverallGrade(),
                grade.getStatus(),
                grade.getNotes()
            });
        }
    }

    public void clearFields() {
        gradeIdField.setText("");
        studentIdField.setText("");
        courseIdField.setText("");
        semesterField.setText("");
        midtermGradeField.setText("");
        finalGradeField.setText("");
        overallGradeField.setText("");
        statusField.setText("");
        notesField.setText("");
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