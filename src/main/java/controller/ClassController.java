/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ClassDAO;
import dao.StudentDAO;
import model.Class;
import model.Student;
import model.UserSession;
import view.ClassView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ClassController {
    private ClassView view;
    private ClassDAO model;
    private StudentDAO studentDAO;
    private UserSession userSession;

    public ClassController(ClassView view, ClassDAO model) {
        this.view = view;
        this.model = model;
        this.studentDAO = new StudentDAO();
        this.userSession = UserSession.getInstance();

        loadClasses();
        setupEventListeners();
        configureUIForRole();
    }
    
    private void setupEventListeners() {
        view.setAddButtonListener(e -> addClass());
        view.setUpdateButtonListener(e -> updateClass());
        view.setDeleteButtonListener(e -> deleteClass());
        view.setSearchButtonListener(e -> searchClass());
    }
    
    private void configureUIForRole() {
        String role = userSession.getCurrentRole();
        
        if ("Student".equals(role)) {
            // Students can only view their own class
            view.setAddButtonEnabled(false);
            view.setUpdateButtonEnabled(false);
            view.setDeleteButtonEnabled(false);
        } else if ("Teacher".equals(role)) {
            // Teachers can view and manage classes
            view.setAddButtonEnabled(false); // Only admin can add classes
        }
        // Admin has full access (default)
    }

    private void loadClasses() {
        try {
            List<Class> classes = model.getAllClasses();
            
            // Filter classes based on user role
            String role = userSession.getCurrentRole();
            String currentUserId = userSession.getCurrentUserId();
            
            if ("Student".equals(role)) {
                // Students can only see their own class
                Student student = studentDAO.getStudentById(currentUserId);
                if (student != null) {
                    String studentClassId = student.getClassId();
                    classes = classes.stream()
                        .filter(c -> c.getClassId().equals(studentClassId))
                        .toList();
                }
            } else if ("Teacher".equals(role)) {
                // Teachers can see classes they teach
                classes = classes.stream()
                    .filter(c -> c.getTeacherId().equals(currentUserId))
                    .toList();
            }
            // Admin sees all classes (no filtering)
            
            view.updateTable(classes);
        } catch (SQLException e) {
            view.showMessage("Error loading classes: " + e.getMessage());
        }
    }

    private void addClass() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_CLASSES)) {
            view.showMessage("Bạn không có quyền thêm lớp học!");
            return;
        }
        if (!validateFields()) return;
        try {
            Class cls = view.getClassFromFields();
            model.addClass(cls);
            loadClasses();
            view.clearFields();
            view.showMessage("Class added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding class: " + e.getMessage());
        }
    }

    private void updateClass() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_CLASSES)) {
            view.showMessage("Bạn không có quyền cập nhật lớp học!");
            return;
        }
        if (!validateFields()) return;
        try {
            Class cls = view.getClassFromFields();
            model.updateClass(cls);
            loadClasses();
            view.clearFields();
            view.showMessage("Class updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating class: " + e.getMessage());
        }
    }

    private void deleteClass() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_CLASSES)) {
            view.showMessage("Bạn không có quyền xóa lớp học!");
            return;
        }
        String classId = view.getSelectedClassId();
        if (classId != null) {
            try {
                model.deleteClass(classId);
                loadClasses();
                view.clearFields();
                view.showMessage("Class deleted successfully!");
            } catch (SQLException e) {
                view.showMessage("Error deleting class: " + e.getMessage());
            }
        } else {
            view.showMessage("Please select a class to delete!");
        }
    }

    private void searchClass() {
        String searchText = view.getSearchText();
        try {
            List<Class> classes = model.getAllClasses();
            
            // Apply role-based filtering first
            String role = userSession.getCurrentRole();
            String currentUserId = userSession.getCurrentUserId();
            
            if ("Student".equals(role)) {
                // Students can only see their own class
                Student student = studentDAO.getStudentById(currentUserId);
                if (student != null) {
                    String studentClassId = student.getClassId();
                    classes = classes.stream()
                        .filter(c -> c.getClassId().equals(studentClassId))
                        .toList();
                }
            } else if ("Teacher".equals(role)) {
                // Teachers can see classes they teach
                classes = classes.stream()
                    .filter(c -> c.getTeacherId().equals(currentUserId))
                    .toList();
            }
            
            // Then apply search filter
            if (!searchText.isEmpty()) {
                classes = classes.stream()
                    .filter(c -> c.getClassName().toLowerCase().contains(searchText.toLowerCase()) ||
                                 c.getClassId().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            
            view.updateTable(classes);
        } catch (SQLException e) {
            view.showMessage("Error searching classes: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        Class cls = view.getClassFromFields();
        if (cls.getClassId().isEmpty() || cls.getClassName().isEmpty()) {
            view.showMessage("Class ID and Class Name are required!");
            return false;
        }
        return true;
    }
    
}
