package dao;

import model.Grade;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {
    public List<Grade> getAllGrades() throws SQLException {
        List<Grade> grades = new ArrayList<>();
        String query = "SELECT * FROM Grades";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Grade grade = new Grade(
                    rs.getInt("grade_id"),
                    rs.getString("student_id"),
                    rs.getString("course_id"),
                    rs.getString("semester"),
                    rs.getDouble("midterm_grade"),
                    rs.getDouble("final_grade"),
                    rs.getDouble("overall_grade"),
                    rs.getString("status"),
                    rs.getString("notes")
                );
                grades.add(grade);
            }
        }
        return grades;
    }

    public void addGrade(Grade grade) throws SQLException {
        String query = "INSERT INTO Grades (student_id, course_id, semester, midterm_grade, final_grade, overall_grade, status, notes) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, grade.getStudentId());
            pstmt.setString(2, grade.getCourseId());
            pstmt.setString(3, grade.getSemester());
            pstmt.setDouble(4, grade.getMidtermGrade());
            pstmt.setDouble(5, grade.getFinalGrade());
            pstmt.setDouble(6, grade.getOverallGrade());
            pstmt.setString(7, grade.getStatus());
            pstmt.setString(8, grade.getNotes());
            pstmt.executeUpdate();
        }
    }

    public void updateGrade(Grade grade) throws SQLException {
        String query = "UPDATE Grades SET student_id = ?, course_id = ?, semester = ?, midterm_grade = ?, final_grade = ?, overall_grade = ?, status = ?, notes = ? " +
                      "WHERE grade_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, grade.getStudentId());
            pstmt.setString(2, grade.getCourseId());
            pstmt.setString(3, grade.getSemester());
            pstmt.setDouble(4, grade.getMidtermGrade());
            pstmt.setDouble(5, grade.getFinalGrade());
            pstmt.setDouble(6, grade.getOverallGrade());
            pstmt.setString(7, grade.getStatus());
            pstmt.setString(8, grade.getNotes());
            pstmt.setInt(9, grade.getGradeId());
            pstmt.executeUpdate();
        }
    }

    public void deleteGrade(int gradeId) throws SQLException {
        String query = "DELETE FROM Grades WHERE grade_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, gradeId);
            pstmt.executeUpdate();
        }
    }
}
