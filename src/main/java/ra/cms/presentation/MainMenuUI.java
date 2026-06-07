package ra.cms.presentation;

import ra.cms.business.IAdminbusiness;
import ra.cms.business.IStudentBusiness;
import ra.cms.presentation.auth.AdminLoginUI;
import ra.cms.presentation.auth.StudentLoginUI;

import java.util.Scanner;

public class MainMenuUI {
    private final IAdminbusiness adminBusiness;
    private final IStudentBusiness studentBusiness;
    private final Scanner scanner;

    public MainMenuUI(IAdminbusiness adminBusiness, IStudentBusiness studentBusiness, Scanner scanner) {
        this.adminBusiness = adminBusiness;
        this.studentBusiness = studentBusiness;
        this.scanner = scanner;
    }

    public void showMainMenu() {
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
                    AdminLoginUI adminLoginUI = new AdminLoginUI(adminBusiness, scanner);
                    adminLoginUI.showLoginMenu();
                    break;
                case "2":
                    StudentLoginUI studentLoginUI = new StudentLoginUI(studentBusiness, scanner);
                    studentLoginUI.showStudentLoginMenu();
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
}