# 05. KẾ HOẠCH THỰC HIỆN DỰ ÁN (GITHUB PROJECT PLAN)

## 1. Mục tiêu

Tài liệu này dùng để:

* Chia dự án thành các giai đoạn nhỏ.
* Tạo Task trên GitHub Project.
* Theo dõi tiến độ thực hiện.
* Quản lý công việc theo từng buổi học.

---

# 2. Cấu hình GitHub Project

## Board Name

Course Management System Planning

---

## Columns

Backlog

↓

Todo

↓

In Progress

↓

Review

↓

Done

---

# 3. Custom Fields

## Field 1

Tên:

Buổi học

Kiểu:

Single Select

Giá trị:

* Buổi 1
* Buổi 2
* Buổi 3
* Buổi 4
* Buổi 5

---

## Field 2

Tên:

Module

Kiểu:

Single Select

Giá trị:

* Authentication
* Course
* Student
* Enrollment
* Statistics

---

## Field 3

Tên:

Priority

Kiểu:

Single Select

Giá trị:

* High
* Medium
* Low

---

## Field 4

Tên:

Core Java

Kiểu:

Text

Mục đích:

Ghi chú kiến thức được áp dụng trong Task.

Ví dụ:

* OOP
* Collections
* JDBC
* Exception Handling
* Validation

---

# 4. Kế hoạch theo từng buổi

## BUỔI 1 - AUTHENTICATION

### Module

Authentication

### Mục tiêu

Hoàn thành chức năng đăng nhập và điều hướng hệ thống.

### Tasks

* Thiết kế luồng đăng nhập Admin
* Thiết kế luồng đăng nhập Student
* Validate dữ liệu đăng nhập
* Kiểm tra tài khoản tồn tại
* Kiểm tra mật khẩu
* Điều hướng theo vai trò
* Xây dựng chức năng Logout

### Kiến thức áp dụng

* OOP
* Exception Handling
* Validation
* JDBC Login Query

---

## BUỔI 2 - COURSE MANAGEMENT

### Module

Course

### Mục tiêu

Hoàn thành CRUD khóa học.

### Tasks

* Hiển thị danh sách khóa học
* Thêm khóa học
* Chỉnh sửa khóa học
* Xóa khóa học
* Tìm kiếm khóa học theo tên
* Sắp xếp khóa học
* Validate dữ liệu khóa học

### Kiến thức áp dụng

* OOP
* Collections
* CRUD JDBC
* Comparator
* Validation

---

## BUỔI 3 - STUDENT MANAGEMENT

### Module

Student

### Mục tiêu

Hoàn thành CRUD học viên.

### Tasks

* Hiển thị danh sách học viên
* Thêm học viên
* Chỉnh sửa học viên
* Xóa học viên
* Tìm kiếm học viên
* Sắp xếp học viên
* Validate email
* Validate số điện thoại

### Kiến thức áp dụng

* OOP
* Collections
* Regex
* JDBC CRUD
* Exception Handling

---

## BUỔI 4 - ENROLLMENT MANAGEMENT

### Module

Enrollment

### Mục tiêu

Hoàn thành đăng ký khóa học.

### Tasks

* Hiển thị danh sách khóa học
* Đăng ký khóa học
* Kiểm tra đăng ký trùng lặp
* Hiển thị danh sách khóa học đã đăng ký
* Hủy đăng ký khóa học
* Duyệt đăng ký khóa học
* Hiển thị học viên theo khóa học

### Kiến thức áp dụng

* JDBC Relationship
* Foreign Key
* JOIN
* Business Validation

---

## BUỔI 5 - STATISTICS

### Module

Statistics

### Mục tiêu

Hoàn thành thống kê và tổng kiểm thử.

### Tasks

* Thống kê tổng số khóa học
* Thống kê tổng số học viên
* Thống kê học viên theo khóa học
* Top 5 khóa học đông học viên nhất
* Liệt kê khóa học có trên 10 học viên
* Kiểm thử toàn hệ thống
* Fix Bug

### Kiến thức áp dụng

* Aggregate Functions
* GROUP BY
* JOIN
* JDBC Reporting

---

# 5. Chức năng Bonus

## Bonus 1

Phân trang dữ liệu

Tasks:

* Phân trang danh sách khóa học
* Phân trang danh sách học viên

---

## Bonus 2

Đề xuất khóa học

Tasks:

* Phân tích lịch sử đăng ký
* Đề xuất khóa học phù hợp

---

# 6. Điều kiện hoàn thành dự án

Dự án được xem là hoàn thành khi:

* Đăng nhập hoạt động ổn định.
* CRUD khóa học hoạt động đầy đủ.
* CRUD học viên hoạt động đầy đủ.
* Đăng ký khóa học hoạt động đúng.
* Thống kê hiển thị chính xác.
* Không phát sinh lỗi nghiêm trọng.
* Cấu trúc mã nguồn tuân thủ kiến trúc đã thiết kế.

---

# 7. Kết quả mong đợi

Sau khi hoàn thành dự án, sinh viên có khả năng:

* Thiết kế cơ sở dữ liệu quan hệ.
* Xây dựng ứng dụng Java Console.
* Làm việc với JDBC và PostgreSQL.
* Áp dụng OOP trong dự án thực tế.
* Áp dụng mô hình DAO - Business - Presentation.
* Quản lý tiến độ bằng GitHub Project.
