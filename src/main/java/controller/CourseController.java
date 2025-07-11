package controller;

import dao.CourseDAO;
import model.Course;
import view.CourseView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class CourseController {
    private CourseView view;
    private CourseDAO model;

    public CourseController(CourseView view, CourseDAO model) {
        this.view = view;
        this.model = model;

        loadCourses();
        view.setAddButtonListener(e -> addCourse());
        view.setUpdateButtonListener(e -> updateCourse());
        view.setDeleteButtonListener(e -> deleteCourse());
        view.setSearchButtonListener(e -> searchCourse());
    }

    private void loadCourses() {
        try {
            List<Course> courses = model.getAllCourses();
            view.updateTable(courses);
        } catch (SQLException e) {
            view.showMessage("Error loading courses: " + e.getMessage());
        }
    }

    private void addCourse() {
        if (!validateFields()) return;
        try {
            Course course = view.getCourseFromFields();
            model.addCourse(course);
            loadCourses();
            view.clearFields();
            view.showMessage("Course added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding course: " + e.getMessage());
        }
    }

    private void updateCourse() {
        if (!validateFields()) return;
        try {
            Course course = view.getCourseFromFields();
            model.updateCourse(course);
            loadCourses();
            view.clearFields();
            view.showMessage("Course updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating course: " + e.getMessage());
        }
    }

    private void deleteCourse() {
        String courseId = view.getSelectedCourseId();
        if (courseId != null) {
            try {
                model.deleteCourse(courseId);
                loadCourses();
                view.clearFields();
                view.showMessage("Course deleted successfully!");
            } catch (SQLException e) {
                view.showMessage("Error deleting course: " + e.getMessage());
            }
        } else {
            view.showMessage("Please select a course to delete!");
        }
    }

    private void searchCourse() {
        String searchText = view.getSearchText();
        try {
            List<Course> courses = model.getAllCourses();
            if (!searchText.isEmpty()) {
                courses = courses.stream()
                    .filter(c -> c.getCourseName().toLowerCase().contains(searchText.toLowerCase()) ||
                                 c.getCourseId().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            view.updateTable(courses);
        } catch (SQLException e) {
            view.showMessage("Error searching courses: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        try {
            Course course = view.getCourseFromFields();
            if (course.getCourseId().isEmpty() || course.getCourseName().isEmpty()) {
                view.showMessage("Course ID and Course Name are required!");
                return false;
            }
            if (course.getCredits() <= 0) {
                view.showMessage("Credits must be a positive number!");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            view.showMessage("Invalid number format for Credits!");
            return false;
        }
    }
}