/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.ClassDAO;
import model.Class;
import view.ClassView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Admin
 */
public class ClassController {
    private ClassView view;
    private ClassDAO model;

    public ClassController(ClassView view, ClassDAO model) {
        this.view = view;
        this.model = model;

        loadClasses();
        view.setAddButtonListener(e -> addClass());
        view.setUpdateButtonListener(e -> updateClass());
        view.setDeleteButtonListener(e -> deleteClass());
        view.setSearchButtonListener(e -> searchClass());
    }

    private void loadClasses() {
        try {
            List<Class> classes = model.getAllClasses();
            view.updateTable(classes);
        } catch (SQLException e) {
            view.showMessage("Error loading classes: " + e.getMessage());
        }
    }

    private void addClass() {
        if (!validateFields()) return;
        try {
            Class cls = view.getClassFromFields();
            model.addClass(cls);
            loadClasses();
            view.clearFields();
            view.showMessage("Class added successfully!");
        } catch (SQLException e) {
            view.showMessage("Error adding class: " + e.getMessage());
        }
    }

    private void updateClass() {
        if (!validateFields()) return;
        try {
            Class cls = view.getClassFromFields();
            model.updateClass(cls);
            loadClasses();
            view.clearFields();
            view.showMessage("Class updated successfully!");
        } catch (SQLException e) {
            view.showMessage("Error updating class: " + e.getMessage());
        }
    }

    private void deleteClass() {
        String classId = view.getSelectedClassId();
        if (classId != null) {
            try {
                model.deleteClass(classId);
                loadClasses();
                view.clearFields();
                view.showMessage("Class deleted successfully!");
            } catch (SQLException e) {
                view.showMessage("Error deleting class: " + e.getMessage());
            }
        } else {
            view.showMessage("Please select a class to delete!");
        }
    }

    private void searchClass() {
        String searchText = view.getSearchText();
        try {
            List<Class> classes = model.getAllClasses();
            if (!searchText.isEmpty()) {
                classes = classes.stream()
                    .filter(c -> c.getClassName().toLowerCase().contains(searchText.toLowerCase()) ||
                                 c.getClassId().toLowerCase().contains(searchText.toLowerCase()))
                    .toList();
            }
            view.updateTable(classes);
        } catch (SQLException e) {
            view.showMessage("Error searching classes: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        Class cls = view.getClassFromFields();
        if (cls.getClassId().isEmpty() || cls.getClassName().isEmpty()) {
            view.showMessage("Class ID and Class Name are required!");
            return false;
        }
        return true;
    }
    
}
