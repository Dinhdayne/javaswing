package com.mycompany.studentmanagementapp;

import controller.*;
import dao.*;
import view.*;

import javax.swing.*;

public class StudentManagementApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize DAOs
            AccountDAO accountDAO = new AccountDAO();
            DepartmentDAO departmentDAO = new DepartmentDAO();
            TeacherDAO teacherDAO = new TeacherDAO();
            StudentDAO studentDAO = new StudentDAO();
            ClassDAO classDAO = new ClassDAO();
            CourseDAO courseDAO = new CourseDAO();
            GradeDAO gradeDAO = new GradeDAO();

            // Initialize Views
            LoginView loginView = new LoginView();
            MainView mainView = new MainView();
            DepartmentView departmentView = new DepartmentView();
            TeacherView teacherView = new TeacherView();
            StudentView studentView = new StudentView();
            ClassView classView = new ClassView();
            CourseView courseView = new CourseView();
            GradeView gradeView = new GradeView();

            // Add tabs to MainView
            mainView.addTab("Departments", departmentView);
            mainView.addTab("Teachers", teacherView);
            mainView.addTab("Students", studentView);
            mainView.addTab("Classes", classView);
            mainView.addTab("Courses", courseView);
            mainView.addTab("Grades", gradeView);

            // Initialize Controllers
            new LoginController(loginView, accountDAO, mainView);
            new DepartmentController(departmentView, departmentDAO);
            new TeacherController(teacherView, teacherDAO);
            new StudentController(studentView, studentDAO);
            new ClassController(classView, classDAO);
            new CourseController(courseView, courseDAO);
            new GradeController(gradeView, gradeDAO);

            // Set logout listener
            mainView.setLogoutButtonListener(e -> {
                mainView.setVisible(false);
                loginView.setVisible(true);
            });

            // Show Login View
            loginView.setVisible(true);
        });
    }
}