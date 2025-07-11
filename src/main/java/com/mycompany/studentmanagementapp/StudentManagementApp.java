package com.mycompany.studentmanagementapp;

import controller.*;
import dao.*;
import view.*;
import model.UserSession;

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
            PersonalInfoView personalInfoView = new PersonalInfoView();

            // Add tabs to MainView
            mainView.addTab("Phòng ban", departmentView);
            mainView.addTab("Giáo viên", teacherView);
            mainView.addTab("Sinh viên", studentView);
            mainView.addTab("Lớp học", classView);
            mainView.addTab("Môn học", courseView);
            mainView.addTab("Điểm số", gradeView);
            mainView.addTab("Thông tin cá nhân", personalInfoView);

            // Initialize Controllers
            LoginController loginController = new LoginController(loginView, accountDAO, mainView);
            new DepartmentController(departmentView, departmentDAO);
            new TeacherController(teacherView, teacherDAO);
            new StudentController(studentView, studentDAO);
            new ClassController(classView, classDAO);
            new CourseController(courseView, courseDAO);
            new GradeController(gradeView, gradeDAO);
            new PersonalInfoController(personalInfoView, accountDAO, studentDAO, teacherDAO);

            // Set logout listener
            mainView.setLogoutButtonListener(e -> {
                // Clear user session
                UserSession.getInstance().logout();
                
                // Update UI
                mainView.updateUserInfo();
                mainView.setVisible(false);
                loginView.setVisible(true);
                
                // Clear login form
                loginView.clearFields();
            });

            // Show Login View
            loginView.setVisible(true);
        });
    }
}