# 📋 TÀI LIỆU KIỂM THỬ MODULE THỐNG KÊ & GỢI Ý (STATISTICS & RECOMMENDATION)

**Ngày thực hiện:** 12/06/2026
**Người thực hiện:** QA Engineer
**Module:** Statistics & Recommendation (Thống kê & Gợi ý Khóa học)

---

# 1. MỤC TIÊU KIỂM THỬ

Đánh giá tính chính xác, hiệu năng và khả năng mở rộng của module Thống kê & Gợi ý, bao gồm:

* Truy vấn thống kê dữ liệu hệ thống
* Tổng hợp dữ liệu bằng COUNT, GROUP BY, JOIN
* Đánh giá logic đề xuất khóa học
* Kiểm tra hiệu năng truy vấn trên dữ liệu lớn
* Đánh giá khả năng mở rộng hệ thống báo cáo
* Kiểm tra tối ưu DTO và Data Mapping
* Phân tích rủi ro N+1 Query
* Đánh giá chiến lược Indexing Database

---

# 2. PROMPT KIỂM THỬ GỐC

> Prompt sử dụng để điều hướng AI Agent trong quá trình phân tích mã nguồn.

```text
Bạn là chuyên gia QA Senior.

Hãy kiểm thử module "Thống kê & Đề xuất".

Yêu cầu:

- Phân tích độ chính xác của các truy vấn thống kê.
- Kiểm tra COUNT, GROUP BY, LEFT JOIN.
- Đánh giá tính hợp lý của thuật toán đề xuất.
- Kiểm tra hiệu năng SQL với dữ liệu lớn.
- Đánh giá DTO và khả năng mapping dữ liệu.
- Phát hiện N+1 Query.
- Đánh giá Indexing Strategy.
- Phân tích tính nhất quán giữa tên module và chức năng thực tế.
- Đề xuất cải tiến theo hướng Clean Architecture.
```

---

# 3. KẾT QUẢ PHÂN TÍCH MÃ NGUỒN

## 3.1 Đánh giá kiến trúc hiện tại

| Thành phần           | Đánh giá       |
| -------------------- | -------------- |
| Statistics UI        | Hoạt động đúng |
| Business Layer       | Hoạt động đúng |
| DAO Layer            | Hoạt động đúng |
| DTO Mapping          | Đạt yêu cầu    |
| Aggregation Queries  | Chính xác      |
| GROUP BY Logic       | Chính xác      |
| JOIN Logic           | Chính xác      |
| Pagination           | Hoạt động đúng |
| Recommendation Logic | Chưa đầy đủ    |

---

## 3.2 Đánh giá độ chính xác thống kê

Các nhóm truy vấn đã kiểm tra:

* Tổng số học viên
* Tổng số khóa học
* Tổng số đăng ký
* Thống kê số lượng học viên theo khóa học
* Thống kê khóa học phổ biến
* Báo cáo tỷ lệ đăng ký

### Kết quả

✅ Các phép tính COUNT, SUM, GROUP BY cho kết quả chính xác.

✅ Các truy vấn LEFT JOIN xử lý đúng dữ liệu kể cả khi không có bản ghi liên kết.

---

# 4. ĐỀ XUẤT TỐI ƯU HÓA MÃ NGUỒN

## 4.1 Tối ưu Database bằng Indexing

### Hiện trạng

Các truy vấn thống kê thường xuyên thực hiện:

* JOIN giữa courses và enrollments
* Lọc theo trạng thái đơn đăng ký
* GROUP BY course_id

Khi dữ liệu tăng lên hàng chục nghìn hoặc hàng trăm nghìn bản ghi, hệ thống có nguy cơ:

* Truy vấn chậm
* Tăng tải Database
* Gây treo giao diện báo cáo

---

### Đề xuất

Tạo Index trên các cột được sử dụng thường xuyên:

```sql
-- Tăng tốc JOIN

CREATE INDEX idx_enrollment_course_id
ON enrollments(course_id);

-- Tăng tốc FILTER

CREATE INDEX idx_enrollment_status
ON enrollments(status);
```

---

### Lợi ích

* Tăng tốc độ JOIN.
* Giảm Full Table Scan.
* Cải thiện hiệu năng báo cáo.
* Tăng khả năng mở rộng hệ thống.

---

## 4.2 Định danh lại chức năng "Recommendation"

### Hiện trạng

Qua phân tích mã nguồn, chức năng hiện tại chủ yếu thống kê các khóa học phổ biến.

Chưa phát hiện:

* Recommendation Engine
* Personalized Recommendation
* Collaborative Filtering
* Content-Based Recommendation

Điều này dẫn đến sự không nhất quán giữa:

**Tên gọi:** Recommendation

và

**Chức năng thực tế:** Thống kê độ phổ biến.

---

### Đề xuất 1 (Khuyến nghị)

Đổi tên:

```java
RecommendationService
```

thành:

```java
CoursePopularityAnalytics
```

hoặc:

```java
CourseStatisticService
```

---

### Đề xuất 2 (Nếu muốn có chức năng gợi ý thật)

Triển khai Recommendation Logic đơn giản:

```text
1. Lấy danh sách khóa học học viên A đã đăng ký.

2. Tìm các học viên khác có đăng ký tương tự.

3. Lấy các khóa học mà nhóm học viên đó đã đăng ký.

4. Loại bỏ các khóa học học viên A đã học.

5. Trả về danh sách gợi ý.
```

Đây là mô hình Collaborative Filtering cơ bản.

---

### Lợi ích

* Đồng nhất giữa tên gọi và chức năng.
* Tránh hiểu nhầm trong tài liệu dự án.
* Dễ mở rộng thành Recommendation Engine trong tương lai.

---

## 4.3 Kiểm tra N+1 Query

### Hiện trạng

Chưa phát hiện N+1 Query nghiêm trọng.

Tuy nhiên cần theo dõi khi:

* Hiển thị danh sách khóa học lớn.
* Sinh báo cáo chi tiết theo từng khóa học.

---

### Khuyến nghị

Ưu tiên:

```sql
JOIN
GROUP BY
```

thay vì:

```text
Lặp từng Course
→ Query Enrollment riêng
```

---

### Lợi ích

* Giảm số lượng truy vấn.
* Giảm tải Database.
* Tăng tốc độ phản hồi.

---

## 4.4 Tối ưu Pagination

### Hiện trạng

Hệ thống sử dụng:

```sql
LIMIT x OFFSET y
```

Giải pháp này hoạt động tốt với dữ liệu vừa và nhỏ.

---

### Khuyến nghị

Khi dữ liệu lớn:

```sql
WHERE id > lastId
LIMIT 20
```

(Keyset Pagination)

---

### Lợi ích

* Tốc độ ổn định.
* Tránh bỏ sót dữ liệu khi dữ liệu thay đổi liên tục.
* Tối ưu hiệu năng trên bảng lớn.

---

# 5. PHÂN TÍCH BUG PIPELINE

| Mức độ       | Vấn đề                                                  | Trạng thái      | Hướng xử lý                           |
| ------------ | ------------------------------------------------------- | --------------- | ------------------------------------- |
| Critical     | Nguy cơ suy giảm hiệu năng khi dữ liệu lớn              | ❌ Cần xử lý     | Bổ sung Index cho JOIN/FILTER         |
| Warning      | Tên module Recommendation không phản ánh đúng chức năng | ⚠ Cần cải thiện | Đổi tên hoặc bổ sung thuật toán gợi ý |
| Optimization | Pagination sử dụng OFFSET                               | ⚠ Có thể tối ưu | Chuyển sang Keyset Pagination         |

---

# 6. BẢNG TỔNG KẾT TEST CASE

| ID       | Chức năng         | Hành động kiểm thử                 | Kết quả mong đợi                   |
| -------- | ----------------- | ---------------------------------- | ---------------------------------- |
| TC_ST_01 | Tổng số học viên  | Thực hiện thống kê                 | Hiển thị đúng số lượng             |
| TC_ST_02 | Tổng số khóa học  | Thực hiện thống kê                 | Hiển thị đúng số lượng             |
| TC_ST_03 | Khóa học phổ biến | Thống kê theo Enrollment           | Hiển thị đúng thứ tự               |
| TC_ST_04 | Báo cáo JOIN      | Kiểm tra dữ liệu liên kết          | Hiển thị đầy đủ dữ liệu            |
| TC_ST_05 | LEFT JOIN         | Kiểm tra khóa học chưa có học viên | Vẫn hiển thị trong báo cáo         |
| TC_RC_01 | Recommendation    | Kiểm tra logic gợi ý hiện tại      | Trả về danh sách khóa học phổ biến |
| TC_PF_01 | Performance       | Kiểm thử với dữ liệu lớn           | Truy vấn phản hồi ổn định          |
| TC_PG_01 | Pagination        | Chuyển trang liên tục              | Không mất dữ liệu                  |

---

# 7. ĐÁNH GIÁ HIỆU NĂNG & BẢO MẬT

## Hiệu năng SQL

Đã kiểm tra:

* GROUP BY
* LEFT JOIN
* Aggregation Queries
* Pagination

### Kết quả

⚠ Hoạt động tốt ở dữ liệu nhỏ và trung bình.

❌ Cần bổ sung Index để đảm bảo hiệu năng khi mở rộng.

---

## SQL Injection

Đã xác nhận:

* Các truy vấn sử dụng PreparedStatement.
* Không phát hiện SQL động nguy hiểm.

### Kết quả

✅ Không phát hiện lỗ hổng SQL Injection nghiêm trọng.

---

## DTO Optimization

Đã kiểm tra:

* StatisticDTO
* ReportDTO
* RecommendationDTO

### Kết quả

✅ DTO được sử dụng hợp lý, không phát hiện dư thừa nghiêm trọng.

---

# 8. KẾT LUẬN KIỂM THỬ

## Kết quả tổng quan

| Tiêu chí             | Kết quả           |
| -------------------- | ----------------- |
| Statistics Query     | ✅ Pass            |
| Aggregation Logic    | ✅ Pass            |
| DTO Mapping          | ✅ Pass            |
| SQL Injection        | ✅ Pass            |
| Database Performance | ⚠ Cần cải thiện   |
| Recommendation Logic | ⚠ Chưa hoàn chỉnh |
| Pagination           | ✅ Đạt yêu cầu     |
| Clean Code           | ✅ Đạt yêu cầu     |

---

## Đánh giá cuối cùng

Module Thống kê & Gợi ý hoạt động ổn định và cung cấp dữ liệu thống kê chính xác.

Điểm mạnh:

* Các truy vấn thống kê chính xác.
* DTO được thiết kế hợp lý.
* Không phát hiện SQL Injection.
* Không phát hiện N+1 Query nghiêm trọng.

Các hạng mục cần cải thiện:

* Bổ sung Index Database để tránh suy giảm hiệu năng.
* Đồng nhất tên gọi giữa module và chức năng thực tế.
* Xây dựng Recommendation Engine nếu muốn cung cấp tính năng gợi ý thực sự.
* Cân nhắc Keyset Pagination khi hệ thống mở rộng.

### Trạng thái QA

**PASS WITH PERFORMANCE IMPROVEMENTS REQUIRED ⚠️**

Module đủ điều kiện triển khai trong môi trường học tập và dữ liệu vừa phải, nhưng cần tối ưu hiệu năng trước khi áp dụng cho quy mô lớn.
