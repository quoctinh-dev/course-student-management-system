# BẢN ĐỒ ĐỊNH VỊ FILE HỆ THỐNG KIỂM THỬ (SYSTEM OVERVIEW)

## MỤC ĐÍCH

Tài liệu này được sử dụng để hỗ trợ Gemini, ChatGPT, Cursor Agent hoặc QA Engineer hiểu đầy đủ cấu trúc dự án trước khi phân tích mã nguồn, sinh test case hoặc thực hiện kiểm thử.

Khi kiểm thử bất kỳ chức năng nào trong bảng đặc tả Excel, bắt buộc phải nạp đầy đủ context của các tầng liên quan:

* Presentation Layer (UI)
* Business Layer
* DAO Layer
* Model / DTO / Exception / Utility

Nếu thiếu bất kỳ tầng nào, Agent có thể đưa ra kết quả phân tích hoặc kiểm thử không chính xác.

---

# 1. PHÂN HỆ XÁC THỰC VÀ ĐĂNG NHẬP (AUTH & LOGIN)

## Chức năng

* Đăng nhập Admin
* Đăng nhập Học viên
* Kiểm tra dữ liệu rỗng
* Xác thực mật khẩu
* So khớp BCrypt
* Điều hướng Menu theo vai trò

## Bản đồ file cần gọi

### UI (Presentation)

* Main.java
* MainMenuUI.java
* AdminLoginUI.java
* StudentLoginUI.java

### Business

* IAdminbusiness.java

* Adminbusinessimpl.java

* IStudentBusiness.java

* StudentBusinessImpl.java

### DAO

* IAdminDao.java

* AdminDAOImpl.java

* IStudentDao.java

* StudentDAOImpl.java

### Utils

* BCryptUtil.java
* DBUtil.java

### Model

* Admin.java
* Student.java

### Exception

* ValidationException.java
* DatabaseException.java

---

# 2. PHÂN HỆ QUẢN LÝ KHÓA HỌC (COURSES MANAGEMENT)

## Chức năng

* Xem danh sách khóa học
* Thêm khóa học
* Chỉnh sửa khóa học
* Xóa khóa học
* Tìm kiếm tương đối theo tên
* Sắp xếp tăng dần
* Sắp xếp giảm dần

## Bản đồ file cần gọi

### UI

* AdminMenuUI.java
* CourseUI.java

### Business

* ICourseBusiness.java
* CourseBusinessImpl.java

### DAO

* ICourseDAO.java
* CourseDAOImpl.java

### Model

* Course.java

### Exception

* ValidationException.java
* BusinessException.java
* DatabaseException.java

### Utils

* DBUtil.java

---

# 3. PHÂN HỆ QUẢN LÝ HỌC VIÊN (STUDENTS MANAGEMENT)

## Chức năng

* Thêm học viên
* Cập nhật học viên
* Xóa học viên
* Tìm kiếm theo ID
* Tìm kiếm theo Email
* Tìm kiếm theo Tên
* Sắp xếp danh sách
* Đổi mật khẩu

## Bản đồ file cần gọi

### UI

* AdminMenuUI.java
* StudentUI.java

### Business

* IStudentBusiness.java
* StudentBusinessImpl.java

### DAO

* IStudentDao.java
* StudentDAOImpl.java

### Model

* Student.java

### Exception

* ValidationException.java
* DatabaseException.java
* BusinessException.java

### Utils

* BCryptUtil.java
* DBUtil.java

---

# 4. PHÂN HỆ QUẢN LÝ ĐĂNG KÝ & PHÊ DUYỆT (ENROLLMENTS)

## Chức năng

* Đăng ký khóa học
* Xem danh sách đăng ký
* Duyệt đăng ký
* Hủy đăng ký
* Xóa đăng ký chưa xác nhận
* Kiểm tra trạng thái đăng ký

## Bản đồ file cần gọi

### UI

* AdminMenuUI.java
* EnrollmentUI.java
* StudentMenuUI.java

### Business

* IEnrollmentBusiness.java
* EnrollmentBusinessImpl.java

### DAO

* IEnrollmentDAO.java
* EnrollmentDAOImpl.java

### Model

* Enrollment.java
* EnrollmentStatus.java

### Exception

* DatabaseException.java
* BusinessException.java

### Utils

* DBUtil.java

---

# 5. PHÂN HỆ THỐNG KÊ & ĐỀ XUẤT KHÓA HỌC (STATISTICS & RECOMMENDATION)

## Chức năng

* Phân trang danh sách khóa học
* Thống kê tổng số khóa học
* Thống kê tổng số học viên
* Thống kê học viên theo từng khóa
* Đề xuất khóa học dựa trên dữ liệu đăng ký

## Bản đồ file cần gọi

### UI

* AdminMenuUI.java
* StatisticsUI.java
* StudentMenuUI.java
* StudentUI.java

### Business

* IStatisticBusiness.java

* StatisticBusinessImpl.java

* IStudentBusiness.java

* StudentBusinessImpl.java

### DAO

* IStatisticDAO.java

* StatisticDAOImpl.java

* IStudentDao.java

* StudentDAOImpl.java

### DTO

* CourseStatisticDTO.java

### Model

* Course.java
* Enrollment.java
* Student.java

### Utils

* DBUtil.java

---

# PACKAGE STRUCTURE THỰC TẾ

```text
ra.cms
│
├── business
│   ├── impl
│   │   ├── Adminbusinessimpl
│   │   ├── CourseBusinessImpl
│   │   ├── EnrollmentBusinessImpl
│   │   ├── StatisticBusinessImpl
│   │   └── StudentBusinessImpl
│   │
│   ├── IAdminbusiness
│   ├── ICourseBusiness
│   ├── IEnrollmentBusiness
│   ├── IStatisticBusiness
│   └── IStudentBusiness
│
├── dao
│   ├── impl
│   │   ├── AdminDAOImpl
│   │   ├── CourseDAOImpl
│   │   ├── EnrollmentDAOImpl
│   │   ├── StatisticDAOImpl
│   │   └── StudentDAOImpl
│   │
│   ├── IAdminDao
│   ├── ICourseDAO
│   ├── IEnrollmentDAO
│   ├── IStatisticDAO
│   └── IStudentDao
│
├── dto
│   └── CourseStatisticDTO
│
├── exception
│   ├── BusinessException
│   ├── DatabaseException
│   └── ValidationException
│
├── model
│   ├── Admin
│   ├── Course
│   ├── Enrollment
│   ├── EnrollmentStatus
│   └── Student
│
├── presentation
│   ├── admin
│   │   ├── AdminMenuUI
│   │   ├── CourseUI
│   │   ├── EnrollmentUI
│   │   ├── StatisticsUI
│   │   └── StudentUI
│   │
│   ├── auth
│   │   ├── AdminLoginUI
│   │   └── StudentLoginUI
│   │
│   ├── student
│   │   └── StudentMenuUI
│   │
│   └── MainMenuUI
│
├── utils
│   ├── BCryptUtil
│   └── DBUtil
│
└── Main
```

---

# LUỒNG KIẾN TRÚC CHUẨN

```text
Presentation Layer
        ↓
Business Layer
        ↓
DAO Layer
        ↓
Database
```

Chi tiết:

```text
UI
 ↓
Interface Business
 ↓
Business Impl
 ↓
Interface DAO
 ↓
DAO Impl
 ↓
DBUtil
 ↓
Database
```

Đối với các chức năng liên quan mật khẩu:

```text
UI
 ↓
Business Impl
 ↓
BCryptUtil
 ↓
DAO
 ↓
Database
```

---

# HỆ THỐNG EXCEPTION DÙNG XUYÊN SUỐT

## ValidationException

Sử dụng khi:

* Dữ liệu rỗng
* Sai định dạng Email
* Sai độ dài Password
* Dữ liệu đầu vào không hợp lệ

## BusinessException

Sử dụng khi:

* Vi phạm quy tắc nghiệp vụ
* Trùng dữ liệu
* Không đủ điều kiện xử lý

## DatabaseException

Sử dụng khi:

* Lỗi kết nối CSDL
* Lỗi truy vấn
* Lỗi thao tác dữ liệu

---

# QUY TẮC BẮT BUỘC CHO AGENT KIỂM THỬ

Khi kiểm thử một chức năng bất kỳ:

1. Luôn đọc UI tương ứng.
2. Luôn đọc Interface Business và Business Impl.
3. Luôn đọc Interface DAO và DAO Impl.
4. Luôn đọc Model/DTO liên quan.
5. Luôn đọc các Exception được sử dụng.
6. Luôn đọc DBUtil nếu có truy cập CSDL.
7. Không được kiểm thử chỉ dựa trên UI hoặc Business.
8. Phải phân tích toàn bộ luồng từ UI → Business → DAO → Database.

Nếu thiếu bất kỳ tầng nào thì kết quả kiểm thử được xem là không hợp lệ.
