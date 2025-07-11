package controller;

import dao.TeacherDAO;
import model.Teacher;
import view.TeacherView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class TeacherController {
    private TeacherView view;
    private TeacherDAO model;

    public TeacherController(TeacherView view, TeacherDAO model) {
        this.view = view;
        this.model = model;

        loadTeachers();
        view.setAddButtonListener(e -> addTeacher());
        view.setUpdateButtonListener(e -> updateTeacher());
        view.setDeleteButtonListener(e -> deleteTeacher());
        view.setSearchButtonListener(e -> searchTeacher());
    }

    private void loadTeachers() {
        try {
            List<Teacher> teachers = model.getAllTeachers();
            view.updateTable(teachers);
        } catch (SQLException e) {
            view.showMessage("Error loading teachers: " + e.getMessage());
        }
    }

    private void addTeacher() {
        if (!validateFields()) return;
        try {
            Teacher teacher = view.getTeacherFromFields();
            model.addTeacher(teacher);
            loadTeachers();
            view.clearFields();
            view.showMessage("Teacher added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding teacher: " + e.getMessage());
        }
    }

    private void updateTeacher() {
        if (!validateFields()) return;
        try {
            Teacher teacher = view.getTeacherFromFields();
            model.updateTeacher(teacher);
            loadTeachers();
            view.clearFields();
            view.showMessage("Teacher updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating teacher: " + e.getMessage());
        }
    }

    private void deleteTeacher() {
        String teacherId = view.getSelectedTeacherId();
        if (teacherId != null) {
            try {
                model.deleteTeacher(teacherId);
                loadTeachers();
                view.clearFields();
                view.showMessage("Teacher deleted successfully!");
            } catch (SQLException e) {
                view.showMessage("Error deleting teacher: " + e.getMessage());
            }
        } else {
            view.showMessage("Please select a teacher to delete!");
        }
    }

    private void searchTeacher() {
        String searchText = view.getSearchText();
        try {
            List<Teacher> teachers = model.getAllTeachers();
            if (!searchText.isEmpty()) {
                teachers = teachers.stream()
                    .filter(t -> t.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                                 t.getTeacherId().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            view.updateTable(teachers);
        } catch (SQLException e) {
            view.showMessage("Error searching teachers: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        Teacher teacher = view.getTeacherFromFields();
        if (teacher.getTeacherId().isEmpty() || teacher.getName().isEmpty() || teacher.getEmail().isEmpty()) {
            view.showMessage("Teacher ID, Name, and Email are required!");
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", teacher.getEmail())) {
            view.showMessage("Invalid email format!");
            return false;
        }
        if (teacher.getDateOfBirth() == null) {
            view.showMessage("Date of Birth is required!");
            return false;
        }
        if (!teacher.getGender().matches("Male|Female|Other")) {
            view.showMessage("Gender must be Male, Female, or Other!");
            return false;
        }
        return true;
    }
}