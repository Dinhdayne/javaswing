# PHÂN TÍCH LỖ HỔNG BẢO MẬT - HỆ THỐNG QUẢN LÝ SINH VIÊN

## 1. VẤN ĐỀ PHÁT HIỆN

### Mô tả lỗ hổng:
Sinh viên có thể xem điểm số của các sinh viên khác mặc dù hệ thống đã được thiết kế với cơ chế phân quyền chi tiết.

### Bằng chứng:
- Ảnh chụp màn hình cho thấy sinh viên `student001` (hiển thị ở phía dưới) có thể xem điểm của nhiều sinh viên khác nhau (ST001, ST002, ST003, ST004, ST005)
- Điều này vi phạm quy tắc bảo mật đã được định nghĩa trong `AUTHORIZATION_SYSTEM.md`

## 2. PHÂN TÍCH MÃ NGUỒN

### 2.1 Logic Phân Quyền Đã Triển Khai

Trong `GradeController.java` (dòng 65-89), phương thức `loadGrades()` có logic lọc theo vai trò:

```java
if ("Student".equals(role)) {
    // Students can only see their own grades
    grades = grades.stream()
        .filter(g -> g.getStudentId().equals(currentUserId))
        .toList();
}
```

### 2.2 Hệ Thống Phân Quyền

Trong `AuthorizationService.java`, sinh viên chỉ có quyền `VIEW_OWN_GRADES`:

```java
private static boolean hasStudentPermission(Permission permission) {
    switch (permission) {
        case VIEW_OWN_GRADES:
            return true;
        default:
            return false;
    }
}
```

## 3. NGUYÊN NHÂN CÓ THỂ

### 3.1 Vấn đề về UserSession
- `UserSession.getInstance().getCurrentUserId()` có thể trả về giá trị không chính xác
- Phiên đăng nhập có thể không được khởi tạo đúng cách

### 3.2 Vấn đề về Mapping dữ liệu
- `studentId` trong bảng Grade có thể không khớp với `userId` trong UserSession
- Cơ chế mapping giữa tài khoản đăng nhập và ID sinh viên có thể có vấn đề

### 3.3 Vấn đề về thời điểm tải dữ liệu
- Dữ liệu có thể được tải trước khi phiên đăng nhập được thiết lập đầy đủ
- Cache dữ liệu có thể chưa được làm mới sau khi đăng nhập

### 3.4 Vấn đề về UI Initialization
- `GradeView` có thể hiển thị dữ liệu từ session trước đó
- Phương thức `configureUIForRole()` có thể chưa được gọi hoặc không hiệu quả

## 4. CÁC KỊCH BẢN KIỂM THỬ ĐỀ XUẤT

### 4.1 Kiểm tra UserSession
```java
// Kiểm tra giá trị UserSession khi đăng nhập
System.out.println("Current User ID: " + UserSession.getInstance().getCurrentUserId());
System.out.println("Current Role: " + UserSession.getInstance().getCurrentRole());
```

### 4.2 Kiểm tra logic lọc dữ liệu
```java
// Thêm log trong loadGrades() để kiểm tra
System.out.println("Role: " + role);
System.out.println("Current User ID: " + currentUserId);
System.out.println("Grades before filtering: " + grades.size());
// ... sau khi lọc
System.out.println("Grades after filtering: " + grades.size());
```

### 4.3 Kiểm tra dữ liệu Grade
```java
// Kiểm tra studentId trong các record Grade
for (Grade grade : grades) {
    System.out.println("Grade ID: " + grade.getGradeId() + 
                      ", Student ID: " + grade.getStudentId());
}
```

## 5. KHUYẾN NGHỊ KHẮC PHỤC

### 5.1 Khắc phục ngay lập tức
1. **Thêm logging chi tiết** trong `GradeController.loadGrades()`
2. **Kiểm tra UserSession initialization** trong `LoginController`
3. **Xác minh mapping** giữa Account.userId và Student.studentId

### 5.2 Cải thiện bảo mật dài hạn
1. **Double-check authorization** ở tầng DAO
2. **Implement data encryption** cho dữ liệu nhạy cảm
3. **Thêm audit logging** cho tất cả truy cập dữ liệu
4. **Regular security testing** với các kịch bản khác nhau

### 5.3 Cải thiện thiết kế
1. **Separation of concerns**: Tách biệt hoàn toàn logic authorization
2. **Defensive programming**: Luôn assume dữ liệu có thể bị compromise
3. **Fail-safe defaults**: Mặc định từ chối truy cập nếu không chắc chắn

## 6. MỨC ĐỘ NGHIÊM TRỌNG

### Mức độ: **CRITICAL** 🔴
- **Impact**: Vi phạm quyền riêng tư sinh viên
- **Risk**: Có thể dẫn đến rò rỉ thông tin học tập
- **Compliance**: Vi phạm các quy định bảo vệ dữ liệu cá nhân

### Ưu tiên khắc phục: **URGENT**
- Cần được khắc phục ngay lập tức
- Có thể cần tạm ngưng hệ thống cho đến khi được fix

## 7. TIMELINE KHẮC PHỤC ĐỀ XUẤT

1. **Ngay lập tức (0-2 giờ)**: 
   - Thêm logging để xác định nguyên nhân
   - Kiểm tra và fix UserSession issues

2. **Trong ngày (2-8 giờ)**:
   - Implement và test các fix
   - Verify authorization logic

3. **Trong tuần (1-3 ngày)**:
   - Comprehensive security review
   - Additional testing và monitoring

## 8. KẾT LUẬN

Đây là một lỗ hổng bảo mật nghiêm trọng cần được ưu tiên khắc phục. Mặc dù hệ thống đã có thiết kế phân quyền tốt, nhưng việc triển khai có vấn đề dẫn đến vi phạm nguyên tắc "students can only view their own grades".

Việc khắc phục cần được thực hiện cẩn thận với đầy đủ testing để đảm bảo không ảnh hưởng đến các chức năng khác của hệ thống.