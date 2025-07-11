package dao;

import model.Department;
import model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    public List<Department> getAllDepartments() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String query = "SELECT * FROM Departments";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Department dept = new Department(
                    rs.getString("department_id"),
                    rs.getString("department_name"),
                    rs.getString("head_teacher_id"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                departments.add(dept);
            }
        }
        return departments;
    }

    public void addDepartment(Department dept) throws SQLException {
        String query = "INSERT INTO Departments (department_id, department_name, head_teacher_id, phone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dept.getDepartmentId());
            pstmt.setString(2, dept.getDepartmentName());
            pstmt.setString(3, dept.getHeadTeacherId());
            pstmt.setString(4, dept.getPhone());
            pstmt.setString(5, dept.getEmail());
            pstmt.executeUpdate();
        }
    }

    public void updateDepartment(Department dept) throws SQLException {
        String query = "UPDATE Departments SET department_name = ?, head_teacher_id = ?, phone = ?, email = ? WHERE department_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dept.getDepartmentName());
            pstmt.setString(2, dept.getHeadTeacherId());
            pstmt.setString(3, dept.getPhone());
            pstmt.setString(4, dept.getEmail());
            pstmt.setString(5, dept.getDepartmentId());
            pstmt.executeUpdate();
        }
    }

    public void deleteDepartment(String departmentId) throws SQLException {
        String query = "DELETE FROM Departments WHERE department_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, departmentId);
            pstmt.executeUpdate();
        }
    }
}
