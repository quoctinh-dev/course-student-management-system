package ra.cms.presentation.admin;

import ra.cms.business.IAdminbusiness;
import java.util.Scanner;

public class EnrollmentUI {
    private final IAdminbusiness adminBusiness;
    private final Scanner scanner;

    public EnrollmentUI(IAdminbusiness adminBusiness, Scanner scanner) {
        this.adminBusiness = adminBusiness;
        this.scanner = scanner;
    }

    public void showEnrollmentManagementMenu() {
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
}