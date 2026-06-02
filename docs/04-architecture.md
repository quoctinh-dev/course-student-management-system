# 04. KIẾN TRÚC HỆ THỐNG

## 1. Mục tiêu

Tài liệu này mô tả cách tổ chức mã nguồn của dự án theo mô hình phân lớp.

Mục tiêu:

* Tách biệt nghiệp vụ và dữ liệu.
* Dễ bảo trì và mở rộng.
* Tuân thủ kiến trúc DAO - Business - Presentation.
* Hạn chế phụ thuộc giữa các lớp.

---

# 2. Tổng quan kiến trúc

Hệ thống được xây dựng theo mô hình 3 lớp:

Presentation Layer

↓

Business Layer

↓

Data Access Layer

↓

PostgreSQL Database

---

# 3. Cấu trúc thư mục dự kiến

src/main/java

├── model
│
├── dao
│
├── dao/impl
│
├── business
│
├── business/impl
│
├── presentation
│
├── utils
│
└── Main.java

---

# 4. Model Layer

## Mục đích

Chứa các đối tượng biểu diễn dữ liệu của hệ thống.

Mỗi bảng trong cơ sở dữ liệu tương ứng với một class Model.

---

## Các Model dự kiến

Admin

Student

Course

Enrollment

---

## Trách nhiệm

* Lưu trữ dữ liệu.
* Getter / Setter.
* Constructor.
* toString().

---

## Không được làm

* Không viết SQL.
* Không xử lý nghiệp vụ.
* Không hiển thị menu.

---

# 5. DAO Layer

## Mục đích

Định nghĩa các thao tác làm việc với cơ sở dữ liệu.

DAO chỉ mô tả hành động cần thực hiện.

---

## DAO dự kiến

AdminDAO

StudentDAO

CourseDAO

EnrollmentDAO

---

## Trách nhiệm

Ví dụ:

CourseDAO

* Lấy danh sách khóa học.
* Thêm khóa học.
* Cập nhật khóa học.
* Xóa khóa học.
* Tìm kiếm khóa học.

---

## Không được làm

* Không in dữ liệu ra màn hình.
* Không xử lý menu.
* Không chứa nghiệp vụ phức tạp.

---

# 6. DAO Implementation Layer

## Mục đích

Triển khai các phương thức đã khai báo trong DAO.

Đây là nơi JDBC làm việc với PostgreSQL.

---

## DAO Implementation dự kiến

AdminDAOImpl

StudentDAOImpl

CourseDAOImpl

EnrollmentDAOImpl

---

## Trách nhiệm

* Kết nối Database.
* Thực hiện SQL.
* Thực hiện CRUD.
* Mapping dữ liệu từ ResultSet sang Model.

---

## Không được làm

* Không hiển thị giao diện.
* Không xử lý nghiệp vụ.

---

# 7. Business Layer

## Mục đích

Định nghĩa các nghiệp vụ của hệ thống.

Business là cầu nối giữa Presentation và DAO.

---

## Business dự kiến

AdminBusiness

StudentBusiness

CourseBusiness

EnrollmentBusiness

---

## Trách nhiệm

Ví dụ:

Đăng ký khóa học

* Kiểm tra khóa học tồn tại.
* Kiểm tra học viên tồn tại.
* Kiểm tra đăng ký trùng lặp.
* Sau đó mới gọi DAO lưu dữ liệu.

---

## Không được làm

* Không viết SQL.
* Không thao tác trực tiếp với Database.

---

# 8. Business Implementation Layer

## Mục đích

Triển khai các nghiệp vụ đã định nghĩa.

---

## Business Implementation dự kiến

AdminBusinessImpl

StudentBusinessImpl

CourseBusinessImpl

EnrollmentBusinessImpl

---

## Trách nhiệm

* Xử lý logic nghiệp vụ.
* Kiểm tra dữ liệu.
* Gọi DAO.

---

# 9. Presentation Layer

## Mục đích

Tương tác với người dùng thông qua Console.

Đây là nơi hiển thị menu và nhận dữ liệu nhập vào.

---

## Chức năng chính

### Menu Đăng nhập

* Đăng nhập Admin.
* Đăng nhập Student.

### Menu Admin

* Quản lý khóa học.
* Quản lý học viên.
* Quản lý đăng ký.
* Thống kê.

### Menu Student

* Xem khóa học.
* Đăng ký khóa học.
* Xem khóa học đã đăng ký.
* Đổi mật khẩu.

---

## Trách nhiệm

* Hiển thị menu.
* Nhận dữ liệu từ bàn phím.
* Hiển thị kết quả.

---

## Không được làm

* Không viết SQL.
* Không kết nối Database trực tiếp.

---

# 10. Utils Layer

## Mục đích

Chứa các thành phần dùng chung cho toàn bộ hệ thống.

---

## Các Utility dự kiến

ConnectionUtil

ValidationUtil

DateTimeUtil

ConsoleUtil

---

## Trách nhiệm

* Kết nối PostgreSQL.
* Kiểm tra dữ liệu đầu vào.
* Xử lý ngày giờ.
* Hỗ trợ hiển thị dữ liệu.

---

# 11. Main Class

## Mục đích

Điểm bắt đầu của chương trình.

---

## Trách nhiệm

* Khởi tạo chương trình.
* Hiển thị màn hình đầu tiên.
* Điều hướng đến các menu chức năng.

---

# 12. Luồng xử lý chuẩn

Ví dụ:

Người dùng chọn:

"Thêm khóa học"

↓

CourseView

↓

CourseBusiness

↓

CourseDAO

↓

PostgreSQL

↓

CourseDAO

↓

CourseBusiness

↓

CourseView

↓

Hiển thị kết quả

---

# 13. Nguyên tắc thiết kế

Nguyên tắc 1

Presentation chỉ giao tiếp với Business.

---

Nguyên tắc 2

Business chỉ giao tiếp với DAO.

---

Nguyên tắc 3

DAO chỉ giao tiếp với Database.

---

Nguyên tắc 4

Model không chứa nghiệp vụ.

---

Nguyên tắc 5

Không viết SQL trong View hoặc Business.

---

# 14. Kết luận

Kiến trúc DAO - Business - Presentation giúp hệ thống dễ bảo trì, dễ kiểm thử và phù hợp với mục tiêu học tập Java Core, JDBC và PostgreSQL.

Mỗi lớp có trách nhiệm riêng, hạn chế phụ thuộc và tăng khả năng mở rộng của dự án.
