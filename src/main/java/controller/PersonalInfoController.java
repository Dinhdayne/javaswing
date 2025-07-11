package controller;

import dao.AccountDAO;
import dao.StudentDAO;
import dao.TeacherDAO;
import model.Account;
import model.Student;
import model.Teacher;
import model.UserSession;
import view.PersonalInfoView;

import java.sql.SQLException;

public class PersonalInfoController {
    private PersonalInfoView view;
    private AccountDAO accountDAO;
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    private UserSession userSession;

    public PersonalInfoController(PersonalInfoView view, AccountDAO accountDAO, 
                                 StudentDAO studentDAO, TeacherDAO teacherDAO) {
        this.view = view;
        this.accountDAO = accountDAO;
        this.studentDAO = studentDAO;
        this.teacherDAO = teacherDAO;
        this.userSession = UserSession.getInstance();
        
        initializeEventListeners();
        loadPersonalInfo();
    }

    private void initializeEventListeners() {
        view.setUpdateInfoButtonListener(e -> updatePersonalInfo());
        view.setChangePasswordButtonListener(e -> changePassword());
        view.setCancelButtonListener(e -> loadPersonalInfo());
    }

    private void loadPersonalInfo() {
        try {
            Account account = userSession.getCurrentAccount();
            if (account == null) {
                view.showErrorMessage("Không tìm thấy thông tin tài khoản!");
                return;
            }

            // Load account information
            view.setUsername(account.getUsername());
            view.setRole(account.getRole());
            view.setUserId(account.getUserId());

            // Load personal information based on role
            String role = account.getRole();
            String userId = account.getUserId();

            if ("Student".equals(role)) {
                loadStudentInfo(userId);
            } else if ("Teacher".equals(role)) {
                loadTeacherInfo(userId);
            } else if ("Admin".equals(role)) {
                // For admin, we might not have detailed personal info
                // You can extend this based on your requirements
                view.setName("Administrator");
                view.setEmail("admin@school.com");
            }

        } catch (SQLException e) {
            view.showErrorMessage("Lỗi khi tải thông tin: " + e.getMessage());
        }
    }

    private void loadStudentInfo(String studentId) throws SQLException {
        Student student = studentDAO.getStudentById(studentId);
        if (student != null) {
            view.setName(student.getName());
            view.setEmail(student.getEmail());
            view.setPhone(student.getPhone());
            view.setAddress(student.getAddress());
            view.setDateOfBirth(student.getDateOfBirth());
            view.setGender(student.getGender());
        }
    }

    private void loadTeacherInfo(String teacherId) throws SQLException {
        Teacher teacher = teacherDAO.getTeacherById(teacherId);
        if (teacher != null) {
            view.setName(teacher.getName());
            view.setEmail(teacher.getEmail());
            view.setPhone(teacher.getPhone());
            view.setAddress(teacher.getAddress());
            view.setDateOfBirth(teacher.getDateOfBirth());
            view.setGender(teacher.getGender());
        }
    }

    private void updatePersonalInfo() {
        try {
            // Check permission
            if (!AuthorizationService.hasPermission(AuthorizationService.Permission.EDIT_OWN_PROFILE)) {
                view.showErrorMessage("Bạn không có quyền chỉnh sửa thông tin cá nhân!");
                return;
            }

            Account account = userSession.getCurrentAccount();
            String role = account.getRole();
            String userId = account.getUserId();

            if ("Student".equals(role)) {
                updateStudentInfo(userId);
            } else if ("Teacher".equals(role)) {
                updateTeacherInfo(userId);
            }

            view.showMessage("Cập nhật thông tin thành công!");

        } catch (SQLException e) {
            view.showErrorMessage("Lỗi khi cập nhật thông tin: " + e.getMessage());
        }
    }

    private void updateStudentInfo(String studentId) throws SQLException {
        Student student = new Student(
            studentId,
            view.getName(),
            view.getDateOfBirth(),
            view.getGender(),
            view.getEmail(),
            view.getPhone(),
            view.getAddress(),
            "", // classId - keep existing
            "" // enrollmentDate - keep existing
        );

        // Get existing student to preserve classId and enrollmentDate
        Student existingStudent = studentDAO.getStudentById(studentId);
        if (existingStudent != null) {
            student.setClassId(existingStudent.getClassId());
            student.setEnrollmentDate(existingStudent.getEnrollmentDate());
        }

        studentDAO.updateStudent(student);
    }

    private void updateTeacherInfo(String teacherId) throws SQLException {
        Teacher teacher = new Teacher(
            teacherId,
            view.getName(),
            view.getDateOfBirth(),
            view.getGender(),
            view.getEmail(),
            view.getPhone(),
            view.getAddress(),
            "", // departmentId - keep existing
            "" // hireDate - keep existing
        );

        // Get existing teacher to preserve departmentId and hireDate
        Teacher existingTeacher = teacherDAO.getTeacherById(teacherId);
        if (existingTeacher != null) {
            teacher.setDepartmentId(existingTeacher.getDepartmentId());
            teacher.setHireDate(existingTeacher.getHireDate());
        }

        teacherDAO.updateTeacher(teacher);
    }

    private void changePassword() {
        try {
            // Check permission
            if (!AuthorizationService.hasPermission(AuthorizationService.Permission.CHANGE_PASSWORD)) {
                view.showErrorMessage("Bạn không có quyền thay đổi mật khẩu!");
                return;
            }

            String currentPassword = view.getCurrentPassword();
            String newPassword = view.getNewPassword();
            String confirmPassword = view.getConfirmPassword();

            // Validate input
            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                view.showErrorMessage("Vui lòng điền đầy đủ thông tin!");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                view.showErrorMessage("Mật khẩu mới và xác nhận mật khẩu không khớp!");
                return;
            }

            if (newPassword.length() < 6) {
                view.showErrorMessage("Mật khẩu mới phải có ít nhất 6 ký tự!");
                return;
            }

            // Verify current password
            String username = userSession.getCurrentUsername();
            if (!accountDAO.verifyCurrentPassword(username, currentPassword)) {
                view.showErrorMessage("Mật khẩu hiện tại không đúng!");
                return;
            }

            // Update password
            if (accountDAO.updatePassword(username, newPassword)) {
                view.showMessage("Đổi mật khẩu thành công!");
                view.clearPasswordFields();
            } else {
                view.showErrorMessage("Lỗi khi đổi mật khẩu!");
            }

        } catch (SQLException e) {
            view.showErrorMessage("Lỗi khi đổi mật khẩu: " + e.getMessage());
        }
    }
}