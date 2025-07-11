package dao;
import model.Course;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM Courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Course course = new Course(
                    rs.getString("course_id"),
                    rs.getString("course_name"),
                    rs.getInt("credits"),
                    rs.getString("department_id"),
                    rs.getString("teacher_id"),
                    rs.getString("semester")
                );
                courses.add(course);
            }
        }
        return courses;
    }

    public void addCourse(Course course) throws SQLException {
        String query = "INSERT INTO Courses (course_id, course_name, credits, department_id, teacher_id, semester) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getDepartmentId());
            pstmt.setString(5, course.getTeacherId());
            pstmt.setString(6, course.getSemester());
            pstmt.executeUpdate();
        }
    }

    public void updateCourse(Course course) throws SQLException {
        String query = "UPDATE Courses SET course_name = ?, credits = ?, department_id = ?, teacher_id = ?, semester = ? WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setInt(2, course.getCredits());
            pstmt.setString(3, course.getDepartmentId());
            pstmt.setString(4, course.getTeacherId());
            pstmt.setString(5, course.getSemester());
            pstmt.setString(6, course.getCourseId());
            pstmt.executeUpdate();
        }
    }

    public void deleteCourse(String courseId) throws SQLException {
        String query = "DELETE FROM Courses WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, courseId);
            pstmt.executeUpdate();
        }
    }
}
