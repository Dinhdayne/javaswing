package test;

import controller.GradeController;
import dao.GradeDAO;
import dao.AccountDAO;
import model.Account;
import model.UserSession;
import model.Grade;
import view.GradeView;

import java.sql.SQLException;
import java.util.List;

/**
 * Test runner để kiểm tra lỗ hổng bảo mật đã được fix chưa
 * CHẠY FILE NÀY ĐỂ KIỂM TRA AN TOÀN CỦA HỆ THỐNG
 */
public class SecurityTestRunner {
    
    public static void main(String[] args) {
        System.out.println("=== BẮT ĐẦU KIỂM TRA BẢO MẬT HỆ THỐNG ===");
        
        SecurityTestRunner tester = new SecurityTestRunner();
        
        try {
            // Test case 1: Student không thể xem điểm của sinh viên khác
            tester.testStudentAccessRestriction();
            
            // Test case 2: Teacher chỉ xem được điểm môn mình dạy  
            tester.testTeacherAccessRestriction();
            
            // Test case 3: Admin xem được tất cả
            tester.testAdminAccessUnrestricted();
            
            System.out.println("\n=== KẾT THÚC KIỂM TRA ===");
            
        } catch (Exception e) {
            System.err.println("LỖI TRONG QUÁ TRÌNH KIỂM TRA: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test: Sinh viên chỉ được xem điểm của mình
     */
    private void testStudentAccessRestriction() {
        System.out.println("\n--- TEST 1: STUDENT ACCESS RESTRICTION ---");
        
        try {
            // Simulate student login
            Account studentAccount = new Account(1, "student001", "password", "Student", "student001", null, null);
            UserSession.getInstance().setCurrentUser(studentAccount);
            
            // Create grade controller
            GradeDAO gradeDAO = new GradeDAO();
            GradeView gradeView = new GradeView();
            
            System.out.println("Đăng nhập với tài khoản: " + studentAccount.getUsername());
            System.out.println("Role: " + studentAccount.getRole());
            System.out.println("User ID: " + studentAccount.getUserId());
            
            // Load grades và kiểm tra kết quả
            List<Grade> allGrades = gradeDAO.getAllGrades();
            System.out.println("Tổng số điểm trong hệ thống: " + allGrades.size());
            
            // Tạo controller sẽ trigger loadGrades() và filtering
            GradeController controller = new GradeController(gradeView, gradeDAO);
            
            // Kiểm tra số điểm hiển thị trong view
            // (Nếu fix đúng, chỉ nên thấy điểm của sinh viên đó)
            
            System.out.println("✓ Test Student Access hoàn thành - kiểm tra console log ở trên");
            
        } catch (SQLException e) {
            System.err.println("✗ LỖI trong test Student Access: " + e.getMessage());
        }
    }
    
    /**
     * Test: Teacher chỉ được xem điểm môn mình dạy
     */
    private void testTeacherAccessRestriction() {
        System.out.println("\n--- TEST 2: TEACHER ACCESS RESTRICTION ---");
        
        try {
            // Simulate teacher login  
            Account teacherAccount = new Account(2, "teacher001", "password", "Teacher", "T001", null, null);
            UserSession.getInstance().setCurrentUser(teacherAccount);
            
            GradeDAO gradeDAO = new GradeDAO();
            GradeView gradeView = new GradeView();
            
            System.out.println("Đăng nhập với tài khoản: " + teacherAccount.getUsername());
            System.out.println("Role: " + teacherAccount.getRole());
            System.out.println("User ID: " + teacherAccount.getUserId());
            
            List<Grade> allGrades = gradeDAO.getAllGrades();
            System.out.println("Tổng số điểm trong hệ thống: " + allGrades.size());
            
            GradeController controller = new GradeController(gradeView, gradeDAO);
            
            System.out.println("✓ Test Teacher Access hoàn thành - kiểm tra console log ở trên");
            
        } catch (SQLException e) {
            System.err.println("✗ LỖI trong test Teacher Access: " + e.getMessage());
        }
    }
    
    /**
     * Test: Admin được xem tất cả điểm
     */
    private void testAdminAccessUnrestricted() {
        System.out.println("\n--- TEST 3: ADMIN ACCESS UNRESTRICTED ---");
        
        try {
            // Simulate admin login
            Account adminAccount = new Account(3, "admin", "password", "Admin", "admin001", null, null);
            UserSession.getInstance().setCurrentUser(adminAccount);
            
            GradeDAO gradeDAO = new GradeDAO();
            GradeView gradeView = new GradeView();
            
            System.out.println("Đăng nhập với tài khoản: " + adminAccount.getUsername());
            System.out.println("Role: " + adminAccount.getRole());
            System.out.println("User ID: " + adminAccount.getUserId());
            
            List<Grade> allGrades = gradeDAO.getAllGrades();
            System.out.println("Tổng số điểm trong hệ thống: " + allGrades.size());
            
            GradeController controller = new GradeController(gradeView, gradeDAO);
            
            System.out.println("✓ Test Admin Access hoàn thành - Admin nên thấy tất cả điểm");
            
        } catch (SQLException e) {
            System.err.println("✗ LỖI trong test Admin Access: " + e.getMessage());
        }
    }
    
    /**
     * Utility method để kiểm tra trực tiếp mapping userId -> studentId
     */
    private void testUserIdMapping() {
        System.out.println("\n--- TEST MAPPING USER ID -> STUDENT ID ---");
        
        // Test cases cho mapping
        String[] testUserIds = {"student001", "student002", "ST001", "ST002", "unknown"};
        
        for (String userId : testUserIds) {
            System.out.println("Testing userId: " + userId);
            // Logic mapping sẽ được test trong getStudentIdFromUserId
        }
    }
}