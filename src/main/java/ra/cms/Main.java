package ra.cms;

import ra.cms.business.IAdminbusiness;
import ra.cms.business.IStudentBusiness;
import ra.cms.business.impl.Adminbusinessimpl;
import ra.cms.business.impl.StudentBusinessImpl;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Admin;
import ra.cms.model.Student;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final IAdminbusiness adminBusiness = new Adminbusinessimpl();
    private static final IStudentBusiness studentBusiness = new StudentBusinessImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean systemRunning = true;

        while (systemRunning) {
            System.out.println("\n========= HỆ THỐNG QUẢN LÝ ĐÀO TẠO =========");
            System.out.println("1. Đăng nhập với tư cách Quản trị viên");
            System.out.println("2. Đăng nhập với tư cách Học viên");
            System.out.println("3. Thoát");
            System.out.println("=============================================");
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showLoginMenu();
                    break;
                case "2":
                    showStudentLoginMenu();
                    break;
                case "3":
                    System.out.println("Tạm biệt! Hệ thống đang đóng...");
                    systemRunning = false;
                    break;
                default:
                    System.err.println("Lựa chọn không hợp lệ, vui lòng chọn lại!");
            }
        }
    }


    private static void showLoginMenu() {
        try {
            System.out.println("\n--- ĐĂNG NHẬP QUẢN TRỊ VIÊN ---");
            System.out.print("Nhập username: ");
            String username = scanner.nextLine();
            System.out.print("Nhập password: ");
            String password = scanner.nextLine();

            System.out.println("Đang xác thực thông tin...");
            Optional<Admin> adminOpt = adminBusiness.login(username, password);

            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                System.out.println("Đăng nhập thành công! Chào mừng Admin: " + admin.getUsername());
                showAdminMenu(admin);
            } else {
                System.err.println("Đăng nhập thất bại: Sai tài khoản hoặc mật khẩu!");
            }

        } catch (ValidationException e) {
            System.err.println("Cảnh báo nhập liệu: " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("Hệ thống gặp sự cố: " + e.getMessage());
        }
    }


    private static void showAdminMenu(Admin admin) {
        boolean adminLoggedIn = true;

        while (adminLoggedIn) {
            System.out.println("\n========= MENU ADMIN =========");
            System.out.println("1. Quản lý khóa học");
            System.out.println("2. Quản lý học viên");
            System.out.println("3. Quản lý đăng ký học");
            System.out.println("4. Thống kê học viên theo khóa học");
            System.out.println("5. Đăng xuất");
            System.out.println("==============================");
            System.out.print("Nhập lựa chọn: ");

            String adminChoice = scanner.nextLine();

            switch (adminChoice) {
                case "1":
                    showCourseManagementMenu();
                    break;
                case "2":
                    showStudentManagementMenu();
                    break;
                case "3":
                    showEnrollmentManagementMenu();
                    break;
                case "4":
                    showStatisticsMenu();
                    break;
                case "5":
                    System.out.println("Đang đăng xuất tài khoản " + admin.getUsername() + "...");
                    adminLoggedIn = false;
                    break;
                default:
                    System.err.println("Chức năng không hợp lệ, vui lòng chọn lại!");
            }
        }
    }

    private static void showCourseManagementMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- [2.1] MENU QUẢN LÝ KHÓA HỌC ---");
            System.out.println("1. Hiển thị danh sách khóa học");
            System.out.println("2. Thêm mới khóa học");
            System.out.println("3. Chỉnh sửa thông tin khóa học");
            System.out.println("4. Xóa khóa học (xác nhận trước khi xóa)");
            System.out.println("5. Tìm kiếm theo tên (tương đối)");
            System.out.println("6. Sắp xếp theo tên hoặc id (tăng/giảm dần)");
            System.out.println("7. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": System.out.println("Thực thi: Hiển thị danh sách khóa học..."); break;
                case "2": System.out.println("Thực thi: Thêm mới khóa học..."); break;
                case "3": System.out.println("Thực thi: Chỉnh sửa thông tin khóa học..."); break;
                case "4": System.out.println("Thực thi: Xóa khóa học..."); break;
                case "5": System.out.println("Thực thi: Tìm kiếm theo tên..."); break;
                case "6": System.out.println("Thực thi: Sắp xếp khóa học..."); break;
                case "7": inLoop = false; break;
                default: System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void showStudentManagementMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- [2.2] MENU QUẢN LÝ HỌC VIÊN ---");
            System.out.println("1. Hiển thị danh sách học viên");
            System.out.println("2. Thêm mới học viên");
            System.out.println("3. Chỉnh sửa thông tin học viên");
            System.out.println("4. Xóa học viên (xác nhận trước khi xóa)");
            System.out.println("5. Tìm kiếm theo tên, email hoặc id (tương đối)");
            System.out.println("6. Sắp xếp theo tên hoặc id (tăng/giảm dần)");
            System.out.println("7. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": System.out.println("Thực thi: Hiển thị danh sách học viên..."); break;
                case "2": System.out.println("Thực thi: Thêm mới học viên..."); break;
                case "3": System.out.println("Thực thi: Chỉnh sửa học viên..."); break;
                case "4": System.out.println("Thực thi: Xóa học viên..."); break;
                case "5": System.out.println("Thực thi: Tìm kiếm học viên..."); break;
                case "6": System.out.println("Thực thi: Sắp xếp học viên..."); break;
                case "7": inLoop = false; break;
                default: System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void showEnrollmentManagementMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- [2.3] QUẢN LÝ ĐĂNG KÝ KHÓA HỌC ---");
            System.out.println("1. Hiển thị học viên theo từng khóa học");
            System.out.println("2. Thêm học viên vào khóa học");
            System.out.println("3. Xóa học viên khỏi khóa học");
            System.out.println("4. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": System.out.println("Thực thi: Hiển thị học viên theo khóa học..."); break;
                case "2": System.out.println("Thực thi: Thêm học viên vào khóa học..."); break;
                case "3": System.out.println("Thực thi: Xóa học viên khỏi khóa học..."); break;
                case "4": inLoop = false; break;
                default: System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private static void showStatisticsMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- [2.4] MENU THỐNG KÊ ---");
            System.out.println("1. Thống kê tổng số lượng khóa học và học viên");
            System.out.println("2. Thống kê học viên theo từng khóa học");
            System.out.println("3. Top 5 khóa học đông học viên nhất");
            System.out.println("4. Liệt kê khóa học có trên 10 học viên");
            System.out.println("5. Quay về menu chính");
            System.out.print("Nhập lựa chọn: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": System.out.println("Thực thi: Thống kê tổng số lượng..."); break;
                case "2": System.out.println("Thực thi: Thống kê chi tiết từng khóa..."); break;
                case "3": System.out.println("Thực thi: Tìm Top 5 khóa đông nhất..."); break;
                case "4": System.out.println("Thực thi: Lọc khóa học > 10 học viên..."); break;
                case "5": inLoop = false; break;
                default: System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }


    private static void showStudentLoginMenu() {
        try {
            System.out.println("\n--- ĐĂNG NHẬP HỌC VIÊN ---");
            System.out.print("Nhập email học viên: ");
            String email = scanner.nextLine();
            System.out.print("Nhập mật khẩu: ");
            String password = scanner.nextLine();

            System.out.println("Đang kết nối xác thực tài khoản Học viên...");

            Optional<Student> studentOpt = studentBusiness.login(email, password);

            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                System.out.println("Đăng nhập thành công! Chào mừng Học viên: " + student.getName());

                showStudentMenu(student);
            } else {
                System.err.println("Đăng nhập thất bại: Sai tài khoản email hoặc mật khẩu Học viên!");
            }

        } catch (ValidationException e) {
            System.err.println("Cảnh báo nhập liệu: " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("Hệ thống gặp sự cố: " + e.getMessage());
        }
    }


    private static void showStudentMenu(Student student) {
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
                case "1": System.out.println("Thực thi: Xem danh sách khóa học..."); break;
                case "2": System.out.println("Thực thi: Đăng ký khóa học mới..."); break;
                case "3": System.out.println("Thực thi: Xem danh sách đã đăng ký..."); break;
                case "4": System.out.println("Thực thi: Hủy đăng ký môn học..."); break;
                case "5":System.out.println("Thực thi: Tiến hành đổi mật khẩu cho email " + student.getEmail() + "..."); break;
                case "6":
                    System.out.println("Đang đăng xuất tài khoản Học viên: " + student.getName() + "...");
                    studentLoggedIn = false;
                    break;
                default: System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}