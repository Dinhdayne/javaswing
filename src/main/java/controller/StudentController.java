package controller;

import dao.StudentDAO;
import model.Student;
import view.StudentView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class StudentController {
    private StudentView view;
    private StudentDAO model;

    public StudentController(StudentView view, StudentDAO model) {
        this.view = view;
        this.model = model;

        loadStudents();
        view.setAddButtonListener(e -> addStudent());
        view.setUpdateButtonListener(e -> updateStudent());
        view.setDeleteButtonListener(e -> deleteStudent());
        view.setSearchButtonListener(e -> searchStudent());
    }

    private void loadStudents() {
        try {
            List<Student> students = model.getAllStudents();
            view.updateTable(students);
        } catch (SQLException e) {
            view.showMessage("Error loading students: " + e.getMessage());
        }
    }

    private void addStudent() {
        if (!validateFields()) return;
        try {
            Student student = view.getStudentFromFields();
            model.addStudent(student);
            loadStudents();
            view.clearFields();
            view.showMessage("Student added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding student: " + e.getMessage());
        }
    }

    private void updateStudent() {
        if (!validateFields()) return;
        try {
            Student student = view.getStudentFromFields();
            model.updateStudent(student);
            loadStudents();
            view.clearFields();
            view.showMessage("Student updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating student: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        String studentId = view.getSelectedStudentId();
        if (studentId != null) {
            try {
                model.deleteStudent(studentId);
                loadStudents();
                view.clearFields();
                view.showMessage("Student deleted successfully!");
            } catch (SQLException e) {
                view.showMessage("Error deleting student: " + e.getMessage());
            }
        } else {
            view.showMessage("Please select a student to delete!");
        }
    }

    private void searchStudent() {
        String searchText = view.getSearchText();
        try {
            List<Student> students = model.getAllStudents();
            if (!searchText.isEmpty()) {
                students = students.stream()
                    .filter(s -> s.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                                 s.getStudentId().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            view.updateTable(students);
        } catch (SQLException e) {
            view.showMessage("Error searching students: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        Student student = view.getStudentFromFields();
        if (student.getStudentId().isEmpty() || student.getName().isEmpty() || student.getEmail().isEmpty()) {
            view.showMessage("Student ID, Name, and Email are required!");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", student.getEmail())) {
            view.showMessage("Invalid email format!");
            return false;
        }
        if (!student.getGender().matches("Male|Female|Other")) {
            view.showMessage("Gender must be Male, Female, or Other!");
            return false;
        }
        return true;
    }
}