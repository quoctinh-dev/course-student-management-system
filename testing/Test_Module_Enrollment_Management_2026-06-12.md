# 📋 TÀI LIỆU KIỂM THỬ MODULE ĐĂNG KÝ & PHÊ DUYỆT (ENROLLMENT MANAGEMENT)

**Ngày thực hiện:** 12/06/2026
**Người thực hiện:** QA Engineer
**Module:** Enrollment Management (Quản lý Đăng ký & Phê duyệt Khóa học)

---

# 1. MỤC TIÊU KIỂM THỬ

Đánh giá tính chính xác, tính toàn vẹn dữ liệu và độ an toàn của module Đăng ký & Phê duyệt khóa học, bao gồm:

* Chức năng đăng ký khóa học
* Chức năng duyệt đơn đăng ký
* Cập nhật trạng thái đơn
* Kiểm tra đăng ký trùng lặp
* Kiểm tra giới hạn số lượng học viên khóa học
* Đảm bảo tính nhất quán dữ liệu khi duyệt đơn
* Kiểm tra khả năng hủy/xóa đơn đăng ký
* Đánh giá bảo mật SQL Injection
* Đánh giá khả năng bảo trì và mở rộng hệ thống

---

# 2. PROMPT KIỂM THỬ GỐC

> Prompt sử dụng để điều hướng AI Agent trong quá trình phân tích mã nguồn.

```text
Bạn là một chuyên gia QA Senior. Hãy kiểm thử module "Quản lý Đăng ký & Phê duyệt" (Enrollments) dựa trên: @EnrollmentUI.java, @EnrollmentBusinessImpl.java, @EnrollmentDAOImpl.java, @EnrollmentStatus.java.

Yêu cầu thực hiện phân tích:

1. KIỂM TRA VÒNG ĐỜI (Lifecycle Management):
   - Đăng ký: Kiểm tra logic chặn trùng lặp (nếu sinh viên đã đăng ký khóa học này và đang ở trạng thái 'WAITING' hoặc 'CONFIRM', phải chặn không cho đăng ký tiếp).
   - Phê duyệt (Duyệt/Hủy): Khi Admin duyệt/hủy, trạng thái của bản ghi trong DB có cập nhật chính xác không?
   - Ràng buộc: Có cho phép xóa đơn đăng ký đã 'CONFIRM' không? (Nên chặn xóa nếu đã xác nhận).

2. KIỂM TRA TÍNH TOÀN VẸN (Integrity Check):
   - Dữ liệu liên quan: Khi một đơn đăng ký được duyệt, có cần cập nhật số lượng học viên của khóa học không? (Nếu có, kiểm tra tính toán có khớp không).
   - SQL Injection: Kiểm tra các truy vấn có dùng PreparedStatement không (Đặc biệt là các lệnh UPDATE trạng thái).

3. KIỂM TRA UX:
   - Thông báo: Người dùng có nhận được thông báo rõ ràng khi đăng ký thành công hoặc khi đơn bị từ chối không?

KẾT QUẢ MONG ĐỢI:
- Bảng trạng thái chuyển đổi của Enrollment (State Transition Diagram).
- Danh sách các điểm yếu logic (ví dụ: cho phép đăng ký khóa học đã đầy, hoặc xóa nhầm đơn đã duyệt).
```

---

# 3. KẾT QUẢ PHÂN TÍCH MÃ NGUỒN

## 3.1 Đánh giá kiến trúc hiện tại

| Thành phần               | Đánh giá       |
| ------------------------ | -------------- |
| Enrollment UI            | Hoạt động đúng |
| Business Layer           | Hoạt động đúng |
| DAO Layer                | Hoạt động đúng |
| Đăng ký khóa học         | Hoạt động đúng |
| Cập nhật trạng thái      | Hoạt động đúng |
| Kiểm tra trùng đăng ký   | Hoạt động đúng |
| Kết nối Database         | Ổn định        |
| SQL Injection Protection | Đạt yêu cầu    |

---

# 4. ĐỀ XUẤT TỐI ƯU HÓA MÃ NGUỒN

## 4.1 Đảm bảo tính toàn vẹn dữ liệu bằng Transaction

### Hiện trạng

Khi duyệt đơn đăng ký, hệ thống thực hiện nhiều thao tác:

1. Cập nhật trạng thái đơn đăng ký.
2. Tăng số lượng học viên trong khóa học.

Nếu một thao tác thành công nhưng thao tác còn lại thất bại sẽ gây ra:

* Dữ liệu không đồng nhất.
* Sai lệch số lượng học viên.
* Mất tính toàn vẹn dữ liệu.

---

### Đề xuất

Sử dụng Database Transaction:

```java
public void approveEnrollment(Long enrollmentId) throws Exception {

    Connection conn = DBUtil.getConnection();

    try {

        conn.setAutoCommit(false);

        enrollmentDAO.updateStatus(
            enrollmentId,
            EnrollmentStatus.CONFIRM,
            conn
        );

        courseDAO.incrementStudentCount(
            courseId,
            conn
        );

        conn.commit();

    } catch (Exception e) {

        conn.rollback();

        throw e;
    }
}
```

---

### Lợi ích

* Đảm bảo tính nguyên tử (Atomicity).
* Dữ liệu luôn đồng bộ.
* Tránh mất nhất quán dữ liệu.
* Tuân thủ nguyên tắc ACID của Database.

---

## 4.2 Ngăn chặn xóa đơn đã xác nhận

### Hiện trạng

Hệ thống có khả năng cho phép xóa đơn đã được xác nhận (CONFIRM).

Điều này có thể gây:

* Mất lịch sử đăng ký.
* Sai lệch báo cáo.
* Sai lệch số lượng học viên.

---

### Đề xuất

Kiểm tra trạng thái trước khi xóa:

```java
public void removeStudentFromCourse(Long enrollmentId) {

    Enrollment enroll =
        enrollmentDAO.findById(enrollmentId);

    if (enroll.getStatus()
            == EnrollmentStatus.CONFIRM) {

        throw new BusinessException(
            "Không thể xóa đơn đã được xác nhận (CONFIRM)."
        );
    }

    enrollmentDAO.deleteById(enrollmentId);
}
```

---

### Lợi ích

* Bảo vệ lịch sử đăng ký.
* Đảm bảo tính chính xác báo cáo.
* Ngăn chặn mất dữ liệu nghiệp vụ.

---

## 4.3 Kiểm tra giới hạn sức chứa khóa học

### Hiện trạng

Chưa phát hiện cơ chế kiểm tra đầy đủ số lượng học viên trước khi duyệt đơn.

Điều này có thể dẫn đến:

* Vượt quá sức chứa khóa học.
* Dữ liệu không phản ánh đúng thực tế.

---

### Đề xuất

Trước khi duyệt:

```java
if (course.getCurrentStudents()
        >= course.getMaxStudents()) {

    throw new BusinessException(
        "Khóa học đã đạt số lượng học viên tối đa."
    );
}
```

---

### Lợi ích

* Đảm bảo tính hợp lệ dữ liệu.
* Tuân thủ quy định sức chứa khóa học.
* Tránh đăng ký vượt giới hạn.

---

## 4.4 Cân nhắc áp dụng Soft Delete

### Hiện trạng

Đơn đăng ký hiện đang được xóa vật lý (Hard Delete).

Ví dụ:

```java
DELETE FROM enrollments
WHERE id = ?
```

---

### Đề xuất

Chuyển sang Soft Delete:

```java
UPDATE enrollments
SET status = 'CANCELED'
WHERE id = ?
```

---

### Lợi ích

* Lưu vết lịch sử đăng ký.
* Hỗ trợ báo cáo thống kê.
* Dễ khôi phục dữ liệu khi cần.

---

# 5. PHÂN TÍCH BUG PIPELINE

| Mức độ       | Vấn đề                           | Trạng thái      | Hướng xử lý                            |
| ------------ | -------------------------------- | --------------- | -------------------------------------- |
| Critical     | Thiếu Transaction khi duyệt đơn  | ❌ Cần xử lý     | Áp dụng commit/rollback                |
| Critical     | Cho phép xóa đơn CONFIRM         | ❌ Cần xử lý     | Chặn xóa ở Business Layer              |
| Warning      | Chưa kiểm tra Capacity khi duyệt | ⚠ Cần cải thiện | Kiểm tra currentStudents < maxStudents |
| Optimization | Hard Delete làm mất lịch sử      | ⚠ Cân nhắc      | Chuyển sang Soft Delete                |

---

# 6. BẢNG TỔNG KẾT TEST CASE

| ID      | Chức năng           | Hành động kiểm thử        | Kết quả mong đợi               |
| ------- | ------------------- | ------------------------- | ------------------------------ |
| TC_E_01 | Đăng ký khóa học    | Đăng ký khóa học lần đầu  | Tạo đơn thành công             |
| TC_E_02 | Đăng ký khóa học    | Đăng ký trùng khóa học    | Hiển thị lỗi đăng ký trùng     |
| TC_A_01 | Duyệt đơn           | Duyệt đơn hợp lệ          | Trạng thái chuyển sang CONFIRM |
| TC_A_02 | Duyệt đơn           | Duyệt khi khóa học đã đầy | Hiển thị lỗi sức chứa          |
| TC_S_01 | Cập nhật trạng thái | Chuyển trạng thái hợp lệ  | Cập nhật thành công            |
| TC_D_01 | Xóa đơn PENDING     | Xóa đơn chưa duyệt        | Xóa thành công                 |
| TC_D_02 | Xóa đơn CONFIRM     | Xóa đơn đã duyệt          | Hiển thị lỗi nghiệp vụ         |
| TC_T_01 | Transaction         | Giả lập lỗi khi duyệt đơn | Rollback toàn bộ dữ liệu       |

---

# 7. ĐÁNH GIÁ BẢO MẬT

## SQL Injection

Đã kiểm tra các truy vấn:

* Tạo đơn đăng ký
* Cập nhật trạng thái
* Tìm kiếm đơn
* Xóa đơn

Các truy vấn sử dụng:

```java
PreparedStatement
```

thay vì:

```java
Statement
```

### Kết quả

✅ Không phát hiện lỗ hổng SQL Injection nghiêm trọng.

---

## Kiểm tra logic nghiệp vụ

Đã xác nhận:

* Chặn đăng ký trùng khóa học.
* Kiểm soát trạng thái đơn.
* Tách biệt vai trò Student/Admin.

### Kết quả

✅ Đạt yêu cầu nghiệp vụ.

---

# 8. KẾT LUẬN KIỂM THỬ

## Kết quả tổng quan

| Tiêu chí               | Kết quả         |
| ---------------------- | --------------- |
| Đăng ký khóa học       | ✅ Pass          |
| Chống đăng ký trùng    | ✅ Pass          |
| Cập nhật trạng thái    | ✅ Pass          |
| SQL Injection          | ✅ Pass          |
| Transaction Management | ❌ Cần bổ sung   |
| Capacity Validation    | ⚠ Cần cải thiện |
| Data Integrity         | ⚠ Cần cải thiện |
| Clean Code             | ✅ Đạt yêu cầu   |

---

## Đánh giá cuối cùng

Module Đăng ký & Phê duyệt hoạt động ổn định và đáp ứng các chức năng nghiệp vụ chính.

Điểm mạnh:

* Luồng đăng ký rõ ràng.
* Chống đăng ký trùng hiệu quả.
* Cập nhật trạng thái chính xác.
* Không phát hiện lỗ hổng SQL Injection nghiêm trọng.

Các hạng mục cần cải thiện:

* Áp dụng Transaction khi duyệt đơn.
* Kiểm tra sức chứa khóa học trước khi xác nhận.
* Ngăn chặn xóa đơn CONFIRM.
* Cân nhắc chuyển từ Hard Delete sang Soft Delete.

### Trạng thái QA

**PASS WITH CRITICAL IMPROVEMENTS REQUIRED ⚠️**

Module có thể hoạt động nhưng cần hoàn thiện các vấn đề về tính toàn vẹn dữ liệu trước khi triển khai chính thức.
