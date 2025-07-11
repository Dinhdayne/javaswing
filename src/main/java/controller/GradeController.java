package controller;

import dao.GradeDAO;
import dao.CourseDAO;
import dao.StudentDAO;
import model.Grade;
import model.Course;
import model.Student;
import model.UserSession;
import view.GradeView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class GradeController {
    private GradeView view;
    private GradeDAO model;
    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private UserSession userSession;

    public GradeController(GradeView view, GradeDAO model) {
        this.view = view;
        this.model = model;
        this.courseDAO = new CourseDAO();
        this.studentDAO = new StudentDAO();
        this.userSession = UserSession.getInstance();

        loadGrades();
        setupEventListeners();
        configureUIForRole();
    }
    
    private void setupEventListeners() {
        view.setAddButtonListener(e -> addGrade());
        view.setUpdateButtonListener(e -> updateGrade());
        view.setDeleteButtonListener(e -> deleteGrade());
        view.setSearchButtonListener(e -> searchGrade());
    }
    
    private void configureUIForRole() {
        String role = userSession.getCurrentRole();
        
        if ("Student".equals(role)) {
            // Students can only view their own grades
            view.setAddButtonEnabled(false);
            view.setUpdateButtonEnabled(false);
            view.setDeleteButtonEnabled(false);
        }
        // Teachers and Admin can manage grades (with appropriate restrictions)
    }

    private void loadGrades() {
        try {
            List<Grade> grades = model.getAllGrades();
            
            // Filter grades based on user role
            String role = userSession.getCurrentRole();
            String currentUserId = userSession.getCurrentUserId();
            
            // DEBUG: Add logging to identify the issue
            System.out.println("=== DEBUG GRADE LOADING ===");
            System.out.println("Current User ID: " + currentUserId);
            System.out.println("Current Role: " + role);
            System.out.println("Total grades before filtering: " + grades.size());
            
            if ("Student".equals(role)) {
                // FIX: For students, we need to find their studentId first
                String studentId = getStudentIdFromUserId(currentUserId);
                System.out.println("Student ID mapped from User ID: " + studentId);
                
                if (studentId != null) {
                    // Students can only see their own grades
                    grades = grades.stream()
                        .filter(g -> {
                            boolean matches = g.getStudentId().equals(studentId);
                            System.out.println("Grade " + g.getGradeId() + " - Student ID: " + g.getStudentId() + 
                                             " matches " + studentId + ": " + matches);
                            return matches;
                        })
                        .toList();
                } else {
                    // If we can't find the student ID, show no grades for security
                    grades = List.of();
                    System.out.println("WARNING: Could not find studentId for userId: " + currentUserId);
                }
            } else if ("Teacher".equals(role)) {
                // Teachers can only see grades for courses they teach
                List<Course> teacherCourses = courseDAO.getAllCourses().stream()
                    .filter(c -> c.getTeacherId().equals(currentUserId))
                    .toList();
                
                List<String> teacherCourseIds = teacherCourses.stream()
                    .map(Course::getCourseId)
                    .toList();
                
                grades = grades.stream()
                    .filter(g -> teacherCourseIds.contains(g.getCourseId()))
                    .toList();
            }
            // Admin sees all grades (no filtering)
            
            System.out.println("Total grades after filtering: " + grades.size());
            System.out.println("=== END DEBUG ===");
            
            view.updateTable(grades);
        } catch (SQLException e) {
            view.showMessage("Error loading grades: " + e.getMessage());
        }
    }
    
    /**
     * FIX: New method to map Account.userId to Student.studentId
     * This is the key fix for the security vulnerability
     */
    private String getStudentIdFromUserId(String userId) {
        try {
            // Get all students and find the one whose studentId matches the userId
            // This assumes that for student accounts, Account.userId should match Student.studentId
            List<Student> students = studentDAO.getAllStudents();
            
            // First try direct match (if userId == studentId)
            for (Student student : students) {
                if (student.getStudentId().equals(userId)) {
                    return student.getStudentId();
                }
            }
            
            // If no direct match, try alternative mappings
            // This might be needed if the Account.userId format is different from Student.studentId
            // For example: userId might be "student001" while studentId is "ST001"
            
            // Try to extract numeric part and map to student format
            if (userId.startsWith("student")) {
                String numericPart = userId.substring(7); // Remove "student" prefix
                String studentIdPattern = "ST" + String.format("%03d", Integer.parseInt(numericPart));
                
                for (Student student : students) {
                    if (student.getStudentId().equals(studentIdPattern)) {
                        return student.getStudentId();
                    }
                }
            }
            
            return null; // No matching student found
        } catch (SQLException e) {
            System.err.println("Error getting student ID from user ID: " + e.getMessage());
            return null;
        }
    }

    private void addGrade() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_GRADES) &&
            !AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_GRADES_FOR_OWN_COURSES)) {
            view.showMessage("Bạn không có quyền thêm điểm!");
            return;
        }
        
        if (!validateFields()) return;
        try {
            Grade grade = view.getGradeFromFields();
            
            // If teacher, check if adding grade for their own course
            if ("Teacher".equals(userSession.getCurrentRole())) {
                if (!isGradeForTeacherCourse(grade.getCourseId())) {
                    view.showMessage("Bạn chỉ có thể thêm điểm cho môn học mà mình dạy!");
                    return;
                }
            }
            
            model.addGrade(grade);
            loadGrades();
            view.clearFields();
            view.showMessage("Grade added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding grade: " + e.getMessage());
        }
    }

    private void updateGrade() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_GRADES) &&
            !AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_GRADES_FOR_OWN_COURSES)) {
            view.showMessage("Bạn không có quyền cập nhật điểm!");
            return;
        }
        
        if (!validateFields()) return;
        try {
            Grade grade = view.getGradeFromFields();
            
            // If teacher, check if updating grade for their own course
            if ("Teacher".equals(userSession.getCurrentRole())) {
                if (!isGradeForTeacherCourse(grade.getCourseId())) {
                    view.showMessage("Bạn chỉ có thể cập nhật điểm cho môn học mà mình dạy!");
                    return;
                }
            }
            
            model.updateGrade(grade);
            loadGrades();
            view.clearFields();
            view.showMessage("Grade updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating grade: " + e.getMessage());
        }
    }

    private void deleteGrade() {
        if (!AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_GRADES) &&
            !AuthorizationService.hasPermission(AuthorizationService.Permission.MANAGE_GRADES_FOR_OWN_COURSES)) {
            view.showMessage("Bạn không có quyền xóa điểm!");
            return;
        }
        
        int gradeId = view.getSelectedGradeId();
        if (gradeId != -1) {
            try {
                // If teacher, check if deleting grade for their own course
                if ("Teacher".equals(userSession.getCurrentRole())) {
                    Grade grade = model.getGradeById(gradeId);
                    if (grade != null && !isGradeForTeacherCourse(grade.getCourseId())) {
                        view.showMessage("Bạn chỉ có thể xóa điểm cho môn học mà mình dạy!");
                        return;
                    }
                }
                
                model.deleteGrade(gradeId);
                loadGrades();
                view.clearFields();
                view.showMessage("Grade deleted successfully!");
            } catch (SQLException e) {
                view.showMessage("Error deleting grade: " + e.getMessage());
            }
        } else {
            view.showMessage("Please select a grade to delete!");
        }
    }

    private void searchGrade() {
        String searchText = view.getSearchText();
        try {
            List<Grade> grades = model.getAllGrades();
            
            // Apply role-based filtering first
            String role = userSession.getCurrentRole();
            String currentUserId = userSession.getCurrentUserId();
            
            if ("Student".equals(role)) {
                // FIX: Use the same mapping logic for search
                String studentId = getStudentIdFromUserId(currentUserId);
                if (studentId != null) {
                    grades = grades.stream()
                        .filter(g -> g.getStudentId().equals(studentId))
                        .toList();
                } else {
                    grades = List.of();
                }
            } else if ("Teacher".equals(role)) {
                // Teachers can only see grades for courses they teach
                List<Course> teacherCourses = courseDAO.getAllCourses().stream()
                    .filter(c -> c.getTeacherId().equals(currentUserId))
                    .toList();
                
                List<String> teacherCourseIds = teacherCourses.stream()
                    .map(Course::getCourseId)
                    .toList();
                
                grades = grades.stream()
                    .filter(g -> teacherCourseIds.contains(g.getCourseId()))
                    .toList();
            }
            
            // Then apply search filter
            if (!searchText.isEmpty()) {
                grades = grades.stream()
                    .filter(g -> g.getStudentId().toLowerCase().contains(searchText.toLowerCase()) ||
                                 g.getCourseId().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            
            view.updateTable(grades);
        } catch (SQLException e) {
            view.showMessage("Error searching grades: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        try {
            Grade grade = view.getGradeFromFields();
            if (grade.getStudentId().isEmpty() || grade.getCourseId().isEmpty()) {
                view.showMessage("Student ID and Course ID are required!");
                return false;
            }
            if (grade.getMidtermGrade() < 0 || grade.getMidtermGrade() > 10 ||
                grade.getFinalGrade() < 0 || grade.getFinalGrade() > 10 ||
                grade.getOverallGrade() < 0 || grade.getOverallGrade() > 10) {
                view.showMessage("Grades must be between 0 and 10!");
                return false;
            }
            if (!grade.getStatus().matches("Pass|Fail|Pending")) {
                view.showMessage("Status must be Pass, Fail, or Pending!");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            view.showMessage("Invalid number format for grades!");
            return false;
        }
    }
    
    private boolean isGradeForTeacherCourse(String courseId) {
        try {
            String currentUserId = userSession.getCurrentUserId();
            List<Course> teacherCourses = courseDAO.getAllCourses().stream()
                .filter(c -> c.getTeacherId().equals(currentUserId))
                .toList();
            
            return teacherCourses.stream()
                .anyMatch(c -> c.getCourseId().equals(courseId));
        } catch (SQLException e) {
            return false;
        }
    }
}