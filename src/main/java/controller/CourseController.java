package controller;

import dao.CourseDAO;
import dao.GradeDAO;
import model.Course;
import model.Grade;
import model.UserSession;
import view.CourseView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class CourseController {
    private CourseView view;
    private CourseDAO model;
    private GradeDAO gradeDAO;
    private UserSession userSession;

    public CourseController(CourseView view, CourseDAO model) {
        this.view = view;
        this.model = model;
        this.gradeDAO = new GradeDAO();
        this.userSession = UserSession.getInstance();

        loadCourses();
        setupEventListeners();
        configureUIForRole();
    }
    
    private void setupEventListeners() {
        view.setAddButtonListener(e -> addCourse());
        view.setUpdateButtonListener(e -> updateCourse());
        view.setDeleteButtonListener(e -> deleteCourse());
        view.setSearchButtonListener(e -> searchCourse());
    }
    
    private void configureUIForRole() {
        String role = userSession.getCurrentRole();
        
        if ("Student".equals(role)) {
            // Students can only view courses
            view.setAddButtonEnabled(false);
            view.setUpdateButtonEnabled(false);
            view.setDeleteButtonEnabled(false);
        } else if ("Teacher".equals(role)) {
            // Teachers can manage their own courses
            view.setAddButtonEnabled(false); // Only admin can add new courses
        }
        // Admin has full access (default)
    }

    private void loadCourses() {
        try {
            List<Course> courses = model.getAllCourses();
            
            // Filter courses based on user role
            String role = userSession.getCurrentRole();
            String currentUserId = userSession.getCurrentUserId();
            
            if ("Student".equals(role)) {
                // Students can only see courses they are enrolled in (have grades for)
                List<Grade> studentGrades = gradeDAO.getAllGrades().stream()
                    .filter(g -> g.getStudentId().equals(currentUserId))
                    .toList();
                
                List<String> enrolledCourseIds = studentGrades.stream()
                    .map(Grade::getCourseId)
                    .distinct()
                    .toList();
                
                courses = courses.stream()
                    .filter(c -> enrolledCourseIds.contains(c.getCourseId()))
                    .toList();
            } else if ("Teacher".equals(role)) {
                // Teachers can see courses they teach
                courses = courses.stream()
                    .filter(c -> c.getTeacherId().equals(currentUserId))
                    .toList();
            }
            // Admin sees all courses (no filtering)
            
            view.updateTable(courses);
        } catch (SQLException e) {
            view.showMessage("Error loading courses: " + e.getMessage());
        }
    }

    private void addCourse() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_COURSES)) {
            view.showMessage("Bạn không có quyền thêm môn học!");
            return;
        }
        if (!validateFields()) return;
        try {
            Course course = view.getCourseFromFields();
            model.addCourse(course);
            loadCourses();
            view.clearFields();
            view.showMessage("Course added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding course: " + e.getMessage());
        }
    }

    private void updateCourse() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_COURSES) &&
            !AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_OWN_COURSES)) {
            view.showMessage("Bạn không có quyền cập nhật môn học!");
            return;
        }
        
        if (!validateFields()) return;
        try {
            Course course = view.getCourseFromFields();
            
            // If teacher, check if updating their own course
            if ("Teacher".equals(userSession.getCurrentRole())) {
                if (!course.getTeacherId().equals(userSession.getCurrentUserId())) {
                    view.showMessage("Bạn chỉ có thể cập nhật môn học mà mình dạy!");
                    return;
                }
            }
            
            model.updateCourse(course);
            loadCourses();
            view.clearFields();
            view.showMessage("Course updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating course: " + e.getMessage());
        }
    }

    private void deleteCourse() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_COURSES)) {
            view.showMessage("Bạn không có quyền xóa môn học!");
            return;
        }
        
        String courseId = view.getSelectedCourseId();
        if (courseId != null) {
            try {
                model.deleteCourse(courseId);
                loadCourses();
                view.clearFields();
                view.showMessage("Course deleted successfully!");
            } catch (SQLException e) {
                view.showMessage("Error deleting course: " + e.getMessage());
            }
        } else {
            view.showMessage("Please select a course to delete!");
        }
    }

    private void searchCourse() {
        String searchText = view.getSearchText();
        try {
            List<Course> courses = model.getAllCourses();
            
            // Apply role-based filtering first
            String role = userSession.getCurrentRole();
            String currentUserId = userSession.getCurrentUserId();
            
            if ("Student".equals(role)) {
                // Students can only see courses they are enrolled in (have grades for)
                List<Grade> studentGrades = gradeDAO.getAllGrades().stream()
                    .filter(g -> g.getStudentId().equals(currentUserId))
                    .toList();
                
                List<String> enrolledCourseIds = studentGrades.stream()
                    .map(Grade::getCourseId)
                    .distinct()
                    .toList();
                
                courses = courses.stream()
                    .filter(c -> enrolledCourseIds.contains(c.getCourseId()))
                    .toList();
            } else if ("Teacher".equals(role)) {
                // Teachers can see courses they teach
                courses = courses.stream()
                    .filter(c -> c.getTeacherId().equals(currentUserId))
                    .toList();
            }
            
            // Then apply search filter
            if (!searchText.isEmpty()) {
                courses = courses.stream()
                    .filter(c -> c.getCourseName().toLowerCase().contains(searchText.toLowerCase()) ||
                                 c.getCourseId().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            
            view.updateTable(courses);
        } catch (SQLException e) {
            view.showMessage("Error searching courses: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        try {
            Course course = view.getCourseFromFields();
            if (course.getCourseId().isEmpty() || course.getCourseName().isEmpty()) {
                view.showMessage("Course ID and Course Name are required!");
                return false;
            }
            if (course.getCredits() <= 0) {
                view.showMessage("Credits must be a positive number!");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            view.showMessage("Invalid number format for Credits!");
            return false;
        }
    }
}