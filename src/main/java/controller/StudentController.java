package controller;

import dao.StudentDAO;
import dao.ClassDAO;
import model.Student;
import model.Class;
import model.UserSession;
import view.StudentView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class StudentController {
    private StudentView view;
    private StudentDAO model;
    private ClassDAO classDAO;
    private UserSession userSession;

    public StudentController(StudentView view, StudentDAO model) {
        this.view = view;
        this.model = model;
        this.classDAO = new ClassDAO();
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
            // Teachers can manage students in their classes
            view.setAddButtonEnabled(true);
            view.setUpdateButtonEnabled(true);
            view.setDeleteButtonEnabled(true);
        }
        // Admin has full access (default)
    }

    private void loadStudents() {
        try {
            List<Student> students = model.getAllStudents();
            
            // Filter students based on user role
            String role = userSession.getCurrentRole();
            String currentUserId = userSession.getCurrentUserId();
            
            if ("Student".equals(role)) {
                // Students can only see their own information
                students = students.stream()
                    .filter(s -> s.getStudentId().equals(currentUserId))
                    .toList();
            } else if ("Teacher".equals(role)) {
                // Teachers can only see students in their classes
                List<Class> teacherClasses = classDAO.getAllClasses().stream()
                    .filter(c -> c.getTeacherId().equals(currentUserId))
                    .toList();
                
                List<String> teacherClassIds = teacherClasses.stream()
                    .map(Class::getClassId)
                    .toList();
                
                students = students.stream()
                    .filter(s -> teacherClassIds.contains(s.getClassId()))
                    .toList();
            }
            // Admin sees all students (no additional filtering)
            
            view.updateTable(students);
        } catch (SQLException e) {
            view.showMessage("Error loading students: " + e.getMessage());
        }
    }

    private void addStudent() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_STUDENTS) &&
            !AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_STUDENTS_IN_OWN_CLASS)) {
            view.showMessage("Bạn không có quyền thêm sinh viên!");
            return;
        }
        
        if (!validateFields()) return;
        try {
            Student student = view.getStudentFromFields();
            
            // If teacher, check if adding student to their own class
            if ("Teacher".equals(userSession.getCurrentRole())) {
                if (!isStudentInTeacherClass(student.getClassId())) {
                    view.showMessage("Bạn chỉ có thể thêm sinh viên vào lớp của mình!");
                    return;
                }
            }
            
            model.addStudent(student);
            loadStudents();
            view.clearFields();
            view.showMessage("Student added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding student: " + e.getMessage());
        }
    }

    private void updateStudent() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_STUDENTS) &&
            !AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_STUDENTS_IN_OWN_CLASS)) {
            view.showMessage("Bạn không có quyền cập nhật thông tin sinh viên!");
            return;
        }
        
        if (!validateFields()) return;
        try {
            Student student = view.getStudentFromFields();
            
            // If teacher, check if updating student in their own class
            if ("Teacher".equals(userSession.getCurrentRole())) {
                if (!isStudentInTeacherClass(student.getClassId())) {
                    view.showMessage("Bạn chỉ có thể cập nhật sinh viên trong lớp của mình!");
                    return;
                }
            }
            
            model.updateStudent(student);
            loadStudents();
            view.clearFields();
            view.showMessage("Student updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating student: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_STUDENTS) &&
            !AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_STUDENTS_IN_OWN_CLASS)) {
            view.showMessage("Bạn không có quyền xóa sinh viên!");
            return;
        }
        
        String studentId = view.getSelectedStudentId();
        if (studentId != null) {
            try {
                // If teacher, check if deleting student from their own class
                if ("Teacher".equals(userSession.getCurrentRole())) {
                    Student student = model.getStudentById(studentId);
                    if (student != null && !isStudentInTeacherClass(student.getClassId())) {
                        view.showMessage("Bạn chỉ có thể xóa sinh viên trong lớp của mình!");
                        return;
                    }
                }
                
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
            
            // Apply role-based filtering first
            String role = userSession.getCurrentRole();
            String currentUserId = userSession.getCurrentUserId();
            
            if ("Student".equals(role)) {
                // Students can only see their own information
                students = students.stream()
                    .filter(s -> s.getStudentId().equals(currentUserId))
                    .toList();
            } else if ("Teacher".equals(role)) {
                // Teachers can only see students in their classes
                List<Class> teacherClasses = classDAO.getAllClasses().stream()
                    .filter(c -> c.getTeacherId().equals(currentUserId))
                    .toList();
                
                List<String> teacherClassIds = teacherClasses.stream()
                    .map(Class::getClassId)
                    .toList();
                
                students = students.stream()
                    .filter(s -> teacherClassIds.contains(s.getClassId()))
                    .toList();
            }
            
            // Then apply search filter
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
    
    private boolean isStudentInTeacherClass(String classId) {
        try {
            String currentUserId = userSession.getCurrentUserId();
            List<Class> teacherClasses = classDAO.getAllClasses().stream()
                .filter(c -> c.getTeacherId().equals(currentUserId))
                .toList();
            
            return teacherClasses.stream()
                .anyMatch(c -> c.getClassId().equals(classId));
        } catch (SQLException e) {
            return false;
        }
    }
}