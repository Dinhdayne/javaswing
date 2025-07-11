package dao;
import model.Student;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Student student = new Student(
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("date_of_birth"),
                    rs.getString("gender"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("class_id"),
                    rs.getString("enrollment_date")
                );
                students.add(student);
            }
        }
        return students;
    }

    public void addStudent(Student student) throws SQLException {
        String query = "INSERT INTO Students (student_id, name, date_of_birth, gender, email, phone, address, class_id, enrollment_date) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDateOfBirth());
            pstmt.setString(4, student.getGender());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getPhone());
            pstmt.setString(7, student.getAddress());
            pstmt.setString(8, student.getClassId());
            pstmt.setString(9, student.getEnrollmentDate());
            pstmt.executeUpdate();
        }
    }

    public void updateStudent(Student student) throws SQLException {
        String query = "UPDATE Students SET name = ?, date_of_birth = ?, gender = ?, email = ?, phone = ?, address = ?, class_id = ?, enrollment_date = ? " +
                      "WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDateOfBirth());
            pstmt.setString(3, student.getGender());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getPhone());
            pstmt.setString(6, student.getAddress());
            pstmt.setString(7, student.getClassId());
            pstmt.setString(8, student.getEnrollmentDate());
            pstmt.setString(9, student.getStudentId());
            pstmt.executeUpdate();
        }
    }

    public void deleteStudent(String studentId) throws SQLException {
        String query = "DELETE FROM Students WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.executeUpdate();
        }
    }
}
