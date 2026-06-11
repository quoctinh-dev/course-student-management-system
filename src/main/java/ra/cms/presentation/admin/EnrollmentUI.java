package ra.cms.presentation.admin;

import ra.cms.business.IAdminbusiness;
import ra.cms.business.IEnrollmentBusiness;
import ra.cms.business.impl.EnrollmentBusinessImpl;
import ra.cms.exception.BusinessException;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Enrollment;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class EnrollmentUI {
    private final IEnrollmentBusiness enrollmentBusiness;
    private final Scanner scanner;

    public EnrollmentUI(IEnrollmentBusiness enrollmentBusiness, Scanner scanner) {
        this.enrollmentBusiness = enrollmentBusiness;
        this.scanner = scanner;
    }

    public void showEnrollmentManagementMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- [2.3] QUẢN LÝ ĐĂNG KÝ KHÓA HỌC ---");
            System.out.println("1. Hiển thị học viên theo từng khóa học");
            System.out.println("2. Duyệt sinh viên đăng ký khóa học");
            System.out.println("3. Xóa học viên khỏi khóa học");
            System.out.println("4. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": handleViewStudentsByCourseWithPagination(); break;
                case "2": handleApproveEnrollment(); break;
                case "3": handleRemoveStudentFromCourse(); break;
                case "4": inLoop = false; break;
                default: System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void handleViewStudentsByCourse() {
        System.out.println("\n============== DANH SÁCH SINH VIÊN ĐĂNG KÝ THEO KHÓA HỌC ==============");
        System.out.print("Nhập Mã số (ID) khóa học cần kiểm tra: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[LỖI] Không được bỏ trống mã khóa học!");
            return;
        }

        try {
            Long courseId = Long.parseLong(input);

            List<Enrollment> list = enrollmentBusiness.getEnrollmentsByCourse(courseId);

            if (list.isEmpty()) {
                System.out.println("[ℹ] Hiện tại chưa có sinh viên nào đăng ký khóa học này.");
                return;
            }

            System.out.println("\n+---------+------------+---------------------------+---------------------+--------------------+");
            System.out.printf("| %-7s | %-10s | %-25s | %-19s | %-18s |\n", "Mã Đơn", "ID Học Viên", "Tên Học Viên", "Ngày Đăng Ký", "Trạng Thái Đơn");
            System.out.println("+---------+------------+---------------------------+---------------------+--------------------+");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            for (Enrollment e : list) {
                System.out.printf("| %-7d | %-10d | %-25s | %-19s | %-18s |\n",
                        e.getId(),
                        e.getStudent().getId(),
                        e.getStudent().getName(),
                        e.getRegisteredAt() != null ? e.getRegisteredAt().format(formatter) : "N/A",
                        e.getStatus()
                );
            }
            System.out.println("+---------+------------+---------------------------+---------------------+--------------------+");

        } catch (NumberFormatException e) {
            System.out.println("[LỖI] Mã ID khóa học bắt buộc phải là ký tự số nguyên dương!");
        } catch (ValidationException | BusinessException e) {
            System.out.println("\n[TỪ CHỐI THAO TÁC] " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void handleViewStudentsByCourseWithPagination() {
        System.out.println("\n============== DANH SÁCH SINH VIÊN ĐĂNG KÝ THEO KHÓA HỌC (PHÂN TRANG) ==============");
        System.out.print("Nhập Mã số (ID) khóa học cần kiểm tra: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[LỖI] Không được bỏ trống mã khóa học!");
            return;
        }

        try {
            Long courseId = Long.parseLong(input);

            int currentPage = 1;
            int pageSize = 5;
            boolean viewing = true;

            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            while (viewing) {
                int totalEnrollments = enrollmentBusiness.countByCourseId(courseId);
                int totalPages = (int) Math.ceil((double) totalEnrollments / pageSize);
                if (totalPages == 0) totalPages = 1;

                List<Enrollment> pagedList = enrollmentBusiness.findByCourseIdWithPagination(courseId, currentPage, pageSize);

                System.out.println("\n================ DANH SÁCH ĐƠN ĐĂNG KÝ - KHÓA HỌC #" + courseId + " (TRANG " + currentPage + "/" + totalPages + ") ================");

                if (pagedList.isEmpty()) {
                    System.out.println("[ℹ] Hiện tại chưa có sinh viên nào đăng ký khóa học này hoặc trang này không có dữ liệu.");
                } else {
                    System.out.println("+---------+------------+---------------------------+---------------------+--------------------+");
                    System.out.printf("| %-7s | %-10s | %-25s | %-19s | %-18s |\n", "Mã Đơn", "ID Học Viên", "Tên Học Viên", "Ngày Đăng Ký", "Trạng Thái Đơn");
                    System.out.println("+---------+------------+---------------------------+---------------------+--------------------+");

                    for (Enrollment e : pagedList) {
                        System.out.printf("| %-7d | %-10d | %-25s | %-19s | %-18s |\n",
                                e.getId(),
                                e.getStudent().getId(),
                                e.getStudent().getName(),
                                e.getRegisteredAt() != null ? e.getRegisteredAt().format(formatter) : "N/A",
                                e.getStatus()
                        );
                    }
                    System.out.println("+---------+------------+---------------------------+---------------------+--------------------+");
                }

                System.out.println("\n--- [ĐIỀU HƯỚNG PHÂN TRANG ĐƠN ĐĂNG KÝ] ---");
                if (currentPage < totalPages) System.out.println("N. Trang tiếp theo (Next)");
                if (currentPage > 1)         System.out.println("P. Trang trước đó (Previous)");
                System.out.println("E. Thoát xem danh sách");
                System.out.print("Nhập hành động (N/P/E): ");

                String action = scanner.nextLine().trim().toUpperCase();
                switch (action) {
                    case "N":
                        if (currentPage < totalPages) currentPage++;
                        else System.out.println("[i] Đang ở trang đơn đăng ký cuối cùng!");
                        break;
                    case "P":
                        if (currentPage > 1) currentPage--;
                        else System.out.println("[i] Đang ở trang đơn đăng ký đầu tiên!");
                        break;
                    case "E":
                        viewing = false;
                        break;
                    default:
                        System.out.println("[x] Lệnh không hợp lệ! Chỉ chọn N, P hoặc E.");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("[LỖI] Mã ID khóa học bắt buộc phải là ký tự số nguyên dương!");
        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
        pressEnterToContinue();
    }

    private void handleApproveEnrollment() {
        System.out.println("\n============== PHÊ DUYỆT ĐƠN ĐĂNG KÝ KHÓA HỌC ==============");
        System.out.print("Nhập Mã số đơn đăng ký (Mã Đơn) bạn muốn xử lý: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[LỖI] Không được bỏ trống mã số đơn đăng ký!");
            return;
        }

        try {
            Long enrollmentId = Long.parseLong(input);

            System.out.println("--- VUI LÒNG CHỌN HÀNH ĐỘNG XỬ LÝ ---");
            System.out.println("1. Phê duyệt (Chấp nhận cho sinh viên vào lớp)");
            System.out.println("2. Từ chối đơn (Không chấp nhận)");
            System.out.print("Nhập lựa chọn của bạn (1-2): ");
            String action = scanner.nextLine().trim();

            boolean isApproved;
            if ("1".equals(action)) {
                isApproved = true;
            } else if ("2".equals(action)) {
                isApproved = false;
            } else {
                System.out.println("[LỖI] Hành động lựa chọn không hợp lệ, hủy bỏ thao tác phê duyệt.");
                return;
            }

            enrollmentBusiness.approveEnrollment(enrollmentId, isApproved);

            String successMsg = isApproved ? "PHÊ DUYỆT THÀNH CÔNG (Trạng thái: CONFIRM)" : "TỪ CHỐI ĐƠN THÀNH CÔNG (Trạng thái: DENIED)";
            System.out.println("\n[THÀNH CÔNG] " + successMsg + " cho đơn đăng ký số #" + enrollmentId);

        } catch (NumberFormatException e) {
            System.out.println("[LỖI] Mã số đơn đăng ký nhập vào phải là ký tự số nguyên dương!");
        } catch (ValidationException | BusinessException e) {
            System.out.println("\n[TỪ CHỐI THAO TÁC] " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Lỗi kết nối hoặc thực thi Database: " + e.getMessage());
        }
    }

    private void handleRemoveStudentFromCourse() {
        System.out.println("\n============== XÓA SINH VIÊN KHỎI KHÓA HỌC ==============");
        System.out.print("Nhập Mã số đơn đăng ký (Mã Đơn) muốn tiến hành xóa: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("[LỖI] Không được bỏ trống mã số đơn!");
            return;
        }

        try {
            Long enrollmentId = Long.parseLong(input);

            System.out.print("CẢNH BÁO: Bạn có chắc chắn muốn XÓA VĨNH VIỄN sinh viên này khỏi khóa học (Mã đơn #" + enrollmentId + ") không? (Y/N): ");
            String confirm = scanner.nextLine().trim();

            if ("Y".equalsIgnoreCase(confirm)) {
                enrollmentBusiness.removeStudentFromCourse(enrollmentId);
                System.out.println("\n[THÀNH CÔNG] Đã xóa sinh viên và gỡ bỏ hoàn toàn đơn đăng ký số #" + enrollmentId + " khỏi hệ thống lớp học!");
            } else {
                System.out.println("[ℹ] Thao tác xóa đã được hủy bỏ theo yêu cầu của Admin.");
            }

        } catch (NumberFormatException e) {
            System.out.println("[LỖI] Mã số đơn đăng ký nhập vào bắt buộc phải là ký tự số nguyên dương!");
        } catch (ValidationException | BusinessException e) {
            System.out.println("\n[TỪ CHỐI THAO TÁC] " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Gặp sự cố kết nối hoặc thực thi Database: " + e.getMessage());
        }
    }

    private void pressEnterToContinue() {
        System.out.print("\n Bấm phím [Enter] để quay lại Menu...");
        scanner.nextLine();
    }
}