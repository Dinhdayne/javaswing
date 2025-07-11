package controller;

import dao.DepartmentDAO;
import model.Department;
import view.DepartmentView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class DepartmentController {
    private DepartmentView view;
    private DepartmentDAO model;

    public DepartmentController(DepartmentView view, DepartmentDAO model) {
        this.view = view;
        this.model = model;

        loadDepartments();
        view.setAddButtonListener(e -> addDepartment());
        view.setUpdateButtonListener(e -> updateDepartment());
        view.setDeleteButtonListener(e -> deleteDepartment());
        view.setSearchButtonListener(e -> searchDepartment());
    }

    private void loadDepartments() {
        try {
            List<Department> departments = model.getAllDepartments();
            view.updateTable(departments);
        } catch (SQLException e) {
            view.showMessage("Error loading departments: " + e.getMessage());
        }
    }

    private void addDepartment() {
        if (!validateFields()) return;
        try {
            Department dept = view.getDepartmentFromFields();
            model.addDepartment(dept);
            loadDepartments();
            view.clearFields();
            view.showMessage("Department added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding department: " + e.getMessage());
        }
    }

    private void updateDepartment() {
        if (!validateFields()) return;
        try {
            Department dept = view.getDepartmentFromFields();
            model.updateDepartment(dept);
            loadDepartments();
            view.clearFields();
            view.showMessage("Department updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating department: " + e.getMessage());
        }
    }

    private void deleteDepartment() {
        String deptId = view.getSelectedDepartmentId();
        if (deptId != null) {
            try {
                model.deleteDepartment(deptId);
                loadDepartments();
                view.clearFields();
                view.showMessage("Department deleted successfully!");
            } catch (SQLException e) {
                view.showMessage("Error deleting department: " + e.getMessage());
            }
        } else {
            view.showMessage("Please select a department to delete!");
        }
    }

    private void searchDepartment() {
        String searchText = view.getSearchText();
        try {
            List<Department> departments = model.getAllDepartments();
            if (!searchText.isEmpty()) {
                departments = departments.stream()
                    .filter(d -> d.getDepartmentName().toLowerCase().contains(searchText.toLowerCase()) ||
                                 d.getDepartmentId().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            view.updateTable(departments);
        } catch (SQLException e) {
            view.showMessage("Error searching departments: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        if (view.getDepartmentFromFields().getDepartmentId().isEmpty() ||
            view.getDepartmentFromFields().getDepartmentName().isEmpty() ||
            view.getDepartmentFromFields().getEmail().isEmpty()) {
            view.showMessage("Department ID, Name, and Email are required!");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", 
            view.getDepartmentFromFields().getEmail())) {
            view.showMessage("Invalid email format!");
            return false;
        }
        return true;
    }
}