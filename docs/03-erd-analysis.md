# 03. PHÂN TÍCH CƠ SỞ DỮ LIỆU (ERD ANALYSIS)

## 1. Mục tiêu

Tài liệu này dùng để phân tích các bảng dữ liệu trong hệ thống nhằm:

* Hiểu ý nghĩa của từng bảng.
* Hiểu mối quan hệ giữa các bảng.
* Xác định các Entity Java cần xây dựng.
* Chuẩn bị cho việc thiết kế DAO, Service và JDBC.

---

# 2. Tổng quan cơ sở dữ liệu

Hệ thống gồm 4 bảng chính:

1. admin
2. student
3. course
4. enrollment

Trong đó:

* admin dùng để xác thực quản trị viên.
* student lưu thông tin học viên.
* course lưu thông tin khóa học.
* enrollment quản lý việc đăng ký khóa học.

---

# 3. Phân tích bảng Admin

## Mục đích

Lưu thông tin tài khoản quản trị viên.

Admin là người có toàn quyền quản lý hệ thống.

---

## Các thuộc tính

| Cột      | Ý nghĩa          |
| -------- | ---------------- |
| id       | Mã quản trị viên |
| username | Tên đăng nhập    |
| password | Mật khẩu         |

---

## Khóa chính

```text
id
```

Vai trò:

* Định danh duy nhất cho Admin.

---

## Nghiệp vụ liên quan

* Đăng nhập Admin.
* Xác thực tài khoản.

---

## Java Entity tương ứng

```text
Admin
```

---

# 4. Phân tích bảng Student

## Mục đích

Lưu thông tin học viên tham gia các khóa học.

---

## Các thuộc tính

| Cột       | Ý nghĩa       |
| --------- | ------------- |
| id        | Mã học viên   |
| name      | Họ tên        |
| dob       | Ngày sinh     |
| email     | Email         |
| sex       | Giới tính     |
| phone     | Số điện thoại |
| password  | Mật khẩu      |
| create_at | Ngày tạo      |

---

## Khóa chính

```text
id
```

---

## Ràng buộc

### Email

```text
UNIQUE
```

Ý nghĩa:

* Không được trùng email.

---

## Nghiệp vụ liên quan

* Quản lý học viên.
* Đăng nhập học viên.
* Đăng ký khóa học.
* Đổi mật khẩu.

---

## Java Entity tương ứng

```text
Student
```

---

# 5. Phân tích bảng Course

## Mục đích

Lưu thông tin khóa học.

---

## Các thuộc tính

| Cột        | Ý nghĩa              |
| ---------- | -------------------- |
| id         | Mã khóa học          |
| name       | Tên khóa học         |
| duration   | Thời lượng           |
| instructor | Giảng viên phụ trách |
| create_at  | Ngày tạo             |

---

## Khóa chính

```text
id
```

---

## Nghiệp vụ liên quan

* Thêm khóa học.
* Sửa khóa học.
* Xóa khóa học.
* Tìm kiếm khóa học.
* Sắp xếp khóa học.

---

## Java Entity tương ứng

```text
Course
```

---

# 6. Phân tích bảng Enrollment

## Mục đích

Quản lý việc đăng ký khóa học.

Đây là bảng trung gian giữa Student và Course.

---

## Các thuộc tính

| Cột           | Ý nghĩa            |
| ------------- | ------------------ |
| id            | Mã đăng ký         |
| student_id    | Học viên đăng ký   |
| course_id     | Khóa học đăng ký   |
| registered_at | Ngày đăng ký       |
| status        | Trạng thái đăng ký |

---

## Khóa chính

```text
id
```

---

## Khóa ngoại

### student_id

Tham chiếu:

```text
student(id)
```

---

### course_id

Tham chiếu:

```text
course(id)
```

---

## Trạng thái đăng ký

Theo thiết kế Database:

```text
WAITING
DENIED
CANCELED
```

Ý nghĩa:

### WAITING

* Chờ duyệt.

### DENIED

* Bị từ chối.

### CANCELED

* Đã hủy.

---

## Nghiệp vụ liên quan

* Đăng ký khóa học.
* Duyệt đăng ký.
* Hủy đăng ký.
* Thống kê học viên theo khóa học.

---

## Java Entity tương ứng

```text
Enrollment
```

---

# 7. Quan hệ giữa các bảng

## Student và Enrollment

Quan hệ:

```text
1 - N
```

Ý nghĩa:

Một học viên có thể đăng ký nhiều khóa học.

---

## Course và Enrollment

Quan hệ:

```text
1 - N
```

Ý nghĩa:

Một khóa học có thể có nhiều học viên.

---

## Student và Course

Quan hệ thực tế:

```text
N - N
```

Được quản lý thông qua:

```text
Enrollment
```

---

# 8. ERD Logic

Student

↓

Enrollment

↓

Course

Hay:

Student
|
| 1 - N
|
Enrollment
|
| N - 1
|
Course

---

# 9. Danh sách Entity Java cần xây dựng

Dựa trên Database hiện tại, hệ thống cần 4 Entity:

```text
Admin
Student
Course
Enrollment
```

Mỗi Entity sẽ tương ứng với:

* Một class Model.
* Một DAO Interface.
* Một DAO Implementation.
* Một Service Interface.
* Một Service Implementation.

---

# 10. Kết luận

Database hiện tại được thiết kế theo mô hình quan hệ đơn giản, phù hợp với đồ án Java Console JDBC.

Bảng Enrollment đóng vai trò quan trọng nhất vì nó quản lý mối quan hệ giữa Student và Course.

Hiểu rõ 4 bảng dữ liệu là điều kiện bắt buộc trước khi bắt đầu thiết kế Entity, DAO và Service.
