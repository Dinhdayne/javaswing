package controller;

import model.UserSession;

public class AuthorizationService {
    
    public enum Permission {
        // Department permissions
        VIEW_DEPARTMENTS,
        MANAGE_DEPARTMENTS,
        
        // Teacher permissions
        VIEW_TEACHERS,
        MANAGE_TEACHERS,
        
        // Student permissions
        VIEW_ALL_STUDENTS,
        MANAGE_STUDENTS,
        VIEW_OWN_STUDENT_INFO,
        MANAGE_STUDENTS_IN_OWN_CLASS, // Teacher quản lý sinh viên trong lớp của mình
        
        // Class permissions
        VIEW_CLASSES,
        VIEW_OWN_CLASS, // Sinh viên xem lớp của mình
        MANAGE_CLASSES,
        
        // Course permissions
        VIEW_COURSES,
        VIEW_OWN_COURSES, // Sinh viên xem môn học của mình
        MANAGE_COURSES,
        MANAGE_OWN_COURSES, // Teacher quản lý môn học của mình
        
        // Grade permissions
        VIEW_ALL_GRADES,
        MANAGE_GRADES,
        VIEW_OWN_GRADES,
        MANAGE_GRADES_FOR_OWN_COURSES, // Teacher quản lý điểm môn học của mình
        
        // Personal information
        VIEW_OWN_PROFILE,
        EDIT_OWN_PROFILE,
        CHANGE_PASSWORD
    }
    
    public static boolean hasPermission(Permission permission) {
        UserSession session = UserSession.getInstance();
        if (!session.isLoggedIn()) {
            return false;
        }
        
        String role = session.getCurrentRole();
        
        switch (role) {
            case "Admin":
                return hasAdminPermission(permission);
            case "Teacher":
                return hasTeacherPermission(permission);
            case "Student":
                return hasStudentPermission(permission);
            default:
                return false;
        }
    }
    
    private static boolean hasAdminPermission(Permission permission) {
        // Admin has all permissions except viewing own student info (they're not students)
        return permission != Permission.VIEW_OWN_STUDENT_INFO && 
               permission != Permission.VIEW_OWN_GRADES;
    }
    
    private static boolean hasTeacherPermission(Permission permission) {
        switch (permission) {
            case VIEW_TEACHERS:
            case VIEW_ALL_STUDENTS:
            case MANAGE_STUDENTS_IN_OWN_CLASS:
            case VIEW_CLASSES:
            case VIEW_COURSES:
            case MANAGE_OWN_COURSES:
            case VIEW_ALL_GRADES:
            case MANAGE_GRADES_FOR_OWN_COURSES:
            case VIEW_OWN_PROFILE:
            case EDIT_OWN_PROFILE:
            case CHANGE_PASSWORD:
                return true;
            default:
                return false;
        }
    }
    
    private static boolean hasStudentPermission(Permission permission) {
        switch (permission) {
            case VIEW_OWN_STUDENT_INFO:
            case VIEW_OWN_CLASS:
            case VIEW_OWN_COURSES:
            case VIEW_OWN_GRADES:
            case VIEW_OWN_PROFILE:
            case EDIT_OWN_PROFILE:
            case CHANGE_PASSWORD:
                return true;
            default:
                return false;
        }
    }
    
    public static void configureUIForRole(javax.swing.JTabbedPane tabbedPane) {
        UserSession session = UserSession.getInstance();
        String role = session.getCurrentRole();
        
        // Reset all tabs to disabled first
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setEnabledAt(i, false);
        }
        
        // Enable tabs based on role
        // Tab indices: 0=Departments, 1=Teachers, 2=Students, 3=Classes, 4=Courses, 5=Grades, 6=Personal Info
        switch (role) {
            case "Admin":
                // Admin can access all tabs
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    tabbedPane.setEnabledAt(i, true);
                }
                break;
                
            case "Teacher":
                // Teacher can access: Teachers, Students (view), Classes, Courses, Grades, Personal Info
                tabbedPane.setEnabledAt(1, true); // Teachers
                tabbedPane.setEnabledAt(2, true); // Students  
                tabbedPane.setEnabledAt(3, true); // Classes
                tabbedPane.setEnabledAt(4, true); // Courses
                tabbedPane.setEnabledAt(5, true); // Grades
                tabbedPane.setEnabledAt(6, true); // Personal Info
                break;
                
            case "Student":
                // Student can access: Classes (own class only), Courses (own courses only), Grades (own grades only), Personal Info
                tabbedPane.setEnabledAt(3, true); // Classes (own class only)
                tabbedPane.setEnabledAt(4, true); // Courses (own courses only)
                tabbedPane.setEnabledAt(5, true); // Grades (own grades only)
                tabbedPane.setEnabledAt(6, true); // Personal Info
                break;
        }
    }
}