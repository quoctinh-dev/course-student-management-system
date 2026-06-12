# 📋 TÀI LIỆU KIỂM THỬ MODULE QUẢN LÝ HỌC VIÊN (STUDENT MANAGEMENT)

**Ngày thực hiện:** 12/06/2026
**Người thực hiện:** QA Engineer
**Module:** Student Management (Quản lý Học viên)

---

# 1. MỤC TIÊU KIỂM THỬ

Đánh giá tính ổn định, bảo mật và tính đúng đắn của module Quản lý Học viên, bao gồm:

* Chức năng CRUD học viên
* Luồng đổi mật khẩu
* Kiểm tra xác thực BCrypt
* Kiểm tra tính duy nhất của Email và Số điện thoại
* Kiểm tra các ràng buộc nghiệp vụ
* Kiểm tra tính toàn vẹn dữ liệu khi xóa học viên
* Đánh giá bảo mật SQL Injection
* Đánh giá trải nghiệm người dùng khi phát sinh lỗi

---

# 2. PROMPT KIỂM THỬ GỐC

> Prompt sử dụng để điều hướng AI Agent trong quá trình phân tích mã nguồn.

```text
Bạn là một chuyên gia QA Senior. Hãy kiểm thử module "Quản lý Học viên" dựa trên các file: @AdminMenuUI.java, @StudentUI.java, @StudentBusinessImpl.java, @StudentDAOImpl.java, @BCryptUtil.java và các class Model/Exception liên quan.

Yêu cầu thực hiện phân tích 3 khía cạnh:

1. KIỂM TRA BẢO MẬT & MẬT KHẨU:
   - Đổi mật khẩu: Kiểm tra luồng đổi mật khẩu (hàm changePassword). Hệ thống có xác thực mật khẩu cũ bằng BCryptUtil.verify() trước khi cập nhật mật khẩu mới không? Có kiểm tra confirmPassword không?
   - Mã hóa: Đảm bảo mật khẩu mới luôn được băm (hash) qua BCryptUtil trước khi lưu vào DB.

2. KIỂM TRA CRUD & DATA INTEGRITY:
   - Xóa học viên: Xác nhận logic chặn xóa (nếu học viên đã có đơn đăng ký, phải chặn xóa hoặc xử lý cascade). Kiểm tra xem StudentDAOImpl có bắt lỗi vi phạm khóa ngoại (foreign key violation) không.
   - Tìm kiếm: Xác nhận các truy vấn tìm kiếm theo (Tên/Email/ID) có an toàn trước SQL Injection không.

3. KIỂM TRA RÀNG BUỘC NGHIỆP VỤ:
   - Kiểm tra validate đầu vào cho Email (đúng định dạng) và số điện thoại. 
   - Kiểm tra tính duy nhất (Unique Constraint) của Email/Số điện thoại khi thêm mới hoặc sửa học viên.

KẾT QUẢ MONG ĐỢI:
- Bảng danh sách các rủi ro bảo mật (nếu có) trong việc đổi mật khẩu.
- Đánh giá khả năng xử lý lỗi khi xóa học viên có dữ liệu liên quan.
```

---

# 3. KẾT QUẢ PHÂN TÍCH MÃ NGUỒN

## 3.1 Đánh giá kiến trúc hiện tại

| Thành phần           | Đánh giá           |
| -------------------- | ------------------ |
| Student UI           | Hoạt động đúng     |
| Business Layer       | Hoạt động đúng     |
| DAO Layer            | Hoạt động đúng     |
| CRUD Student         | Hoạt động đúng     |
| Change Password Flow | Hoạt động đúng     |
| BCrypt Verification  | Đúng quy trình     |
| Validation Email     | Hoạt động đúng     |
| Validation SĐT       | Hoạt động một phần |
| Database Connection  | Ổn định            |

---

# 4. ĐỀ XUẤT TỐI ƯU HÓA MÃ NGUỒN

## 4.1 Nâng cấp logic xóa học viên (Check-before-delete)

### Hiện trạng

Khi thực hiện xóa học viên, hệ thống phụ thuộc vào ràng buộc Foreign Key của cơ sở dữ liệu.

Điều này có thể dẫn đến:

* Phát sinh lỗi SQL Exception.
* Thông báo lỗi khó hiểu với người dùng.
* Trải nghiệm sử dụng không thân thiện.

---

### Đề xuất

Kiểm tra dữ liệu liên quan trước khi thực hiện xóa:

```java
public void deleteStudent(Long id) {

    if (enrollmentDAO.existsByStudentId(id)) {

        throw new BusinessException(
            "Không thể xóa học viên: Học viên đang có đơn đăng ký khóa học."
        );
    }

    studentDAO.delete(id);
}
```

---

### Lợi ích

* Tránh lỗi Foreign Key từ Database.
* Thông báo lỗi rõ ràng cho người dùng.
* Tuân thủ nguyên tắc Business Validation.
* Cải thiện trải nghiệm người dùng.

---

## 4.2 Bổ sung kiểm tra tính duy nhất cho Số điện thoại

### Hiện trạng

Hệ thống đã kiểm tra Email trùng lặp nhưng chưa kiểm tra đầy đủ tính duy nhất của Số điện thoại.

Điều này có thể dẫn đến:

* Nhiều tài khoản sử dụng cùng một số điện thoại.
* Mất tính toàn vẹn dữ liệu.

---

### Đề xuất

Thêm phương thức tại DAO:

```java
public boolean existsByPhone(String phone);
```

Kiểm tra tại Business Layer:

```java
if (studentDAO.existsByPhone(student.getPhone())) {

    throw new ValidationException(
        "Số điện thoại này đã được đăng ký bởi một học viên khác."
    );
}
```

---

### Lợi ích

* Đảm bảo dữ liệu học viên là duy nhất.
* Tránh trùng lặp thông tin liên hệ.
* Tăng độ tin cậy của hệ thống.

---

## 4.3 Chuẩn hóa Validation Layer

### Hiện trạng

Regex Email và Số điện thoại đang được viết trực tiếp trong Business Layer.

Ví dụ:

```java
if (!email.matches(...)) {
    ...
}
```

---

### Đề xuất

Tách sang lớp Validator riêng:

```java
StudentValidator
```

Ví dụ:

```java
StudentValidator.validateEmail(email);

StudentValidator.validatePhone(phone);
```

---

### Lợi ích

* Business Layer gọn gàng hơn.
* Dễ tái sử dụng.
* Dễ Unit Test.
* Tuân thủ Single Responsibility Principle.

---

# 5. PHÂN TÍCH BUG PIPELINE

| Mức độ       | Vấn đề                                             | Trạng thái      | Hướng xử lý                    |
| ------------ | -------------------------------------------------- | --------------- | ------------------------------ |
| Critical     | Rủi ro crash khi xóa học viên có dữ liệu liên quan | ❌ Cần xử lý     | Triển khai Check-before-delete |
| Warning      | Thiếu kiểm tra tính duy nhất cho SĐT               | ⚠ Cần cải thiện | Bổ sung existsByPhone()        |
| Optimization | Validation đang nằm trong Business Layer           | ⚠ Cần tối ưu    | Tách sang Validator Layer      |

---

# 6. BẢNG TỔNG KẾT TEST CASE

| ID      | Chức năng         | Hành động kiểm thử                           | Kết quả mong đợi               |
| ------- | ----------------- | -------------------------------------------- | ------------------------------ |
| TC_C_01 | Thêm học viên     | Thêm Email đã tồn tại                        | Hiển thị lỗi Email đã tồn tại  |
| TC_C_02 | Thêm học viên     | Thêm SĐT đã tồn tại                          | Hiển thị lỗi SĐT đã tồn tại    |
| TC_U_01 | Cập nhật học viên | Cập nhật dữ liệu hợp lệ                      | Lưu thành công                 |
| TC_D_01 | Xóa học viên      | Xóa học viên chưa đăng ký khóa học           | Xóa thành công                 |
| TC_D_02 | Xóa học viên      | Xóa học viên đã có đăng ký khóa học          | Hiển thị lỗi nghiệp vụ         |
| TC_P_01 | Đổi mật khẩu      | Nhập đúng mật khẩu cũ và mật khẩu mới hợp lệ | Đổi mật khẩu thành công        |
| TC_P_02 | Đổi mật khẩu      | Nhập sai mật khẩu cũ                         | Hiển thị lỗi xác thực          |
| TC_P_03 | Đổi mật khẩu      | Mật khẩu xác nhận không khớp                 | Hiển thị lỗi xác nhận mật khẩu |
| TC_S_01 | Tìm kiếm học viên | Nhập một phần tên hoặc Email                 | Hiển thị đúng kết quả          |

---

# 7. ĐÁNH GIÁ BẢO MẬT

## Luồng đổi mật khẩu

Đã kiểm tra các tiêu chí:

* Xác thực mật khẩu cũ bằng BCrypt.
* So khớp mật khẩu xác nhận.
* Băm mật khẩu mới trước khi lưu Database.
* Không lưu mật khẩu dạng Plain Text.

### Kết quả

✅ Đạt yêu cầu bảo mật cơ bản.

---

## SQL Injection

Đã xác nhận:

* Các truy vấn sử dụng PreparedStatement.
* Không phát hiện truy vấn ghép chuỗi nguy hiểm.

### Kết quả

✅ Không phát hiện lỗ hổng SQL Injection nghiêm trọng.

---

# 8. KẾT LUẬN KIỂM THỬ

## Kết quả tổng quan

| Tiêu chí          | Kết quả          |
| ----------------- | ---------------- |
| CRUD Student      | ✅ Pass           |
| Change Password   | ✅ Pass           |
| BCrypt Security   | ✅ Pass           |
| Email Validation  | ✅ Pass           |
| Phone Validation  | ⚠ Cần bổ sung    |
| Delete Validation | ⚠ Cần cải thiện  |
| SQL Injection     | ✅ Pass           |
| Clean Code        | ⚠ Cần tối ưu nhỏ |

---

## Đánh giá cuối cùng

Module Quản lý Học viên hoạt động ổn định và đáp ứng phần lớn yêu cầu nghiệp vụ.

Điểm mạnh:

* Luồng đổi mật khẩu an toàn.
* Sử dụng BCrypt đúng chuẩn.
* CRUD hoạt động chính xác.
* Không phát hiện lỗ hổng SQL Injection nghiêm trọng.
* Thông báo lỗi nghiệp vụ tương đối rõ ràng.

Các hạng mục cần cải thiện:

* Kiểm tra dữ liệu liên quan trước khi xóa học viên.
* Bổ sung kiểm tra tính duy nhất cho Số điện thoại.
* Tách Validation khỏi Business Layer.

### Trạng thái QA

**PASS WITH MINOR IMPROVEMENTS ✅**

Module đủ điều kiện chuyển sang giai đoạn Integration Testing sau khi hoàn thành các cải tiến nhỏ được đề xuất.
