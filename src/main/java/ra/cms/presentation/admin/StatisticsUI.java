package ra.cms.presentation.admin;

import ra.cms.business.IStatisticBusiness;
import ra.cms.dto.CourseStatisticDTO;
import ra.cms.exception.DatabaseException;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StatisticsUI {
    private final IStatisticBusiness statisticBusiness;
    private final Scanner scanner;

    public StatisticsUI(IStatisticBusiness statisticBusiness, Scanner scanner) {
        this.statisticBusiness = statisticBusiness;
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
                case "1":
                    handleShowGeneralOverview();
                    pressEnterToContinue();
                    break;
                case "2":
                    handleShowStudentCountByCourse();
                    pressEnterToContinue();
                    break;
                case "3":
                    handleShowTop5HotCourses();
                    pressEnterToContinue();
                    break;
                case "4":
                    handleShowCrowdedCourses();
                    pressEnterToContinue();
                    break;
                case "5": inLoop = false; break;
                default:
                    System.out.println("[LỖI] Lựa chọn không hợp lệ!"); // Thay đổi sang System.out để tránh lệch luồng Console
                    pressEnterToContinue();
            }
        }
    }

    private void handleShowCrowdedCourses() {
        System.out.println("\n================ DANH SÁCH CÁC KHÓA HỌC CÓ TRÊN 10 HỌC VIÊN ================");
        try {
            List<CourseStatisticDTO> reportList = statisticBusiness.getCrowdedCourses();

            if (reportList.isEmpty()) {
                System.out.println("[ℹ] Hiện tại không có khóa học nào đạt số lượng trên 10 học viên.");
                return;
            }

            System.out.println("+------------+--------------------------------------------------+------------------------+");
            System.out.printf("| %-10s | %-48s | %-22s |\n", "Mã Số (ID)", "Tên Khóa Học Đông", "Số Học Viên Thực Tế");
            System.out.println("+------------+--------------------------------------------------+------------------------+");

            for (CourseStatisticDTO dto : reportList) {
                System.out.printf("| %-10d | %-48s | %-22d |\n",
                        dto.getCourseId(),
                        dto.getCourseName(),
                        dto.getStudentCount()
                );
            }
            System.out.println("+------------+--------------------------------------------------+------------------------+");

        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Gặp sự cố khi kết nối dữ liệu lọc khóa học: " + e.getMessage());
        }
    }

    private void handleShowTop5HotCourses() {
        System.out.println("\n================ TOP 5 KHÓA HỌC ĐÔNG HỌC VIÊN NHẤT ================");
        try {
            List<CourseStatisticDTO> reportList = statisticBusiness.getTop5HotCourses();

            if (reportList.isEmpty()) {
                System.out.println("[ℹ] Hệ thống chưa có dữ liệu khóa học để xếp hạng.");
                return;
            }

            System.out.println("+------------+--------------------------------------------------+------------------------+");
            System.out.printf("| %-10s | %-48s | %-22s |\n", "Mã Số (ID)", "Tên Khóa Học Hot", "Số Học Viên Thực Tế");
            System.out.println("+------------+--------------------------------------------------+------------------------+");

            for (CourseStatisticDTO dto : reportList) {
                System.out.printf("| %-10d | %-48s | %-22d |\n",
                        dto.getCourseId(),
                        dto.getCourseName(),
                        dto.getStudentCount()
                );
            }
            System.out.println("+------------+--------------------------------------------------+------------------------+");

        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Gặp sự cố khi kết nối dữ liệu top 5: " + e.getMessage());
        }
    }


    private void handleShowStudentCountByCourse() {
        System.out.println("\n================ THỐNG KÊ SỐ LƯỢNG HỌC VIÊN THEO TỪNG KHÓA ================");
        try {
            List<CourseStatisticDTO> reportList = statisticBusiness.getStudentCountByCourse();

            if (reportList.isEmpty()) {
                System.out.println("[ℹ] Hệ thống chưa có dữ liệu khóa học nào để tiến hành thống kê.");
                return;
            }

            System.out.println("+------------+--------------------------------------------------+------------------------+");
            System.out.printf("| %-10s | %-48s | %-22s |\n", "Mã Số (ID)", "Tên Khóa Học Hiện Có", "Số Học Viên Đã Duyệt");
            System.out.println("+------------+--------------------------------------------------+------------------------+");

            for (ra.cms.dto.CourseStatisticDTO dto : reportList) {
                System.out.printf("| %-10d | %-48s | %-22d |\n",
                        dto.getCourseId(),
                        dto.getCourseName(),
                        dto.getStudentCount()
                );
            }
            System.out.println("+------------+--------------------------------------------------+------------------------+");

        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Gặp sự cố khi kết nối dữ liệu báo cáo: " + e.getMessage());
        }
    }

    private void handleShowGeneralOverview() {
        System.out.println("\n================ THỐNG KÊ TỔNG QUAN HỆ THỐNG ================");
        try {
            Map<String, Integer> overviewData = statisticBusiness.getGeneralOverview();

            int totalCourses = overviewData.getOrDefault("totalCourses", 0);
            int totalStudents = overviewData.getOrDefault("totalStudents", 0);

            System.out.println("+-----------------------------------+------------------------+");
            System.out.printf("| %-33s | %-22s |\n", "Hạng Mục Thống Kê", "Số Lượng Thực Tế");
            System.out.println("+-----------------------------------+------------------------+");
            System.out.printf("| %-33s | %-22d |\n", "Tổng số lượng Khóa học hiện có", totalCourses);
            System.out.printf("| %-33s | %-22d |\n", "Tổng số lượng Học viên đăng ký", totalStudents);
            System.out.println("+-----------------------------------+------------------------+");

        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Không thể tải dữ liệu báo cáo tổng quan: " + e.getMessage());
        }
    }

    private void pressEnterToContinue() {
        System.out.print("\nNhấn phím [Enter] để tiếp tục...");
        scanner.nextLine();
    }
}