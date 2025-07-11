package dao;

import model.Class;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {
    public List<Class> getAllClasses() throws SQLException {
        List<Class> classes = new ArrayList<>();
        String query = "SELECT * FROM Classes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Class cls = new Class(
                    rs.getString("class_id"),
                    rs.getString("class_name"),
                    rs.getString("department_id"),
                    rs.getString("academic_year"),
                    rs.getString("teacher_id")
                );
                classes.add(cls);
            }
        }
        return classes;
    }

    public void addClass(Class cls) throws SQLException {
        String query = "INSERT INTO Classes (class_id, class_name, department_id, academic_year, teacher_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, cls.getClassId());
            pstmt.setString(2, cls.getClassName());
            pstmt.setString(3, cls.getDepartmentId());
            pstmt.setString(4, cls.getAcademicYear());
            pstmt.setString(5, cls.getTeacherId());
            pstmt.executeUpdate();
        }
    }

    public void updateClass(Class cls) throws SQLException {
        String query = "UPDATE Classes SET class_name = ?, department_id = ?, academic_year = ?, teacher_id = ? WHERE class_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, cls.getClassName());
            pstmt.setString(2, cls.getDepartmentId());
            pstmt.setString(3, cls.getAcademicYear());
            pstmt.setString(4, cls.getTeacherId());
            pstmt.setString(5, cls.getClassId());
            pstmt.executeUpdate();
        }
    }

    public void deleteClass(String classId) throws SQLException {
        String query = "DELETE FROM Classes WHERE class_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, classId);
            pstmt.executeUpdate();
        }
    }
}
