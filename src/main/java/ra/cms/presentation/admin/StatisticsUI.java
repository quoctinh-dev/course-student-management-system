package ra.cms.presentation.admin;

import ra.cms.business.IAdminbusiness;
import java.util.Scanner;

public class StatisticsUI {
    private final IAdminbusiness adminBusiness;
    private final Scanner scanner;

    public StatisticsUI(IAdminbusiness adminBusiness, Scanner scanner) {
        this.adminBusiness = adminBusiness;
        this.scanner = scanner;
    }

    public void showStatisticsMenu() {
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
}