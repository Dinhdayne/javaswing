package dao;

import model.Teacher;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {
    public List<Teacher> getAllTeachers() throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        String query = "SELECT * FROM Teachers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Teacher teacher = new Teacher(
                    rs.getString("teacher_id"),
                    rs.getString("name"),
                    rs.getString("date_of_birth"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("department_id"),
                    rs.getString("hire_date")
                );
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public void addTeacher(Teacher teacher) throws SQLException {
        String query = "INSERT INTO Teachers (teacher_id, name, date_of_birth, gender, email, phone, address, department_id, hire_date) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, teacher.getTeacherId());
            pstmt.setString(2, teacher.getName());
            pstmt.setString(3, teacher.getDateOfBirth());
            pstmt.setString(4, teacher.getGender());
            pstmt.setString(5, teacher.getEmail());
            pstmt.setString(6, teacher.getPhone());
            pstmt.setString(7, teacher.getAddress());
            pstmt.setString(8, teacher.getDepartmentId());
            pstmt.setString(9, teacher.getHireDate());
            pstmt.executeUpdate();
        }
    }

    public void updateTeacher(Teacher teacher) throws SQLException {
        String query = "UPDATE Teachers SET name = ?, date_of_birth = ?, gender = ?, email = ?, phone = ?, address = ?, department_id = ?, hire_date = ? " +
                      "WHERE teacher_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, teacher.getName());
            pstmt.setString(2, teacher.getDateOfBirth());
            pstmt.setString(3, teacher.getGender());
            pstmt.setString(4, teacher.getEmail());
            pstmt.setString(5, teacher.getPhone());
            pstmt.setString(6, teacher.getAddress());
            pstmt.setString(7, teacher.getDepartmentId());
            pstmt.setString(8, teacher.getHireDate());
            pstmt.setString(9, teacher.getTeacherId());
            pstmt.executeUpdate();
        }
    }

    public void deleteTeacher(String teacherId) throws SQLException {
        String query = "DELETE FROM Teachers WHERE teacher_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, teacherId);
            pstmt.executeUpdate();
        }
    }
}
