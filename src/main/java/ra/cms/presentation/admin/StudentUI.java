package ra.cms.presentation.admin;

import ra.cms.business.IAdminbusiness;
import java.util.Scanner;

public class StudentUI {
    private final IAdminbusiness adminBusiness;
    private final Scanner scanner;

    public StudentUI(IAdminbusiness adminBusiness, Scanner scanner) {
        this.adminBusiness = adminBusiness;
        this.scanner = scanner;
    }

    public void showStudentManagementMenu() {
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
}