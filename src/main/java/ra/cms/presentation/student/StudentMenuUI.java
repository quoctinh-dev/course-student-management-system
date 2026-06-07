package ra.cms.presentation.student;

import ra.cms.business.IStudentBusiness;
import ra.cms.model.Student;

import java.util.Scanner;

public class StudentMenuUI {
    private final IStudentBusiness studentBusiness;
    private final Scanner scanner;
    private final Student student;

    public StudentMenuUI(IStudentBusiness studentBusiness, Scanner scanner, Student student) {
        this.studentBusiness = studentBusiness;
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
                case "1": System.out.println("Thực thi: Xem danh sách khóa học..."); break;
                case "2": System.out.println("Thực thi: Đăng ký khóa học mới..."); break;
                case "3": System.out.println("Thực thi: Xem danh sách đã đăng ký..."); break;
                case "4": System.out.println("Thực thi: Hủy đăng ký môn học..."); break;
                case "5": System.out.println("Thực thi: Tiến hành đổi mật khẩu cho email " + student.getEmail() + "..."); break;
                case "6":
                    System.out.println("Đang đăng xuất tài khoản Học viên: " + student.getName() + "...");
                    studentLoggedIn = false;
                    break;
                default: System.err.println("Lựa chọn không hợp lệ!");
            }
        }
    }
}