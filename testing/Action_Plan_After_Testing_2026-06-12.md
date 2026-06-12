# 🚀 ACTION PLAN - KẾ HOẠCH HÀNH ĐỘNG CẢI TIẾN HỆ THỐNG

**Ngày thực hiện:** 12/06/2026
**Người thực hiện:** Technical Lead
**Phạm vi:** Tổng hợp kết quả kiểm thử từ các module:

* Authentication (Auth)
* Course Management
* Student Management
* Enrollment Management
* Statistics & Recommendation

---

# 1. MỤC TIÊU

Tài liệu này tổng hợp các phát hiện từ quá trình kiểm thử hệ thống nhằm:

* Xác định các điểm yếu nghiêm trọng cần xử lý.
* Đưa ra kế hoạch refactor ưu tiên.
* Nâng cao tính toàn vẹn dữ liệu.
* Tăng cường bảo mật hệ thống.
* Cải thiện hiệu năng cơ sở dữ liệu.
* Chuẩn hóa kiến trúc theo SOLID và Clean Architecture.
* Chuẩn bị hệ thống cho giai đoạn triển khai thực tế.

---

# 2. TỔNG QUAN CÁC ĐIỂM CHẾT (CRITICAL POINTS)

## 2.1 Danh sách vấn đề ưu tiên

| ID  | Module        | Mức độ          | Vấn đề phát hiện                             | Hậu quả                         |
| --- | ------------- | --------------- | -------------------------------------------- | ------------------------------- |
| C01 | Enrollment    | 🔴 Critical     | Thiếu Transaction khi duyệt đơn              | Dữ liệu không đồng nhất         |
| C02 | Enrollment    | 🔴 Critical     | Cho phép xóa đơn CONFIRM                     | Mất lịch sử đăng ký             |
| C03 | Student       | 🔴 Critical     | Xóa học viên có dữ liệu liên quan gây lỗi FK | Crash ứng dụng                  |
| C04 | Statistics    | 🔴 Critical     | Thiếu Index Database                         | Hiệu năng suy giảm nghiêm trọng |
| W01 | Course        | 🟠 Warning      | SQL Injection tiềm ẩn trong ORDER BY         | Rủi ro bảo mật                  |
| O01 | Toàn hệ thống | 🟡 Optimization | Khởi tạo phụ thuộc bằng new                  | Vi phạm SOLID                   |

---

# 3. KẾ HOẠCH TRIỂN KHAI

## Giai đoạn 1 - Critical Fixes (Bắt buộc)

### Ưu tiên cao nhất

* Task 1: Enrollment Transaction Management
* Task 2: Student Safe Delete
* Task 3: Course SQL Injection Protection

Mục tiêu:

* Bảo vệ dữ liệu.
* Ngăn chặn lỗi hệ thống.
* Đảm bảo an toàn bảo mật.

---

## Giai đoạn 2 - Performance Improvements

### Ưu tiên trung bình

* Task 4: Database Indexing

Mục tiêu:

* Tăng tốc báo cáo.
* Tránh Full Table Scan.
* Chuẩn bị dữ liệu lớn.

---

## Giai đoạn 3 - Architecture Refactoring

### Ưu tiên dài hạn

* Task 5: Dependency Injection

Mục tiêu:

* Tuân thủ SOLID.
* Tăng khả năng Unit Test.
* Giảm Tight Coupling.

---

# 4. TASK 01 - TRANSACTION & BUSINESS RULE CHO ENROLLMENT

## Thông tin

| Thuộc tính | Giá trị                     |
| ---------- | --------------------------- |
| ID         | TASK-01                     |
| Module     | Enrollment                  |
| Mức độ     | 🔴 Critical                 |
| File chính | EnrollmentBusinessImpl.java |

---

## Phân tích

Quá trình duyệt đơn hiện gồm nhiều thao tác:

1. Cập nhật trạng thái đơn.
2. Tăng số lượng học viên khóa học.

Nếu một bước thất bại:

* Dữ liệu không đồng bộ.
* Báo cáo sai lệch.
* Vi phạm nguyên tắc ACID.

Ngoài ra, hệ thống chưa ngăn chặn việc xóa đơn đã xác nhận.

---

## Hướng xử lý

### Transaction Management

```java
public void approveEnrollment(Long enrollmentId)
        throws Exception {

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

        throw new BusinessException(
                "Lỗi duyệt đơn, dữ liệu đã rollback.",
                e
        );

    } finally {

        conn.setAutoCommit(true);
    }
}
```

### Chặn xóa đơn CONFIRM

```java
public void deleteEnrollment(Long enrollmentId) {

    Enrollment enroll =
            enrollmentDAO.findById(enrollmentId);

    if (enroll.getStatus()
            == EnrollmentStatus.CONFIRM) {

        throw new BusinessException(
                "Không thể xóa đơn đã được xác nhận."
        );
    }

    enrollmentDAO.deleteById(enrollmentId);
}
```

---

## Kết quả mong đợi

* Đảm bảo tính nguyên tử dữ liệu.
* Ngăn chặn mất lịch sử đăng ký.
* Tuân thủ ACID.

---

# 5. TASK 02 - SAFE DELETE CHO STUDENT

## Thông tin

| Thuộc tính | Giá trị                  |
| ---------- | ------------------------ |
| ID         | TASK-02                  |
| Module     | Student                  |
| Mức độ     | 🔴 Critical              |
| File chính | StudentBusinessImpl.java |

---

## Phân tích

Hiện tại việc xóa học viên phụ thuộc hoàn toàn vào Foreign Key của Database.

Khi học viên đã đăng ký khóa học:

* Database phát sinh lỗi.
* Người dùng nhận Exception khó hiểu.
* Có thể gây crash ứng dụng.

---

## Hướng xử lý

```java
public void deleteStudent(Long id) {

    if (enrollmentDAO.existsByStudentId(id)) {

        throw new BusinessException(
                "Không thể xóa học viên: "
                + "Học viên đang có đơn đăng ký khóa học."
        );
    }

    studentDAO.delete(id);
}
```

---

## Kết quả mong đợi

* Không phát sinh Foreign Key Exception.
* Trải nghiệm người dùng tốt hơn.
* Thông báo lỗi rõ ràng.

---

# 6. TASK 03 - BẢO MẬT ORDER BY BẰNG WHITELIST

## Thông tin

| Thuộc tính | Giá trị            |
| ---------- | ------------------ |
| ID         | TASK-03            |
| Module     | Course             |
| Mức độ     | 🟠 Warning         |
| File chính | CourseDAOImpl.java |

---

## Phân tích

PreparedStatement không bảo vệ được:

```sql
ORDER BY columnName
```

Do đó nếu nối chuỗi trực tiếp sẽ có nguy cơ SQL Injection.

---

## Hướng xử lý

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

    String safeField =
            allowedFields.contains(
                    sortField.toLowerCase()
            )
            ? sortField
            : "name";

    String safeDir =
            "DESC".equalsIgnoreCase(direction)
            ? "DESC"
            : "ASC";

    String sql =
            "SELECT * FROM courses ORDER BY "
                    + safeField
                    + " "
                    + safeDir;

    // Execute Query
}
```

---

## Kết quả mong đợi

* Loại bỏ nguy cơ SQL Injection.
* Đảm bảo truy vấn hợp lệ.

---

# 7. TASK 04 - TỐI ƯU DATABASE INDEXING

## Thông tin

| Thuộc tính | Giá trị       |
| ---------- | ------------- |
| ID         | TASK-04       |
| Module     | Statistics    |
| Mức độ     | 🔴 Critical   |
| File chính | Migration SQL |

---

## Phân tích

Các truy vấn thống kê thường xuyên:

* JOIN enrollments và courses
* GROUP BY course_id
* WHERE status

Hiện chưa có Index phù hợp.

---

## Hướng xử lý

Tạo file migration:

```sql
-- V2__add_indexes.sql

CREATE INDEX idx_enrollment_course_id
ON enrollments(course_id);

CREATE INDEX idx_enrollment_status
ON enrollments(status);
```

---

## Kết quả mong đợi

* Giảm Full Table Scan.
* Tăng tốc truy vấn thống kê.
* Hỗ trợ dữ liệu lớn.

---

# 8. TASK 05 - DEPENDENCY INJECTION REFACTOR

## Thông tin

| Thuộc tính | Giá trị                |
| ---------- | ---------------------- |
| ID         | TASK-05                |
| Module     | System-wide            |
| Mức độ     | 🟡 Optimization        |
| File chính | Các lớp UI và Business |

---

## Phân tích

Nhiều lớp đang sử dụng:

```java
new CourseBusinessImpl()
new StudentBusinessImpl()
new EnrollmentBusinessImpl()
```

Điều này dẫn đến:

* Tight Coupling
* Khó Unit Test
* Vi phạm Dependency Inversion Principle

---

## Hướng xử lý

### Trước

```java
private CourseBusinessImpl courseBusiness =
        new CourseBusinessImpl();
```

### Sau

```java
public class CourseUI {

    private final ICourseBusiness courseBusiness;

    public CourseUI(
            ICourseBusiness courseBusiness
    ) {
        this.courseBusiness = courseBusiness;
    }
}
```

---

## Kết quả mong đợi

* Tuân thủ SOLID.
* Hỗ trợ Mock Testing.
* Dễ bảo trì.

---

# 9. KHUYẾN NGHỊ CHO TEAM PHÁT TRIỂN

## Thứ tự thực hiện

### Sprint 1

* TASK-01
* TASK-02
* TASK-03

### Sprint 2

* TASK-04

### Sprint 3

* TASK-05

---

## Database Migration

Không thực thi trực tiếp trên Database Production.

Bắt buộc:

* Flyway Migration
* Liquibase Migration

Ví dụ:

```text
V1__init_schema.sql
V2__add_indexes.sql
V3__constraints_update.sql
```

---

## Composition Root

Sau khi áp dụng Dependency Injection:

Tạo lớp:

```java
ApplicationContext
```

hoặc:

```java
ApplicationFactory
```

để khởi tạo và quản lý toàn bộ dependency của hệ thống.

---

# 10. KẾT LUẬN

Qua quá trình tổng hợp 5 báo cáo kiểm thử, hệ thống hiện tại đã đáp ứng tốt các yêu cầu nghiệp vụ cốt lõi. Tuy nhiên vẫn tồn tại một số điểm yếu liên quan đến:

* Tính toàn vẹn dữ liệu.
* Hiệu năng Database.
* Bảo mật truy vấn động.
* Kiến trúc phần mềm.

Ưu tiên xử lý ngay các vấn đề Critical trong module Enrollment, Student và Statistics trước khi triển khai thực tế.

## Trạng thái tổng thể

| Tiêu chí             | Đánh giá                      |
| -------------------- | ----------------------------- |
| Business Logic       | ✅ Tốt                         |
| Security             | ⚠ Cần cải thiện nhỏ           |
| Data Integrity       | ⚠ Cần cải thiện               |
| Performance          | ⚠ Cần tối ưu                  |
| Architecture         | ⚠ Cần refactor                |
| Deployment Readiness | ⚠ Chưa khuyến nghị Production |

### Đánh giá cuối cùng

**PASS WITH CRITICAL IMPROVEMENTS REQUIRED ⚠️**

Hệ thống đủ điều kiện tiếp tục phát triển và tích hợp, nhưng cần hoàn thành các nhiệm vụ ưu tiên trước khi triển khai Production.
