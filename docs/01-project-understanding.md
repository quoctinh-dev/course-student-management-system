# 01. TỔNG QUAN DỰ ÁN

## 1. Giới thiệu dự án

### Tên dự án

Hệ thống Quản lý Khóa học và Học viên (Course Management System)

### Mô tả

Đây là ứng dụng Console được xây dựng bằng Java và JDBC, sử dụng PostgreSQL làm hệ quản trị cơ sở dữ liệu.

Hệ thống hỗ trợ quản lý hoạt động đào tạo giữa quản trị viên và học viên, cho phép thực hiện các chức năng quản lý khóa học, quản lý học viên, đăng ký khóa học và thống kê dữ liệu.

### Mục tiêu

* Quản lý thông tin khóa học.
* Quản lý thông tin học viên.
* Quản lý việc đăng ký khóa học.
* Phân quyền người dùng theo vai trò.
* Thống kê dữ liệu phục vụ quản lý đào tạo.

---

## 2. Đối tượng sử dụng

Hệ thống có hai nhóm người dùng chính.

### 2.1 Quản trị viên (Admin)

Quản trị viên là người có quyền quản lý toàn bộ hệ thống.

Các công việc chính:

* Đăng nhập hệ thống.
* Quản lý khóa học.
* Quản lý học viên.
* Quản lý đăng ký khóa học.
* Xem thống kê.
* Đăng xuất.

### 2.2 Học viên (Student)

Học viên sử dụng hệ thống để tham gia các khóa học.

Các công việc chính:

* Đăng nhập hệ thống.
* Xem danh sách khóa học.
* Đăng ký khóa học.
* Xem danh sách khóa học đã đăng ký.
* Hủy đăng ký (nếu chưa được xác nhận).
* Đổi mật khẩu.
* Đăng xuất.

---

## 3. Các nhóm chức năng chính

Dựa trên tài liệu đặc tả và khung chức năng, hệ thống được chia thành 5 module chính.

### 3.1 Xác thực người dùng (Authentication)

Chức năng:

* Đăng nhập Admin.
* Đăng nhập Student.
* Kiểm tra thông tin đăng nhập.
* Phân quyền người dùng.
* Đăng xuất.

---

### 3.2 Quản lý khóa học (Course Management)

Chức năng:

* Xem danh sách khóa học.
* Thêm khóa học mới.
* Chỉnh sửa thông tin khóa học.
* Xóa khóa học.
* Tìm kiếm khóa học.
* Sắp xếp khóa học.

---

### 3.3 Quản lý học viên (Student Management)

Chức năng:

* Xem danh sách học viên.
* Thêm học viên.
* Chỉnh sửa học viên.
* Xóa học viên.
* Tìm kiếm học viên.
* Sắp xếp học viên.

---

### 3.4 Quản lý đăng ký khóa học (Enrollment Management)

Chức năng:

* Đăng ký khóa học.
* Duyệt đăng ký khóa học.
* Hủy đăng ký khóa học.
* Xem học viên theo khóa học.
* Kiểm tra đăng ký trùng lặp.

---

### 3.5 Thống kê (Statistics)

Chức năng:

* Thống kê tổng số khóa học.
* Thống kê tổng số học viên.
* Thống kê số học viên theo từng khóa học.
* Thống kê top khóa học có nhiều học viên.
* Liệt kê khóa học có trên 10 học viên.

---

## 4. Các thực thể chính trong hệ thống

Hệ thống quản lý 4 thực thể chính.

### 4.1 Admin

Lưu thông tin tài khoản quản trị viên.

Vai trò:

* Xác thực đăng nhập.
* Quản lý hệ thống.

---

### 4.2 Student

Lưu thông tin học viên.

Vai trò:

* Quản lý hồ sơ học viên.
* Tham gia đăng ký khóa học.

---

### 4.3 Course

Lưu thông tin khóa học.

Vai trò:

* Quản lý các chương trình đào tạo.

---

### 4.4 Enrollment

Lưu thông tin đăng ký khóa học.

Vai trò:

* Liên kết Student và Course.
* Theo dõi trạng thái đăng ký học.

---

## 5. Quan hệ giữa các thực thể

Student
│
│ 1 - N
│
Enrollment
│
│ N - 1
│
Course

Ý nghĩa:

* Một học viên có thể đăng ký nhiều khóa học.
* Một khóa học có thể có nhiều học viên.
* Enrollment là bảng trung gian quản lý quan hệ nhiều - nhiều giữa Student và Course.

---

## 6. Luồng hoạt động tổng quát

Khởi động chương trình

↓

Màn hình đăng nhập

↓

Chọn vai trò người dùng

├── Admin
│
├── Quản lý khóa học
├── Quản lý học viên
├── Quản lý đăng ký khóa học
├── Thống kê
└── Đăng xuất

└── Student

├── Xem khóa học
├── Đăng ký khóa học
├── Xem khóa học đã đăng ký
├── Hủy đăng ký
├── Đổi mật khẩu
└── Đăng xuất

---

## 7. Mục tiêu học tập của đồ án

Thông qua dự án này, sinh viên cần vận dụng:

### Core Java

* Lập trình hướng đối tượng (OOP).
* Collections Framework.
* Exception Handling.
* Validation dữ liệu đầu vào.

### JDBC

* Kết nối cơ sở dữ liệu.
* Thao tác CRUD.
* PreparedStatement.
* ResultSet.

### PostgreSQL

* Thiết kế cơ sở dữ liệu quan hệ.
* Primary Key.
* Foreign Key.
* JOIN.
* Aggregate Functions.

### Thiết kế phần mềm

* Mô hình phân lớp.
* DAO Pattern.
* Business Layer.
* Presentation Layer.

---

## 8. Phạm vi dự án

### Bao gồm

* Đăng nhập hệ thống.
* Quản lý khóa học.
* Quản lý học viên.
* Quản lý đăng ký khóa học.
* Thống kê dữ liệu.

### Không bao gồm

* Web Application.
* REST API.
* Spring Framework.
* Mobile Application.
* Cloud Deployment.

Dự án tập trung vào việc rèn luyện kỹ năng Java Core, JDBC, PostgreSQL và tư duy thiết kế phần mềm theo mô hình phân lớp.
