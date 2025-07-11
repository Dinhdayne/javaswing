# HƯỚNG DẪN KIỂM TRA KHẨN CẤP - LỖ HỔNG BẢO MẬT ĐIỂM SỐ

## 1. TÌNH TRẠNG HIỆN TẠI
- ✅ **ĐÃ FIX**: Thêm logic mapping `Account.userId` → `Student.studentId` 
- ✅ **ĐÃ THÊM**: Debug logging để theo dõi quá trình lọc dữ liệu
- ✅ **ĐÃ CẢI THIỆN**: Fail-safe mechanism (hiển thị rỗng nếu không tìm được mapping)

## 2. KIỂM TRA NGAY LẬP TỨC

### Bước 1: Compile và chạy ứng dụng
```bash
# Trong thư mục root của project
javac -cp ".:lib/*" src/main/java/**/*.java
java -cp ".:src/main/java:lib/*" MainClass
```

### Bước 2: Test với tài khoản sinh viên
1. **Đăng nhập** với tài khoản sinh viên (ví dụ: `student001`)
2. **Chuyển đến tab "Điểm số"**
3. **Kiểm tra console output** để xem debug log:
   ```
   === DEBUG GRADE LOADING ===
   Current User ID: student001
   Current Role: Student
   Total grades before filtering: 11
   Student ID mapped from User ID: ST001
   Grade 1 - Student ID: ST001 matches ST001: true
   Grade 2 - Student ID: ST001 matches ST001: true
   ...
   Total grades after filtering: 2
   === END DEBUG ===
   ```
4. **Xác minh**: Chỉ thấy điểm của sinh viên đó (ví dụ: chỉ thấy điểm của ST001)

### Bước 3: Test với nhiều tài khoản sinh viên khác nhau
- Đăng nhập với `student002` → chỉ thấy điểm của ST002
- Đăng nhập với `student003` → chỉ thấy điểm của ST003

## 3. DẤU HIỆU LỖI VẪN TỒN TẠI

### 🚨 CẢNH BÁO: Nếu thấy trong console log:
```
WARNING: Could not find studentId for userId: student001
Total grades after filtering: 0
```
➜ **Nguyên nhân**: Mapping `userId` → `studentId` thất bại

### 🚨 CẢNH BÁO: Nếu thấy trong console log:
```
Total grades after filtering: 11
```
(Với số lượng = tổng số điểm trong hệ thống)
➜ **Nguyên nhân**: Logic lọc không hoạt động

## 4. CÁC KỊCH BẢN KIỂM TRA NÂNG CAO

### Test Case 1: Direct Mapping
```
Account.userId = "ST001"
Student.studentId = "ST001"
Expected Result: ✅ Match thành công
```

### Test Case 2: Pattern Mapping  
```
Account.userId = "student001"
Student.studentId = "ST001"
Expected Result: ✅ Match thành công (qua pattern mapping)
```

### Test Case 3: No Mapping
```
Account.userId = "unknown_user"
Student.studentId = "ST001"
Expected Result: ⚠️ Hiển thị 0 điểm (fail-safe)
```

### Test Case 4: Teacher Access
```
Teacher với teacherId = "T001"
Course với teacherId = "T001"
Expected Result: ✅ Thấy điểm của môn học mình dạy
```

### Test Case 5: Admin Access
```
Admin role
Expected Result: ✅ Thấy tất cả điểm số
```

## 5. KIỂM TRA DATABASE

### Kiểm tra dữ liệu Account:
```sql
SELECT username, role, userId FROM accounts WHERE role = 'Student';
```

### Kiểm tra dữ liệu Student:
```sql
SELECT studentId, name FROM students;
```

### Kiểm tra dữ liệu Grade:
```sql
SELECT gradeId, studentId, courseId FROM grades;
```

### Xác minh mapping:
```sql
SELECT a.username, a.userId, s.studentId, s.name 
FROM accounts a 
LEFT JOIN students s ON (a.userId = s.studentId OR a.userId = CONCAT('student', SUBSTRING(s.studentId, 3)))
WHERE a.role = 'Student';
```

## 6. THAM SỐ CẦN ĐIỀU CHỈNH

### Nếu mapping pattern khác:
```java
// Trong getStudentIdFromUserId(), thay đổi logic:
if (userId.startsWith("student")) {
    String numericPart = userId.substring(7); // Remove "student" prefix
    String studentIdPattern = "ST" + String.format("%03d", Integer.parseInt(numericPart));
    // ...
}
```

### Nếu format Account.userId khác:
- Cập nhật logic mapping trong phương thức `getStudentIdFromUserId()`
- Thêm case xử lý cho format mới

## 7. ROLLBACK PLAN

### Nếu fix gây lỗi khác:
1. **Backup hiện tại**: Copy file `GradeController.java` 
2. **Revert changes**: Khôi phục version trước
3. **Temporary fix**: Tạm thời disable Grade tab cho Student
   ```java
   if ("Student".equals(role)) {
       view.updateTable(List.of()); // Show empty table
   }
   ```

## 8. MONITORING TIẾP THEO

### Cần theo dõi:
- [ ] Performance impact của việc query StudentDAO
- [ ] Memory usage khi load all students
- [ ] Database connection pooling
- [ ] Log file size (do debug logging)

### Optimization cho production:
1. **Cache student mapping** để giảm database queries
2. **Remove debug logging** sau khi confirmed fix works
3. **Add performance metrics** cho security checks

## 9. THÔNG BÁO CHO USERS

### Nội dung thông báo:
```
THÔNG BÁO BẢO TRÌ HỆ THỐNG

Hệ thống đã được cập nhật để cải thiện bảo mật dữ liệu điểm số.

Từ nay:
✅ Sinh viên chỉ có thể xem điểm số của mình
✅ Giáo viên chỉ có thể xem điểm môn học mình dạy  
✅ Dữ liệu cá nhân được bảo vệ tốt hơn

Nếu gặp vấn đề, vui lòng liên hệ IT Support.
```

## 10. SUCCESS CRITERIA

### ✅ Fix được coi là thành công khi:
1. Sinh viên chỉ thấy điểm của mình (0-2 điểm max thay vì 11 điểm)
2. Debug log cho thấy đúng mapping: `student001` → `ST001`
3. Không có exception hoặc crash
4. UI vẫn hoạt động bình thường cho Admin và Teacher
5. Search function vẫn hoạt động đúng với filtered data

### 🔄 Nếu chưa thành công:
1. Check database schema và sample data
2. Verify Account.userId format  
3. Verify Student.studentId format
4. Update mapping logic accordingly
5. Re-run tests

---

**⚠️ LƯU Ý QUAN TRỌNG**: Đây là critical security fix. Phải test kỹ lưỡng trước khi deploy lên production!