# 🎓 Hệ thống Quản lý Học viên và Khóa học

Hệ thống Quản lý Học viên và Khóa học được xây dựng bằng Java Core và PostgreSQL, mô phỏng hoạt động quản lý tại một trung tâm đào tạo.

Dự án cho phép quản trị viên quản lý học viên, khóa học, đăng ký học tập và thống kê dữ liệu. Đồng thời học viên có thể tra cứu khóa học, đăng ký học, theo dõi lịch sử đăng ký và nhận gợi ý khóa học phù hợp.

---

## 🎯 Mục tiêu dự án

Dự án được thực hiện nhằm:

* Thực hành lập trình hướng đối tượng (OOP)
* Làm việc với cơ sở dữ liệu PostgreSQL thông qua JDBC
* Áp dụng kiến trúc phân tầng trong ứng dụng Java
* Xây dựng và xử lý các nghiệp vụ quản lý học viên
* Thực hành kiểm thử và cải thiện chất lượng phần mềm

---

## 🛠 Công nghệ sử dụng

### Ngôn ngữ và Cơ sở dữ liệu

* Java 17
* PostgreSQL

### Thư viện

* PostgreSQL JDBC Driver
* BCrypt (Mã hóa mật khẩu)

### Công cụ phát triển

* IntelliJ IDEA
* Maven
* pgAdmin 4
* Git & GitHub

---

## 🏗 Kiến trúc hệ thống

Hệ thống được xây dựng theo mô hình phân tầng:

```text
Presentation Layer (Console UI)
            ↓
Business Layer
            ↓
DAO Layer
            ↓
PostgreSQL Database
```

### Cấu trúc chính

```text
src/
├── presentation/
├── business/
├── dao/
├── model/
├── dto/
├── exception/
└── utils/
```

| Tầng         | Vai trò                           |
| ------------ | --------------------------------- |
| Presentation | Giao diện và tương tác người dùng |
| Business     | Xử lý nghiệp vụ                   |
| DAO          | Truy xuất dữ liệu                 |
| Model        | Định nghĩa đối tượng              |
| DTO          | Truyền dữ liệu thống kê           |
| Exception    | Xử lý lỗi tùy chỉnh               |
| Utils        | Tiện ích dùng chung               |

---

## 🗄 Thiết kế cơ sở dữ liệu

Hệ thống sử dụng 4 bảng chính:

### Admins

Lưu thông tin tài khoản quản trị viên.

### Students

Lưu thông tin học viên.

### Courses

Lưu thông tin khóa học.

### Enrollments

Lưu thông tin đăng ký khóa học của học viên.

### Quan hệ dữ liệu

```text
Students
    |
    | 1:N
    |
Enrollments
    |
    | N:1
    |
Courses
```

Bảng Enrollments đóng vai trò trung gian xử lý quan hệ nhiều-nhiều giữa học viên và khóa học.

---

# 🔐 Chức năng Xác thực

### Quản trị viên

* Đăng nhập
* Đăng xuất

### Học viên

* Đăng nhập
* Đăng xuất
* Đổi mật khẩu

### Bảo mật

* Mật khẩu được mã hóa bằng BCrypt
* Sử dụng PreparedStatement để hạn chế SQL Injection
* Kiểm tra dữ liệu đầu vào trước khi xử lý
* Sử dụng Foreign Key để đảm bảo tính toàn vẹn dữ liệu

---

# 👨‍💼 Chức năng Quản trị viên

## Quản lý khóa học

* Hiển thị danh sách khóa học
* Phân trang dữ liệu
* Thêm khóa học mới
* Cập nhật thông tin khóa học
* Xóa khóa học
* Tìm kiếm khóa học theo tên
* Sắp xếp khóa học theo ID hoặc tên

## Quản lý học viên

* Hiển thị danh sách học viên
* Phân trang dữ liệu
* Thêm học viên
* Cập nhật thông tin học viên
* Xóa học viên
* Tìm kiếm học viên
* Sắp xếp dữ liệu học viên

## Quản lý đăng ký học

* Xem danh sách đăng ký theo khóa học
* Duyệt đăng ký học
* Từ chối đăng ký học
* Xóa đăng ký học

## Thống kê

* Tổng số học viên
* Tổng số khóa học
* Thống kê số lượng học viên theo khóa học
* Top 5 khóa học có nhiều học viên nhất
* Danh sách khóa học có trên 10 học viên

---

# 👨‍🎓 Chức năng Học viên

## Tra cứu khóa học

* Xem danh sách khóa học
* Tìm kiếm khóa học theo tên

## Đăng ký học

* Đăng ký khóa học
* Hủy đăng ký khóa học
* Theo dõi trạng thái đăng ký

### Trạng thái đăng ký

```text
WAITING
   ↓
CONFIRM

hoặc

WAITING
   ↓
DENIED

hoặc

WAITING
   ↓
CANCELED
```

## Quản lý tài khoản

* Đổi mật khẩu

## Gợi ý khóa học

Hệ thống hỗ trợ gợi ý khóa học dựa trên lịch sử đăng ký của các học viên có hành vi học tập tương tự.

---

# 📊 Kiểm thử hệ thống

Dự án được kiểm thử thủ công thông qua 5 bộ Test Report:

* Authentication
* Course Management
* Student Management
* Enrollment Management
* Statistics & Recommendation

Các báo cáo kiểm thử được lưu trong thư mục:

```text
testing/
```

Mục tiêu là kiểm tra:

* Tính đúng đắn của nghiệp vụ
* Xử lý ngoại lệ
* Kiểm tra dữ liệu đầu vào
* Luồng xử lý người dùng

---

# ⚙ Xử lý ngoại lệ

Hệ thống xây dựng các lớp Exception riêng:

```text
BusinessException
DatabaseException
ValidationException
```

Giúp:

* Phân loại lỗi rõ ràng
* Dễ bảo trì
* Hiển thị thông báo thân thiện cho người dùng

---

# 📌 Kết quả đạt được

* Hoàn thành hệ thống quản lý học viên và khóa học bằng Java Core
* Kết nối và thao tác dữ liệu với PostgreSQL bằng JDBC
* Áp dụng mô hình phân tầng UI - Business - DAO
* Triển khai cơ chế mã hóa mật khẩu BCrypt
* Xây dựng chức năng đăng ký học và quản lý trạng thái đăng ký
* Xây dựng hệ thống thống kê dữ liệu
* Thực hiện kiểm thử cho các phân hệ chính

---

# 🚀 Hướng phát triển

* Áp dụng Dependency Injection để giảm phụ thuộc giữa các lớp
* Bổ sung Unit Test bằng JUnit
* Tối ưu mã nguồn theo nguyên tắc DRY
* Phát triển giao diện Desktop hoặc Web
* Mở rộng chức năng báo cáo và phân tích dữ liệu

---

## 👨‍💻 Tác giả

Nguyễn Quốc Tịnh

Sinh viên Công nghệ Thông tin, yêu thích phát triển Backend và thiết kế hệ thống.

Dự án được xây dựng nhằm thực hành Java Core, JDBC, PostgreSQL, OOP và kiến trúc phần mềm phân tầng trong môi trường quản lý học viên thực tế.

GitHub: https://github.com/quoctinh-dev
