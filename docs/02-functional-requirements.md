# 02. YÊU CẦU CHỨC NĂNG

## 1. Chức năng dành cho Quản trị viên (Admin)

### 1.1 Đăng nhập Admin

Mô tả:

Cho phép quản trị viên đăng nhập vào hệ thống.

Yêu cầu:

* Hiển thị menu đăng nhập.
* Nhập tên đăng nhập.
* Nhập mật khẩu.
* Không cho phép dữ liệu rỗng.
* Kiểm tra thông tin với cơ sở dữ liệu.
* Hiển thị thông báo khi đăng nhập thất bại.
* Chuyển tới menu quản trị khi đăng nhập thành công.

---

### 1.2 Quản lý khóa học

#### Hiển thị danh sách khóa học

* Hiển thị toàn bộ khóa học.
* Dữ liệu hiển thị dạng bảng.
* Căn lề rõ ràng.

#### Thêm khóa học

* Nhập thông tin khóa học.
* Kiểm tra dữ liệu hợp lệ.
* Lưu dữ liệu vào hệ thống.

#### Chỉnh sửa khóa học

* Chọn khóa học cần chỉnh sửa.
* Hiển thị menu chọn thuộc tính cần sửa.
* Cập nhật dữ liệu.

#### Xóa khóa học

* Chọn khóa học cần xóa.
* Hiển thị xác nhận trước khi xóa.
* Thực hiện xóa nếu người dùng đồng ý.

#### Tìm kiếm khóa học

* Tìm theo tên khóa học.
* Hỗ trợ tìm kiếm tương đối.

#### Sắp xếp khóa học

* Sắp xếp theo tên.
* Sắp xếp tăng dần.
* Sắp xếp giảm dần.

#### Kiểm tra dữ liệu

* Không để trống dữ liệu bắt buộc.
* Hiển thị lỗi rõ ràng.

---

### 1.3 Quản lý học viên

#### Hiển thị danh sách học viên

* Hiển thị toàn bộ học viên.
* Hiển thị dạng bảng.

#### Thêm học viên

* Nhập thông tin học viên.
* Kiểm tra dữ liệu hợp lệ.

#### Chỉnh sửa học viên

* Chọn học viên cần sửa.
* Chọn thuộc tính cần sửa.
* Lưu thay đổi.

#### Xóa học viên

* Xác nhận trước khi xóa.
* Thực hiện xóa khi được xác nhận.

#### Tìm kiếm học viên

* Tìm theo tên.
* Tìm theo email.
* Tìm theo mã học viên.
* Hỗ trợ tìm kiếm tương đối.

#### Sắp xếp học viên

* Sắp xếp theo tên.
* Sắp xếp tăng dần.
* Sắp xếp giảm dần.

#### Kiểm tra dữ liệu

* Kiểm tra dữ liệu đầu vào.
* Hiển thị lỗi rõ ràng.

---

### 1.4 Quản lý đăng ký khóa học

#### Hiển thị học viên theo khóa học

* Chọn khóa học.
* Hiển thị danh sách học viên đăng ký.

#### Duyệt đăng ký khóa học

* Hiển thị danh sách đăng ký chờ duyệt.
* Duyệt đăng ký hợp lệ.

#### Xóa học viên khỏi khóa học

* Chọn học viên.
* Hủy đăng ký khóa học.

#### Kiểm tra dữ liệu

* Kiểm tra thông tin tồn tại.
* Kiểm tra trạng thái đăng ký.

---

### 1.5 Thống kê

#### Thống kê tổng quan

* Tổng số khóa học.
* Tổng số học viên.

#### Thống kê theo khóa học

* Số học viên theo từng khóa học.

#### Thống kê xếp hạng

* Top 5 khóa học có nhiều học viên nhất.

#### Thống kê điều kiện

* Liệt kê các khóa học có trên 10 học viên.

---

## 2. Yêu cầu giao diện

### Hiển thị danh sách

* Hiển thị dạng bảng.
* Căn lề dữ liệu rõ ràng.
* Dễ đọc.

### Điều hướng

* Mỗi menu có số lựa chọn tương ứng.
* Có chức năng quay lại menu trước.
* Có chức năng quay về menu chính.

---

## 3. Chức năng dành cho Học viên (Student)

### 3.1 Đăng nhập học viên

Mô tả:

Cho phép học viên đăng nhập hệ thống.

Yêu cầu:

* Nhập tên đăng nhập.
* Nhập mật khẩu.
* Kiểm tra dữ liệu không rỗng.
* Kiểm tra tài khoản trong hệ thống.
* Chuyển tới menu học viên nếu hợp lệ.

---

### 3.2 Xem danh sách khóa học

#### Hiển thị khóa học

* Hiển thị toàn bộ khóa học đang mở.

#### Tìm kiếm khóa học

* Tìm theo tên khóa học.

---

### 3.3 Đăng ký khóa học

#### Đăng ký

* Chọn khóa học cần đăng ký.
* Kiểm tra khóa học tồn tại.
* Kiểm tra đăng ký trùng lặp.
* Lưu thông tin đăng ký.

#### Kiểm tra dữ liệu

* Kiểm tra dữ liệu hợp lệ.
* Hiển thị lỗi rõ ràng.

---

### 3.4 Xem khóa học đã đăng ký

#### Hiển thị danh sách

* Hiển thị các khóa học đã đăng ký.

#### Sắp xếp

* Sắp xếp theo tên khóa học.
* Sắp xếp tăng dần.
* Sắp xếp giảm dần.

---

### 3.5 Hủy đăng ký

#### Hủy đăng ký khóa học

* Chọn khóa học cần hủy.
* Chỉ cho phép hủy khi chưa được xác nhận.

---

### 3.6 Đổi mật khẩu

#### Cập nhật mật khẩu

* Nhập mật khẩu cũ.
* Nhập mật khẩu mới.
* Xác thực bằng email hoặc số điện thoại.
* Cập nhật mật khẩu mới.

---

## 4. Chức năng nâng cao (Bonus)

### Phân trang

* Phân trang danh sách khóa học.
* Phân trang danh sách học viên.

### Đề xuất khóa học

* Gợi ý khóa học cho học viên.
* Dựa trên lịch sử đăng ký học.
