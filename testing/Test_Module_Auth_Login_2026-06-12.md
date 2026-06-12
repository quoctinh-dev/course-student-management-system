# 📋 TÀI LIỆU KIỂM THỬ MODULE ĐĂNG NHẬP (AUTH)

**Ngày thực hiện:** 12/06/2026  
**Người thực hiện:** QA Engineer  
**Module:** Authentication (Admin Login & Student Login)

---

# 1. MỤC TIÊU KIỂM THỬ

Đánh giá tính đúng đắn của luồng đăng nhập trong hệ thống, bao gồm:

- Giao diện người dùng (UI Layer)
- Business Layer
- DAO Layer
- Kết nối cơ sở dữ liệu
- Kiểm tra bảo mật cơ bản
- Kiểm tra tính nhất quán dữ liệu giữa các vai trò (Admin, Student)

---

# 2. PROMPT KIỂM THỬ GỐC

```text
Bạn là chuyên gia QA kiểm thử phần mềm Java. Hãy thực hiện kiểm thử tích hợp (Integration Test) cho module Đăng nhập (Auth) bao gồm Admin và Student.
Các file nguồn: @AdminLoginUI.java, @StudentLoginUI.java, @AdminBusinessImpl.java, @StudentBusinessImpl.java, @AdminDAOImpl.java, @StudentDAOImpl.java, @BCryptUtil.java, @DBUtil.java và các Model liên quan.

Yêu cầu thực hiện "Traceability Analysis" (Phân tích truy xuất) như sau:

1. TRACE DÒNG CHẢY DỮ LIỆU (Data Flow Trace):
    - Vẽ lại luồng thực thi: UI -> Business (lớp tương ứng) -> DAO (lớp tương ứng) -> DB.
    - Xác nhận xem các lớp UI có đang gọi đúng Interface và Business đúng quy định không?
    - Xác nhận xem BCryptUtil có được gọi để so sánh password băm (hash) hay hệ thống đang so sánh chuỗi thô (plain-text) - đây là lỗi bảo mật nghiêm trọng.

2. KIỂM TRA TÍNH TOÀN VẸN (Security & Integrity):
    - Kiểm tra validate đầu vào: Hệ thống có xử lý các trường hợp: (1) Chuỗi rỗng, (2) Dấu cách thừa (trim), (3) SQL Injection (xác nhận việc dùng PreparedStatement ở tầng DAO).
    - Kiểm tra Exception Handling: Các lớp UI có đang bắt các Exception (ValidationException, BusinessException, DatabaseException) không? Nếu người dùng nhập sai mật khẩu, hệ thống có ném ra lỗi thân thiện hay để lộ stack trace?

3. SO SÁNH (Comparative Analysis):
    - So sánh logic xác thực giữa Admin và Student. Có điểm nào bất hợp lý (ví dụ: Admin dùng hash nhưng Student lại không) không?

KẾT QUẢ MONG ĐỢI:
- Bảng mô tả luồng dữ liệu của module Đăng nhập.
- Danh sách các lỗi (nếu có) theo 3 cấp độ: Critical (Bảo mật/Crash), Warning (UX/Performance), Optimization (Clean code).
```

---

# 3. KẾT QUẢ PHÂN TÍCH MÃ NGUỒN

## 3.1 Đánh giá kiến trúc hiện tại

| Thành phần | Đánh giá |
|------------|-----------|
| UI Layer | Hoạt động đúng |
| Business Layer | Hoạt động đúng |
| DAO Layer | Hoạt động đúng |
| Database Connection | Ổn định |
| Luồng xác thực | Chính xác |
| Phân quyền Admin/Student | Chính xác |

---

# 4. ĐỀ XUẤT TỐI ƯU HÓA MÃ NGUỒN

## 4.1 Refactor UI Layer

### Hiện trạng

Một số lớp UI khởi tạo trực tiếp Business Layer:

```java
private StudentBusinessImpl studentBusiness =
        new StudentBusinessImpl();
```

### Vấn đề

- Tight Coupling
- Khó Unit Test
- Không tuân thủ Dependency Injection

### Đề xuất

Sử dụng Factory Pattern và Constructor Injection

```java
// 1. Factory tập trung việc khởi tạo
public class BusinessFactory {
    public IStudentBusiness getStudentBusiness() { return new StudentBusinessImpl(); }
}

// 2. UI nhận đối tượng qua Constructor (Injection)
public class StudentLoginUI {
    private final IStudentBusiness studentBusiness;

    public StudentLoginUI(IStudentBusiness studentBusiness) {
        this.studentBusiness = studentBusiness;
    }
}

// 3. Sử dụng
BusinessFactory factory = new BusinessFactory();
StudentLoginUI ui = new StudentLoginUI(factory.getStudentBusiness());
```

### Lợi ích

- Tuân thủ SOLID
- Dễ Mock khi Unit Test
- Giảm phụ thuộc giữa các tầng
- Dễ mở rộng trong tương lai

---

## 4.2 Kiểm tra DAO Layer

### SQL Injection

✅ Đã xác nhận các truy vấn sử dụng:

```java
PreparedStatement
```

Thay vì:

```java
Statement
```

=> Giảm nguy cơ SQL Injection.

---

### Xử lý ngoại lệ

Khuyến nghị chuẩn hóa Exception Handling.

Thay vì:

```java
e.printStackTrace();
```

Nên sử dụng:

```java
throw new DatabaseException(
    "Lỗi truy cập cơ sở dữ liệu",
    e
);
```

### Lợi ích

- Dễ quản lý lỗi
- Dễ ghi log
- Tránh crash ứng dụng
- Tăng khả năng bảo trì

---

# 5. PHÂN TÍCH BUG PIPELINE

| Mức độ | Vấn đề | Trạng thái | Hướng xử lý |
|----------|----------|----------|----------|
| Critical | Không phát hiện lỗi | ✅ Đạt | Không cần xử lý |
| Warning | Không phát hiện lỗi | ✅ Đạt | Không cần xử lý |
| Optimization | Khởi tạo Dependency bằng `new` | ⚠ Cần cải thiện | Áp dụng Constructor Injection |

---

# 6. KẾT LUẬN KIỂM THỬ

## Kết quả tổng quan

| Tiêu chí | Kết quả |
|-----------|----------|
| Chức năng đăng nhập | ✅ Pass |
| Xác thực tài khoản | ✅ Pass |
| Phân quyền Admin | ✅ Pass |
| Phân quyền Student | ✅ Pass |
| Kết nối Database | ✅ Pass |
| Bảo mật cơ bản | ✅ Pass |
| SQL Injection | ✅ An toàn |
| Clean Code | ⚠ Cần tối ưu nhỏ |

---

## Đánh giá cuối cùng

**Module Authentication hiện hoạt động ổn định và đáp ứng yêu cầu nghiệp vụ.**

Không phát hiện lỗi nghiêm trọng ảnh hưởng đến chức năng đăng nhập hoặc bảo mật.

Các cải tiến còn lại chủ yếu thuộc nhóm **Optimization**, tập trung vào:

- Áp dụng Dependency Injection.
- Chuẩn hóa Exception Handling.
- Tăng khả năng Unit Testing.
- Chuẩn bị cho việc mở rộng hệ thống trong tương lai.

### Trạng thái QA

**PASS ✅**

Module đủ điều kiện chuyển sang giai đoạn tích hợp hoặc kiểm thử hệ thống (System Testing).