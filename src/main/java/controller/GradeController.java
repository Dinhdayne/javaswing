package controller;

import dao.GradeDAO;
import model.Grade;
import view.GradeView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class GradeController {
    private GradeView view;
    private GradeDAO model;

    public GradeController(GradeView view, GradeDAO model) {
        this.view = view;
        this.model = model;

        loadGrades();
        view.setAddButtonListener(e -> addGrade());
        view.setUpdateButtonListener(e -> updateGrade());
        view.setDeleteButtonListener(e -> deleteGrade());
        view.setSearchButtonListener(e -> searchGrade());
    }

    private void loadGrades() {
        try {
            List<Grade> grades = model.getAllGrades();
            view.updateTable(grades);
        } catch (SQLException e) {
            view.showMessage("Error loading grades: " + e.getMessage());
        }
    }

    private void addGrade() {
        if (!validateFields()) return;
        try {
            Grade grade = view.getGradeFromFields();
            model.addGrade(grade);
            loadGrades();
            view.clearFields();
            view.showMessage("Grade added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding grade: " + e.getMessage());
        }
    }

    private void updateGrade() {
        if (!validateFields()) return;
        try {
            Grade grade = view.getGradeFromFields();
            model.updateGrade(grade);
            loadGrades();
            view.clearFields();
            view.showMessage("Grade updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating grade: " + e.getMessage());
        }
    }

    private void deleteGrade() {
        int gradeId = view.getSelectedGradeId();
        if (gradeId != -1) {
            try {
                model.deleteGrade(gradeId);
                loadGrades();
                view.clearFields();
                view.showMessage("Grade deleted successfully!");
            } catch (SQLException e) {
                view.showMessage("Error deleting grade: " + e.getMessage());
            }
        } else {
            view.showMessage("Please select a grade to delete!");
        }
    }

    private void searchGrade() {
        String searchText = view.getSearchText();
        try {
            List<Grade> grades = model.getAllGrades();
            if (!searchText.isEmpty()) {
                grades = grades.stream()
                    .filter(g -> g.getStudentId().toLowerCase().contains(searchText.toLowerCase()) ||
                                 g.getCourseId().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            view.updateTable(grades);
        } catch (SQLException e) {
            view.showMessage("Error searching grades: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        try {
            Grade grade = view.getGradeFromFields();
            if (grade.getStudentId().isEmpty() || grade.getCourseId().isEmpty()) {
                view.showMessage("Student ID and Course ID are required!");
                return false;
            }
            if (grade.getMidtermGrade() < 0 || grade.getMidtermGrade() > 10 ||
                grade.getFinalGrade() < 0 || grade.getFinalGrade() > 10 ||
                grade.getOverallGrade() < 0 || grade.getOverallGrade() > 10) {
                view.showMessage("Grades must be between 0 and 10!");
                return false;
            }
            if (!grade.getStatus().matches("Pass|Fail|Pending")) {
                view.showMessage("Status must be Pass, Fail, or Pending!");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            view.showMessage("Invalid number format for grades!");
            return false;
        }
    }
}