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
  - Xem thông tin sinh viên (tất cả sinh viên)
  - Xem và quản lý lớp học
  - Xem và quản lý môn học
  - Xem và quản lý điểm số
  - Xem và chỉnh sửa thông tin cá nhân
  - Đổi mật khẩu
- **Không thể**:
  - Quản lý phòng ban
  - Thêm/xóa sinh viên

### 3. Student (Sinh viên)
- **Quyền hạn chế nhất**: Chỉ truy cập thông tin cá nhân
- **Có thể**:
  - Xem môn học
  - Xem điểm số cá nhân
  - Xem và chỉnh sửa thông tin cá nhân
  - Đổi mật khẩu
- **Không thể**:
  - Xem thông tin phòng ban
  - Xem thông tin giáo viên
  - Xem thông tin sinh viên khác
  - Quản lý lớp học

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

### 3. PersonalInfoView
- Giao diện quản lý thông tin cá nhân
- Tab "Thông tin cá nhân" và "Đổi mật khẩu"
- Validation dữ liệu đầu vào

### 4. PersonalInfoController
- Xử lý logic quản lý thông tin cá nhân
- Tích hợp với StudentDAO và TeacherDAO
- Kiểm tra quyền hạn trước khi thực hiện thao tác

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
- Sinh viên chỉ thấy thông tin cá nhân của mình
- Giáo viên thấy tất cả sinh viên nhưng không thể xóa
- Admin có quyền truy cập đầy đủ

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

## Mở rộng tương lai

Hệ thống được thiết kế để dễ dàng mở rộng:
- Thêm vai trò mới
- Thêm quyền hạn mới
- Tích hợp với các module khác
- Cải thiện UI/UX