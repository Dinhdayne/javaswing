package controller;

import dao.StudentDAO;
import model.Student;
import model.UserSession;
import view.StudentView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class StudentController {
    private StudentView view;
    private StudentDAO model;
    private UserSession userSession;

    public StudentController(StudentView view, StudentDAO model) {
        this.view = view;
        this.model = model;
        this.userSession = UserSession.getInstance();

        loadStudents();
        setupEventListeners();
        configureUIForRole();
    }
    
    private void setupEventListeners() {
        view.setAddButtonListener(e -> addStudent());
        view.setUpdateButtonListener(e -> updateStudent());
        view.setDeleteButtonListener(e -> deleteStudent());
        view.setSearchButtonListener(e -> searchStudent());
    }
    
    private void configureUIForRole() {
        // Configure UI elements based on user role
        String role = userSession.getCurrentRole();
        
        if ("Student".equals(role)) {
            // Students can only view their own information
            view.setAddButtonEnabled(false);
            view.setUpdateButtonEnabled(false);
            view.setDeleteButtonEnabled(false);
        } else if ("Teacher".equals(role)) {
            // Teachers can view all students but may have limited editing capabilities
            view.setAddButtonEnabled(false);
            view.setDeleteButtonEnabled(false);
        }
        // Admin has full access (default)
    }

    private void loadStudents() {
        try {
            List<Student> students = model.getAllStudents();
            
            // Filter students based on user role
            String role = userSession.getCurrentRole();
            if ("Student".equals(role)) {
                // Students can only see their own information
                String currentUserId = userSession.getCurrentUserId();
                students = students.stream()
                    .filter(s -> s.getStudentId().equals(currentUserId))
                    .toList();
            }
            
            view.updateTable(students);
        } catch (SQLException e) {
            view.showMessage("Error loading students: " + e.getMessage());
        }
    }

    private void addStudent() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_STUDENTS)) {
            view.showMessage("Bạn không có quyền thêm sinh viên!");
            return;
        }
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
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_STUDENTS)) {
            view.showMessage("Bạn không có quyền cập nhật thông tin sinh viên!");
            return;
        }
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
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_STUDENTS)) {
            view.showMessage("Bạn không có quyền xóa sinh viên!");
            return;
        }
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