# 📋 TÀI LIỆU KIỂM THỬ MODULE QUẢN LÝ KHÓA HỌC (COURSE MANAGEMENT)

**Ngày thực hiện:** 12/06/2026
**Người thực hiện:** QA Engineer
**Module:** Course Management (Quản lý Khóa học)

---

# 1. MỤC TIÊU KIỂM THỬ

Đánh giá tính đúng đắn và độ ổn định của module Quản lý Khóa học, bao gồm:

* Chức năng CRUD khóa học
* Kiểm tra ràng buộc dữ liệu nghiệp vụ
* Kiểm tra tính toàn vẹn dữ liệu giữa các tầng
* Kiểm tra bảo mật truy vấn SQL
* Kiểm tra trải nghiệm người dùng khi xảy ra lỗi
* Đánh giá khả năng mở rộng và bảo trì mã nguồn

---

# 2. PROMPT KIỂM THỬ GỐC

> Prompt sử dụng để điều hướng AI Agent trong quá trình phân tích mã nguồn.

```text
Bạn là một chuyên gia QA Senior.

Hãy kiểm thử module "Quản lý Khóa học" dựa trên các file:

- AdminMenuUI.java
- CourseUI.java
- CourseBusinessImpl.java
- CourseDAOImpl.java
- Các class Model/Exception liên quan

Yêu cầu:

- Kiểm tra tính toàn vẹn của CRUD.
- Kiểm tra luồng dữ liệu UI → Business → DAO → Database.
- Phát hiện lỗi nghiệp vụ.
- Kiểm tra bảo mật SQL Injection.
- Đánh giá khả năng mở rộng mã nguồn.
- Đề xuất hướng tối ưu theo SOLID và Clean Architecture.
- Đánh giá trải nghiệm người dùng khi phát sinh lỗi.
```

---

# 3. KẾT QUẢ PHÂN TÍCH MÃ NGUỒN

## 3.1 Đánh giá kiến trúc hiện tại

| Thành phần           | Đánh giá       |
| -------------------- | -------------- |
| Course UI            | Hoạt động đúng |
| Business Layer       | Hoạt động đúng |
| DAO Layer            | Hoạt động đúng |
| CRUD Operations      | Hoạt động đúng |
| Validation nghiệp vụ | Đạt yêu cầu    |
| Kết nối Database     | Ổn định        |
| Tìm kiếm & Sắp xếp   | Hoạt động đúng |

---

# 4. ĐỀ XUẤT TỐI ƯU HÓA MÃ NGUỒN

## 4.1 Khắc phục rủi ro SQL Injection (Whitelist Pattern)

### Hiện trạng

Phương thức:

```java
findAllSorted(String sortField, String direction)
```

đang sử dụng nối chuỗi SQL động.

Ví dụ:

```java
String sql = "SELECT * FROM courses ORDER BY "
             + sortField + " " + direction;
```

Mặc dù giao diện hiện tại kiểm soát đầu vào, nhưng vẫn tồn tại rủi ro SQL Injection nếu dữ liệu bị can thiệp.

---

### Đề xuất

Áp dụng Whitelist Pattern:

```java
public List<Course> findAllSorted(
        String sortField,
        String direction) {

    List<String> allowedFields =
            Arrays.asList(
                "name",
                "duration",
                "instructor"
            );

    String field =
            allowedFields.contains(sortField)
                    ? sortField
                    : "name";

    String dir =
            "DESC".equalsIgnoreCase(direction)
                    ? "DESC"
                    : "ASC";

    String sql =
            "SELECT * FROM courses ORDER BY "
                    + field + " " + dir;

    // Execute bằng PreparedStatement
}
```

---

### Lợi ích

* Loại bỏ nguy cơ SQL Injection.
* Chỉ cho phép sắp xếp trên các cột hợp lệ.
* Tuân thủ Secure Coding Practice.
* Dễ mở rộng trong tương lai.

---

## 4.2 Áp dụng Dependency Injection

### Hiện trạng

Một số lớp khởi tạo phụ thuộc trực tiếp:

```java
private CourseBusinessImpl courseBusiness =
        new CourseBusinessImpl();
```

---

### Vấn đề

* Tight Coupling
* Khó Unit Test
* Vi phạm Dependency Inversion Principle

---

### Đề xuất

Sử dụng Constructor Injection:

```java
public CourseUI(ICourseBusiness courseBusiness) {
    this.courseBusiness = courseBusiness;
}
```

Tương tự đối với:

* CourseBusinessImpl
* CourseDAOImpl
* Các lớp Business khác

---

### Lợi ích

* Dễ Mock khi Unit Test
* Tuân thủ SOLID
* Tăng khả năng mở rộng
* Giảm phụ thuộc giữa các tầng

---

# 5. PHÂN TÍCH BUG PIPELINE

| Mức độ       | Vấn đề                                  | Trạng thái      | Hướng xử lý                   |
| ------------ | --------------------------------------- | --------------- | ----------------------------- |
| Critical     | Không phát hiện lỗi                     | ✅ Đạt           | Không cần xử lý               |
| Warning      | SQL Injection tiềm ẩn tại findAllSorted | ⚠ Cần cải thiện | Áp dụng Whitelist Pattern     |
| Optimization | Khởi tạo Dependency bằng new            | ⚠ Cần cải thiện | Áp dụng Constructor Injection |

---

# 6. BẢNG TỔNG KẾT TEST CASE (CRUD)

| ID      | Chức năng         | Hành động kiểm thử              | Kết quả mong đợi                                   |
| ------- | ----------------- | ------------------------------- | -------------------------------------------------- |
| TC_C_02 | Thêm khóa học     | Thêm khóa học có tên đã tồn tại | Hiển thị lỗi: "Tên khóa học đã tồn tại"            |
| TC_D_02 | Xóa khóa học      | Xóa khóa học đã có học viên     | Hiển thị lỗi: "Không thể xóa khóa học có học viên" |
| TC_U_01 | Cập nhật khóa học | Sửa dữ liệu hợp lệ              | Lưu thành công vào Database                        |
| TC_S_01 | Tìm kiếm khóa học | Nhập một phần tên khóa học      | Hiển thị đúng danh sách kết quả lọc                |

---

# 7. KẾT LUẬN KIỂM THỬ

## Kết quả tổng quan

| Tiêu chí                   | Kết quả             |
| -------------------------- | ------------------- |
| Thêm khóa học              | ✅ Pass              |
| Sửa khóa học               | ✅ Pass              |
| Xóa khóa học               | ✅ Pass              |
| Tìm kiếm khóa học          | ✅ Pass              |
| Kiểm tra ràng buộc dữ liệu | ✅ Pass              |
| Bảo mật SQL                | ⚠ Cần cải thiện nhỏ |
| Clean Code                 | ⚠ Cần tối ưu nhỏ    |

---

## Đánh giá cuối cùng

Module Quản lý Khóa học hoạt động ổn định và đáp ứng đầy đủ yêu cầu nghiệp vụ.

Không phát hiện lỗi nghiêm trọng ảnh hưởng đến dữ liệu hoặc tính toàn vẹn của hệ thống.

Điểm mạnh nổi bật:

* CRUD hoạt động chính xác.
* Đã chặn xóa khóa học có học viên.
* Thông báo lỗi nghiệp vụ rõ ràng.
* Luồng dữ liệu nhất quán giữa UI, Business và DAO.

Các cải tiến còn lại thuộc nhóm Optimization:

* Áp dụng Whitelist Pattern cho truy vấn sắp xếp.
* Chuẩn hóa Dependency Injection.
* Tăng khả năng Unit Testing và bảo trì hệ thống.

### Trạng thái QA

**PASS ✅**

Module đủ điều kiện chuyển sang giai đoạn tích hợp hệ thống (Integration Testing) hoặc kiểm thử toàn hệ thống (System Testing).
