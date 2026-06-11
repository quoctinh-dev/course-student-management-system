package ra.cms.presentation.student;

import ra.cms.business.ICourseBusiness;
import ra.cms.business.IEnrollmentBusiness;
import ra.cms.business.IStudentBusiness;
import ra.cms.exception.BusinessException;
import ra.cms.exception.ValidationException;
import ra.cms.exception.DatabaseException;
import ra.cms.model.Course;
import ra.cms.model.Enrollment;
import ra.cms.model.Student;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class StudentMenuUI {
    private final IStudentBusiness studentBusiness;
    private final IEnrollmentBusiness enrollmentBusiness;
    private final ICourseBusiness courseBusiness;
    private final Scanner scanner;
    private final Student student;

    public StudentMenuUI(IStudentBusiness studentBusiness, IEnrollmentBusiness enrollmentBusiness, ICourseBusiness courseBusiness, Scanner scanner, Student student) {
        this.studentBusiness = studentBusiness;
        this.enrollmentBusiness = enrollmentBusiness;
        this.courseBusiness = courseBusiness;
        this.scanner = scanner;
        this.student = student;
    }

    public void showStudentMenu() {
        boolean studentLoggedIn = true;
        while (studentLoggedIn) {
            System.out.println("\n========= MENU HỌC VIÊN =========");
            System.out.println("1. Xem danh sách khóa học");
            System.out.println("2. Đăng ký khóa học");
            System.out.println("3. Xem khóa học đã đăng ký");
            System.out.println("4. Hủy đăng ký (nếu chưa bắt đầu)");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("6. Đăng xuất");
            System.out.println("==================================");
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleCourseSubMenu();
                    break;
                case "2":
                    handleEnrollCourse(student.getId());
                    pressEnterToContinue();
                    break;
                case "3":
                    handleViewEnrollmentHistory(student.getId());
                    pressEnterToContinue();
                    break;
                case "4":
                    handleCancelEnrollment(student.getId());
                    pressEnterToContinue();
                    break;
                case "5":
                    handleChangePassword();
                    pressEnterToContinue();
                    break;
                case "6":
                    System.out.println("Đang đăng xuất tài khoản Học viên: " + student.getName() + "...");
                    studentLoggedIn = false;
                    break;
                default:
                    System.out.println("[LỖI] Lựa chọn không hợp lệ!");
                    pressEnterToContinue();
            }
        }
    }

    private void handleChangePassword() {
        System.out.println("\n============== TIẾN HÀNH ĐỔI MẬT KHẨU ==============");

        System.out.print("Nhập mật khẩu HIỆN TẠI của bạn: ");
        String oldPassword = scanner.nextLine().trim();

        System.out.print("Nhập Email hoặc Số điện thoại để xác thực tài khoản: ");
        String verificationInput = scanner.nextLine().trim();

        System.out.print("Nhập mật khẩu MỚI: ");
        String newPassword = scanner.nextLine().trim();

        System.out.print("Nhập LẠI mật khẩu mới để xác nhận: ");
        String confirmPassword = scanner.nextLine().trim();

        try {
            studentBusiness.changePassword(student, oldPassword, verificationInput, newPassword, confirmPassword);

            System.out.println("\n[THÀNH CÔNG] Mật khẩu của bạn đã được cập nhật hoàn tất trên hệ thống.");
            System.out.println("[ℹ] Hãy sử dụng mật khẩu mới này cho lần đăng nhập tiếp theo.");

        } catch (ValidationException | BusinessException e) {
            System.out.println("\n[TỪ CHỐI THAO TÁC] " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Lỗi kết nối hoặc thực thi Database: " + e.getMessage());
        }
    }

    private void handleCancelEnrollment(Long studentId) {
        System.out.println("\n============== HỦY ĐĂNG KÝ KHÓA HỌC ==============");
        System.out.println("[ℹ] Mẹo: Bạn nên chọn mục số 3 ở Menu chính trước để xem chính xác 'Mã Đơn' của mình.");
        System.out.print("Nhập Mã Đơn đăng ký (ID Đơn) bạn muốn tiến hành hủy: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[LỖI] Không được bỏ trống ô nhập mã số đơn!"); // Đổi sang System.out
            return;
        }

        try {
            Long enrollmentId = Long.parseLong(input);

            System.out.print("Bạn có chắc chắn muốn hủy bỏ đơn đăng ký học số #" + enrollmentId + " không? (Y/N): ");
            String confirm = scanner.nextLine().trim();

            if ("Y".equalsIgnoreCase(confirm)) {
                enrollmentBusiness.cancelEnrollment(studentId, enrollmentId);
                System.out.println("\n[THÀNH CÔNG] Đã hủy đơn đăng ký học và rút tên khỏi danh sách chờ lớp thành công!");
            } else {
                System.out.println("[ℹ] Thao tác hủy đơn đã được hủy bỏ theo yêu cầu.");
            }

        } catch (NumberFormatException e) {
            System.out.println("[LỖI] Mã số đơn đăng ký nhập vào bắt buộc phải là ký tự số nguyên dương!"); // Đổi sang System.out
        } catch (ValidationException | BusinessException e) {
            System.out.println("\n[TỪ CHỐI THAO TÁC] " + e.getMessage()); // Đổi sang System.out
        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Gặp sự cố kết nối hoặc thực thi Database: " + e.getMessage()); // Đổi sang System.out
        }
    }

    private void handleCourseSubMenu() {
        boolean inSubMenu = true;
        while (inSubMenu) {
            System.out.println("\n--- TRA CỨU THÔNG TIN KHÓA HỌC ---");
            System.out.println("1. Xem danh sách toàn bộ khóa học hiện có");
            System.out.println("2. Tìm kiếm khóa học theo tên");
            System.out.println("3. Quay lại Menu chính");
            System.out.print("Nhập lựa chọn của bạn (1-3): ");

            String subChoice = scanner.nextLine().trim();
            switch (subChoice) {
                case "1":
                    viewAllCourses();
                    pressEnterToContinue();
                    break;
                case "2":
                    searchCourseByName();
                    pressEnterToContinue();
                    break;
                case "3":
                    inSubMenu = false;
                    break;
                default:
                    System.out.println("[LỖI] Lựa chọn nằm ngoài danh mục, vui lòng chọn lại."); // Đổi sang System.out
                    pressEnterToContinue();
            }
        }
    }

    private void viewAllCourses() {
        System.out.println("\n================ DANH SÁCH KHÓA HỌC ĐANG CÓ ================");
        try {
            List<Course> list = courseBusiness.getAllCourses();
            printCourseTable(list);
        } catch (DatabaseException e) {
            System.out.println("[LỖI HỆ THỐNG] Không thể tải danh sách khóa học từ server: " + e.getMessage()); // Đổi sang System.out
        }
    }

    private void searchCourseByName() {
        System.out.println("\n================ TÌM KIẾM KHÓA HỌC THEO TÊN ================");
        System.out.print("Nhập tên khóa học hoặc từ khóa cần tra cứu: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("[LỖI] Bạn không được bỏ trống từ khóa tìm kiếm!"); // Đổi sang System.out
            return;
        }

        try {
            List<Course> result = courseBusiness.searchCoursesByName(keyword);
            System.out.println("\n[KẾT QUẢ] Tìm thấy " + result.size() + " khóa học khớp với từ khóa '" + keyword + "':");
            printCourseTable(result);
        } catch (ValidationException | BusinessException e) {
            System.out.println("\n[TỪ CHỐI THAO TÁC] " + e.getMessage()); // Đổi sang System.out
        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Gặp sự cố kết nối dữ liệu: " + e.getMessage()); // Đổi sang System.out
        }
    }

    private void handleEnrollCourse(Long studentId) {
        System.out.println("\n============== ĐĂNG KÝ KHÓA HỌC MỚI ==============");
        System.out.print("Nhập mã số (ID) khóa học bạn muốn đăng ký: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[LỖI] Bạn không được bỏ trống ô nhập mã khóa học!"); // Đổi sang System.out
            return;
        }

        try {
            Long courseId = Long.parseLong(input);

            enrollmentBusiness.registerCourse(studentId, courseId);

            System.out.println("\n[THÀNH CÔNG] Đơn đăng ký học của bạn đã được gửi lên hệ thống.");
            System.out.println("[ℹ] Trạng thái đơn: WAITING (Vui lòng chờ Admin phê duyệt đơn để vào lớp).");

        } catch (NumberFormatException e) {
            System.out.println("[LỖI] Mã ID khóa học nhập vào bắt buộc phải là ký tự số nguyên dương!"); // Đổi sang System.out
        } catch (ValidationException | BusinessException e) {
            System.out.println("\n[TỪ CHỐI THAO TÁC] " + e.getMessage()); // Đổi sang System.out
        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Lỗi kết nối hoặc thực thi Database: " + e.getMessage()); // Đổi sang System.out
        }
    }

    private void handleViewEnrollmentHistory(Long studentId) {
        int sortOption = 4;

        System.out.println("\n--- TIÊU CHÍ SẮP XẾP KHÓA HỌC ĐÃ ĐĂNG KÝ ---");
        System.out.println("1. Sắp xếp theo tên khóa học tăng dần (A-Z)");
        System.out.println("2. Sắp xếp theo tên khóa học giảm dần (Z-A)");
        System.out.println("3. Sắp xếp theo ngày đăng ký tăng dần (Cũ nhất)");
        System.out.println("4. Sắp xếp theo ngày đăng ký giảm dần (Mới nhất)");
        System.out.print("Mời bạn chọn tiêu chí sắp xếp (1-4) hoặc nhấn Enter để mặc định: ");

        String sortInput = scanner.nextLine().trim();
        if (!sortInput.isEmpty()) {
            try {
                int option = Integer.parseInt(sortInput);
                if (option >= 1 && option <= 4) {
                    sortOption = option;
                } else {
                    System.out.println("[ℹ] Lựa chọn nằm ngoài danh mục, hệ thống tự động sắp xếp theo mới nhất.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ℹ] Định dạng sai, hệ thống tự động sắp xếp theo mới nhất.");
            }
        }

        System.out.println("\n================ DANH SÁCH KHÓA HỌC ĐÃ ĐĂNG KÝ ================");
        try {
            List<Enrollment> history = enrollmentBusiness.getHistoryByStudent(studentId, sortOption);

            if (history == null || history.isEmpty()) {
                System.out.println("[ℹ] Bạn chưa đăng ký bất kỳ khóa học nào trên hệ thống.");
                return;
            }

            System.out.println("+---------+-----------------------------------+---------------------+--------------------+");
            System.out.printf("| %-7s | %-33s | %-19s | %-18s |\n", "Mã Đơn", "Tên Khóa Học", "Ngày Đăng Ký", "Trạng Thái");
            System.out.println("+---------+-----------------------------------+---------------------+--------------------+");

            DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            for (Enrollment e : history) {
                System.out.printf("| %-7d | %-33s | %-19s | %-18s |\n",
                        e.getId(),
                        e.getCourse().getName(),
                        e.getRegisteredAt().format(formatter),
                        e.getStatus()
                );
            }
            System.out.println("+---------+-----------------------------------+---------------------+--------------------+");

        } catch (DatabaseException e) {
            System.out.println("[LỖI HỆ THỐNG] Không thể truy xuất dữ liệu từ máy chủ: " + e.getMessage()); // Đổi sang System.out
        }
    }

    private void printCourseTable(List<Course> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("[ℹ] Không có dữ liệu khóa học nào để hiển thị.");
            return;
        }
        System.out.println("+------------+--------------------------------------------------+");
        System.out.printf("| %-10s | %-48s |\n", "Mã Số (ID)", "Tên Khóa Học");
        System.out.println("+------------+--------------------------------------------------+");
        for (Course c : list) {
            System.out.printf("| %-10d | %-48s |\n", c.getId(), c.getName());
        }
        System.out.println("+------------+--------------------------------------------------+");
    }

    private void pressEnterToContinue() {
        System.out.print("\nNhấn phím [Enter] để tiếp tục...");
        scanner.nextLine();
    }
}