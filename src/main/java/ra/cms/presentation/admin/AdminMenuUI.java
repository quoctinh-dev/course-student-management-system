package ra.cms.presentation.admin;

import ra.cms.business.IAdminbusiness;
import ra.cms.business.ICourseBusiness; // 1. Import Interface Course Business
import ra.cms.business.IEnrollmentBusiness;
import ra.cms.business.IStudentBusiness;
import ra.cms.business.impl.CourseBusinessImpl; // 2. Import lớp triển khai cụ thể
import ra.cms.business.impl.EnrollmentBusinessImpl;
import ra.cms.business.impl.StudentBusinessImpl;
import ra.cms.model.Admin;

import java.util.Scanner;

public class AdminMenuUI {
    private final IAdminbusiness adminBusiness;
    private final Scanner scanner;
    private final Admin admin;

    public AdminMenuUI(IAdminbusiness adminBusiness, Scanner scanner, Admin admin) {
        this.adminBusiness = adminBusiness;
        this.scanner = scanner;
        this.admin = admin;
    }

    public void showAdminMenu() {
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
                    ICourseBusiness courseBusiness = new CourseBusinessImpl();
                    CourseUI courseUI = new CourseUI(courseBusiness, scanner);
                    courseUI.showCourseManagementMenu();
                    break;
                case "2":
                    IStudentBusiness studentBusiness = new StudentBusinessImpl();
                    StudentUI studentUI = new StudentUI(studentBusiness, scanner);
                    studentUI.showStudentManagementMenu();
                    break;
                case "3":
                    IEnrollmentBusiness enrollmentBusiness = new EnrollmentBusinessImpl();
                    EnrollmentUI enrollmentUI = new EnrollmentUI(enrollmentBusiness, scanner);
                    enrollmentUI.showEnrollmentManagementMenu();
                    break;
                case "4":
                    StatisticsUI statisticsUI = new StatisticsUI(adminBusiness, scanner);
                    statisticsUI.showStatisticsMenu();
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
}