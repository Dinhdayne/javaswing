# Hệ thống Phân quyền và Quản lý Thông tin Cá nhân

## Tổng quan
Hệ thống quản lý sinh viên đã được nâng cấp với một hệ thống phân quyền toàn diện và tính năng quản lý thông tin cá nhân. Các tính năng mới bao gồm:

- **Phân quyền dựa trên vai trò (Role-based Authorization)**
- **Quản lý thông tin cá nhân**
- **Đổi mật khẩu**
- **Kiểm soát truy cập UI động**

## Các Vai trò và Quyền hạn

### 1. Admin (Quản trị viên)
- **Quyền đầy đủ**: Truy cập tất cả các chức năng
- **Có thể**:
  - Xem và quản lý phòng ban
  - Xem và quản lý giáo viên
  - Xem và quản lý sinh viên
  - Xem và quản lý lớp học
  - Xem và quản lý môn học
  - Xem và quản lý điểm số
  - Xem và chỉnh sửa thông tin cá nhân
  - Đổi mật khẩu

### 2. Teacher (Giáo viên)
- **Quyền hạn chế**: Truy cập các chức năng liên quan đến giảng dạy
- **Có thể**:
  - Xem thông tin giáo viên
  - Xem và quản lý sinh viên trong lớp học của mình
  - Xem lớp học của mình
  - Xem và quản lý môn học mà mình dạy
  - Xem và quản lý điểm số cho môn học mà mình dạy
  - Xem và chỉnh sửa thông tin cá nhân
  - Đổi mật khẩu
- **Không thể**:
  - Quản lý phòng ban
  - Xem/quản lý sinh viên ngoài lớp của mình
  - Quản lý môn học của giáo viên khác
  - Quản lý điểm số của môn học không phải mình dạy

### 3. Student (Sinh viên)
- **Quyền hạn chế nhất**: Chỉ truy cập thông tin cá nhân
- **Có thể**:
  - Xem lớp học của mình
  - Xem môn học mà mình đăng ký (có điểm)
  - Xem điểm số cá nhân
  - Xem và chỉnh sửa thông tin cá nhân
  - Đổi mật khẩu
- **Không thể**:
  - Xem thông tin phòng ban
  - Xem thông tin giáo viên
  - Xem thông tin sinh viên khác
  - Quản lý lớp học
  - Quản lý môn học
  - Quản lý điểm số

## Tính năng Quản lý Thông tin Cá nhân

### Tab "Thông tin cá nhân"
Mỗi người dùng có thể truy cập tab "Thông tin cá nhân" để:

#### 1. Xem thông tin tài khoản (chỉ đọc)
- Tên đăng nhập
- Vai trò
- Mã người dùng

#### 2. Chỉnh sửa thông tin cá nhân
- Họ và tên
- Email
- Số điện thoại
- Ngày sinh
- Giới tính
- Địa chỉ

#### 3. Đổi mật khẩu
- Nhập mật khẩu hiện tại
- Nhập mật khẩu mới
- Xác nhận mật khẩu mới
- Validation: Mật khẩu phải có ít nhất 6 ký tự

## Các Thành phần Hệ thống

### 1. UserSession
- Quản lý phiên đăng nhập hiện tại
- Lưu trữ thông tin người dùng đang đăng nhập
- Singleton pattern để truy cập toàn cục

### 2. AuthorizationService
- Kiểm tra quyền hạn dựa trên vai trò
- Cấu hình UI động theo quyền hạn
- Enum Permission để định nghĩa các quyền
- **Quyền mới**: MANAGE_STUDENTS_IN_OWN_CLASS, VIEW_OWN_CLASS, VIEW_OWN_COURSES, MANAGE_OWN_COURSES, MANAGE_GRADES_FOR_OWN_COURSES

### 3. PersonalInfoView
- Giao diện quản lý thông tin cá nhân
- Tab "Thông tin cá nhân" và "Đổi mật khẩu"
- Validation dữ liệu đầu vào

### 4. PersonalInfoController
- Xử lý logic quản lý thông tin cá nhân
- Tích hợp với StudentDAO và TeacherDAO
- Kiểm tra quyền hạn trước khi thực hiện thao tác

### 5. Các Controller với Phân quyền Nâng cao
- **StudentController**: Teacher chỉ quản lý sinh viên trong lớp của mình
- **ClassController**: Student xem lớp của mình, Teacher xem lớp mình dạy
- **CourseController**: Student xem môn đã đăng ký, Teacher xem môn mình dạy
- **GradeController**: Student xem điểm của mình, Teacher quản lý điểm môn mình dạy

## Tính năng Bảo mật

### 1. Kiểm soát truy cập
- Mỗi thao tác được kiểm tra quyền hạn
- UI được cấu hình động theo vai trò
- Buttons bị vô hiệu hóa nếu không có quyền

### 2. Validation dữ liệu
- Kiểm tra tính hợp lệ của email
- Kiểm tra độ dài mật khẩu
- Xác nhận mật khẩu trước khi thay đổi

### 3. Lọc dữ liệu
- **Sinh viên**: Chỉ thấy lớp, môn học và điểm số của mình
- **Giáo viên**: 
  - Chỉ thấy sinh viên trong lớp mình dạy
  - Chỉ thấy lớp học mình phụ trách
  - Chỉ thấy môn học mình giảng dạy
  - Chỉ quản lý điểm số của môn học mình dạy
- **Admin**: Có quyền truy cập đầy đủ tất cả dữ liệu

## Cách sử dụng

### Đăng nhập
1. Nhập tên đăng nhập và mật khẩu
2. Hệ thống sẽ tự động cấu hình UI theo vai trò
3. Thông tin người dùng hiển thị ở góc dưới màn hình

### Quản lý thông tin cá nhân
1. Chọn tab "Thông tin cá nhân"
2. Chỉnh sửa thông tin trong tab "Thông tin cá nhân"
3. Đổi mật khẩu trong tab "Đổi mật khẩu"
4. Nhấn nút "Cập nhật" để lưu thay đổi

### Đăng xuất
1. Nhấn nút "Đăng xuất"
2. Hệ thống sẽ xóa phiên đăng nhập và trở về màn hình đăng nhập

## Lợi ích

### 1. Bảo mật cao
- Kiểm soát truy cập chặt chẽ
- Phân quyền rõ ràng theo vai trò
- Validation dữ liệu đầy đủ

### 2. Trải nghiệm người dùng tốt
- UI thân thiện với người dùng
- Thông báo rõ ràng bằng tiếng Việt
- Cấu hình động theo quyền hạn

### 3. Dễ bảo trì
- Kiến trúc modular
- Separation of concerns
- Extensible design

## Cập nhật Phân quyền Chi tiết

### Thay đổi chính trong hệ thống:

#### 1. Sinh viên (Student)
- **Lớp học**: Chỉ xem được lớp của mình (dựa trên classId trong thông tin sinh viên)
- **Môn học**: Chỉ xem được môn học mà mình đã đăng ký (có điểm số)
- **Điểm số**: Chỉ xem được điểm số của chính mình
- **Không thể**: Thực hiện bất kỳ thao tác thêm/sửa/xóa nào

#### 2. Giáo viên (Teacher) 
- **Sinh viên**: Chỉ quản lý sinh viên trong các lớp mà mình phụ trách (teacherId trong Class)
- **Lớp học**: Chỉ xem các lớp mà mình phụ trách
- **Môn học**: Chỉ quản lý các môn học mà mình giảng dạy (teacherId trong Course)
- **Điểm số**: Chỉ thêm/sửa/xóa điểm của các môn học mà mình dạy

#### 3. Logic kiểm tra quyền hạn
- Mỗi thao tác đều được kiểm tra quyền hạn thông qua AuthorizationService
- Dữ liệu được filter theo userId hiện tại trước khi hiển thị
- Các thao tác thêm/sửa/xóa có kiểm tra bổ sung để đảm bảo chỉ tác động đến dữ liệu thuộc quyền

### Cơ chế hoạt động:

1. **Load dữ liệu**: Filter theo role và userId
2. **Thao tác**: Kiểm tra permission và ownership
3. **UI**: Disable các button không có quyền
4. **Validation**: Double-check trước khi thực hiện thao tác

## Mở rộng tương lai

Hệ thống được thiết kế để dễ dàng mở rộng:
- Thêm vai trò mới
- Thêm quyền hạn mới
- Tích hợp với các module khác
- Cải thiện UI/UX