# 🎓 Course & Student Management System

Hệ thống Quản lý Học viên và Khóa học được xây dựng bằng **Java Core** và **PostgreSQL**, mô phỏng hoạt động quản lý tại một trung tâm đào tạo.

Dự án hỗ trợ quản trị viên quản lý học viên, khóa học, đăng ký học tập và thống kê dữ liệu. Đồng thời học viên có thể tra cứu khóa học, đăng ký học, theo dõi trạng thái đăng ký và nhận gợi ý khóa học phù hợp.

---

## 📌 Tổng quan dự án

Mục tiêu của dự án là áp dụng các kiến thức về:

* Lập trình hướng đối tượng (OOP)
* JDBC và PostgreSQL
* Thiết kế ứng dụng theo kiến trúc phân tầng
* Xử lý ngoại lệ và kiểm tra dữ liệu đầu vào
* Quản lý nghiệp vụ thực tế trong hệ thống đào tạo
* Kiểm thử và cải thiện chất lượng phần mềm

Dự án được phát triển theo mô hình Console Application nhằm tập trung vào thiết kế hệ thống, xử lý nghiệp vụ và làm việc với cơ sở dữ liệu.

---

## 🛠 Công nghệ sử dụng

| Thành phần      | Công nghệ     |
| --------------- | ------------- |
| Ngôn ngữ        | Java 17       |
| Cơ sở dữ liệu   | PostgreSQL    |
| Kết nối dữ liệu | JDBC          |
| Mã hóa mật khẩu | BCrypt        |
| Build Tool      | Maven         |
| IDE             | IntelliJ IDEA |
| Database Tool   | pgAdmin 4     |
| Version Control | Git & GitHub  |

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

### Cấu trúc thư mục

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

| Tầng         | Chức năng                                        |
| ------------ | ------------------------------------------------ |
| Presentation | Hiển thị giao diện và nhận dữ liệu từ người dùng |
| Business     | Xử lý nghiệp vụ và kiểm tra dữ liệu              |
| DAO          | Thao tác với cơ sở dữ liệu                       |
| Model        | Định nghĩa các thực thể                          |
| DTO          | Truyền dữ liệu giữa các tầng                     |
| Exception    | Quản lý các ngoại lệ tùy chỉnh                   |
| Utils        | Các tiện ích hỗ trợ hệ thống                     |

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

Bảng **Enrollments** đóng vai trò trung gian xử lý quan hệ nhiều-nhiều giữa học viên và khóa học, đồng thời lưu trạng thái đăng ký.

---

# 🔐 Chức năng xác thực

### Quản trị viên

* Đăng nhập
* Đăng xuất

### Học viên

* Đăng nhập
* Đăng xuất
* Đổi mật khẩu

### Bảo mật

* Mã hóa mật khẩu bằng BCrypt
* Sử dụng PreparedStatement để hạn chế SQL Injection
* Kiểm tra dữ liệu đầu vào trước khi xử lý
* Ràng buộc khóa ngoại (Foreign Key) đảm bảo tính toàn vẹn dữ liệu

---

# 👨‍💼 Chức năng dành cho Quản trị viên

## Quản lý khóa học

* Xem danh sách khóa học
* Phân trang dữ liệu
* Thêm mới khóa học
* Cập nhật thông tin khóa học
* Xóa khóa học
* Tìm kiếm khóa học theo tên
* Sắp xếp khóa học theo ID hoặc tên

## Quản lý học viên

* Xem danh sách học viên
* Phân trang dữ liệu
* Thêm mới học viên
* Cập nhật thông tin học viên
* Xóa học viên
* Tìm kiếm học viên
* Sắp xếp danh sách học viên

## Quản lý đăng ký học

* Xem danh sách đăng ký theo khóa học
* Duyệt đăng ký học
* Từ chối đăng ký học
* Xóa đăng ký học

## Thống kê và báo cáo

* Tổng số học viên
* Tổng số khóa học
* Thống kê số lượng học viên theo từng khóa học
* Top 5 khóa học có nhiều học viên nhất
* Danh sách khóa học có trên 10 học viên

---

# 👨‍🎓 Chức năng dành cho Học viên

## Tra cứu khóa học

* Xem danh sách khóa học
* Tìm kiếm khóa học theo tên

## Đăng ký học

* Đăng ký khóa học
* Hủy đăng ký khóa học
* Theo dõi trạng thái đăng ký

### Luồng trạng thái đăng ký

```text
WAITING
   ↓
CONFIRM

WAITING
   ↓
DENIED

WAITING
   ↓
CANCELED
```

## Quản lý tài khoản

* Đổi mật khẩu

## Gợi ý khóa học

Hệ thống hỗ trợ gợi ý khóa học dựa trên lịch sử đăng ký của các học viên có hành vi học tập tương tự, giúp người dùng dễ dàng khám phá các khóa học phù hợp.

---

# ⭐ Điểm nổi bật của dự án

* Áp dụng mô hình phân tầng UI – Business – DAO
* Quản lý dữ liệu bằng PostgreSQL thông qua JDBC
* Mã hóa mật khẩu bằng BCrypt
* Hệ thống phân trang cho các danh sách dữ liệu
* Xử lý ngoại lệ bằng Custom Exception
* Hỗ trợ thống kê và báo cáo dữ liệu
* Có chức năng gợi ý khóa học cho học viên
* Có bộ tài liệu kiểm thử và đặc tả chức năng đi kèm

---

# 📊 Kiểm thử hệ thống

Dự án được kiểm thử thủ công thông qua 5 bộ Test Report:

* Authentication
* Course Management
* Student Management
* Enrollment Management
* Statistics & Recommendation

Các tài liệu kiểm thử được lưu tại:

```text
testing/
```

Mục tiêu kiểm thử:

* Kiểm tra tính đúng đắn của nghiệp vụ
* Kiểm tra dữ liệu đầu vào
* Kiểm tra xử lý ngoại lệ
* Kiểm tra luồng thao tác người dùng

---

# 📄 Tài liệu dự án

Các tài liệu phân tích, thiết kế và kiểm thử được lưu trong:

```text
docs/
testing/
```

Bao gồm:

* Đặc tả chức năng hệ thống
* Thiết kế cơ sở dữ liệu
* Báo cáo kiểm thử
* Tài liệu phân tích nghiệp vụ

---

# 🚀 Hướng phát triển

* Áp dụng Dependency Injection
* Bổ sung Unit Test với JUnit
* Tối ưu và tái cấu trúc mã nguồn
* Phát triển giao diện Desktop hoặc Web
* Mở rộng hệ thống báo cáo và phân tích dữ liệu

---

## 👨‍💻 Tác giả

### Nguyễn Quốc Tính

Sinh viên Công nghệ Thông tin, quan tâm đến:

* Backend Development
* Java Ecosystem
* Database Design
* Software Architecture
* Software Testing

GitHub: https://github.com/quoctinh-dev

---

> Dự án được thực hiện nhằm thực hành Java Core, JDBC, PostgreSQL, OOP và thiết kế phần mềm theo kiến trúc phân tầng trong môi trường quản lý đào tạo thực tế.
