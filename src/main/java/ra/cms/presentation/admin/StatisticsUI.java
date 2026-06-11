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
                    handleShowStudentCountByCourseWithPagination();
                    pressEnterToContinue();
                    break;
                case "3":
                    handleShowTop5HotCourses();
                    pressEnterToContinue();
                    break;
                case "4":
                    handleShowCrowdedCoursesWithPagination();
                    pressEnterToContinue();
                    break;
                case "5": inLoop = false; break;
                default:
                    System.out.println("[LỖI] Lựa chọn không hợp lệ!");
                    pressEnterToContinue();
            }
        }
    }

    // PHÂN TRANG NÂNG CAO


    private void handleShowCrowdedCourses() {
        System.out.println("\n================ DANH SÁCH CÁC KHÓA HỌC CÓ TRÊN 10 HỌC VIÊN ================");
        try {
            List<CourseStatisticDTO> reportList = statisticBusiness.getCrowdedCourses();

            if (reportList.isEmpty()) {
                System.out.println("[ℹ] Hiện tại không có khóa học nào đạt số lượng trên 10 học viên.");
                return;
            }

            System.out.println("+------------+----------------------------------------------------+------------------------+");
            System.out.printf("| %-10s | %-50s | %-22s |\n", "Mã Số (ID)", "Tên Khóa Học Đông", "Số Học Viên Thực Tế");
            System.out.println("+------------+----------------------------------------------------+------------------------+");

            for (CourseStatisticDTO dto : reportList) {
                System.out.printf("| %-10d | %-50s | %-22d |\n",
                        dto.getCourseId(),
                        dto.getCourseName(),
                        dto.getStudentCount()
                );
            }
            System.out.println("+------------+----------------------------------------------------+------------------------+");

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

            System.out.println("+------------+----------------------------------------------------+------------------------+");
            System.out.printf("| %-10s | %-50s | %-22s |\n", "Mã Số (ID)", "Tên Khóa Học Hot", "Số Học Viên Thực Tế");
            System.out.println("+------------+----------------------------------------------------+------------------------+");

            for (CourseStatisticDTO dto : reportList) {
                System.out.printf("| %-10d | %-50s | %-22d |\n",
                        dto.getCourseId(),
                        dto.getCourseName(),
                        dto.getStudentCount()
                );
            }
            System.out.println("+------------+----------------------------------------------------+------------------------+");

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

            System.out.println("+------------+----------------------------------------------------+------------------------+");
            System.out.printf("| %-10s | %-50s | %-22s |\n", "Mã Số (ID)", "Tên Khóa Học Hiện Có", "Số Học Viên Đã Duyệt");
            System.out.println("+------------+----------------------------------------------------+------------------------+");

            for (ra.cms.dto.CourseStatisticDTO dto : reportList) {
                System.out.printf("| %-10d | %-50s | %-22d |\n",
                        dto.getCourseId(),
                        dto.getCourseName(),
                        dto.getStudentCount()
                );
            }
            System.out.println("+------------+----------------------------------------------------+------------------------+");

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

            System.out.println("+-----------------------------------------+------------------------+");
            System.out.printf("| %-39s | %-22s |\n", "Hạng Mục Thống Kê", "Số Lượng Thực Tế");
            System.out.println("+-----------------------------------------+------------------------+");
            System.out.printf("| %-39s | %-22d |\n", "Tổng số lượng Khóa học hiện có", totalCourses);
            System.out.printf("| %-39s | %-22d |\n", "Tổng số lượng Học viên đăng ký", totalStudents);
            System.out.println("+-----------------------------------------+------------------------+");

        } catch (DatabaseException e) {
            System.out.println("\n[LỖI HỆ THỐNG] Không thể tải dữ liệu báo cáo tổng quan: " + e.getMessage());
        }
    }

    // PHÂN TRANG NÂNG CAO
    private void handleShowStudentCountByCourseWithPagination() {
        int currentPage = 1;
        int pageSize = 5;
        boolean viewing = true;

        while (viewing) {
            try {
                int totalCourses = statisticBusiness.countTotalCoursesForStatistic();
                int totalPages = (int) Math.ceil((double) totalCourses / pageSize);
                if (totalPages == 0) totalPages = 1;

                List<CourseStatisticDTO> reportList = statisticBusiness.getStudentCountByCourseWithPagination(currentPage, pageSize);

                System.out.println("\n================ THỐNG KÊ SỐ LƯỢNG HỌC VIÊN THEO TỪNG KHÓA - PHÂN TRANG (TRANG " + currentPage + "/" + totalPages + ") ================");
                if (reportList.isEmpty()) {
                    System.out.println("[ℹ] Hệ thống chưa có dữ liệu khóa học nào để tiến hành thống kê.");
                    break;
                }

                System.out.println("+------------+----------------------------------------------------+------------------------+");
                System.out.printf("| %-10s | %-50s | %-22s |\n", "Mã Số (ID)", "Tên Khóa Học Hiện Có", "Số Học Viên Đã Duyệt");
                System.out.println("+------------+----------------------------------------------------+------------------------+");

                for (CourseStatisticDTO dto : reportList) {
                    System.out.printf("| %-10d | %-50s | %-22d |\n",
                            dto.getCourseId(),
                            dto.getCourseName(),
                            dto.getStudentCount()
                    );
                }
                System.out.println("+------------+----------------------------------------------------+------------------------+");

                System.out.println("\n--- [ĐIỀU HƯỚNG PHÂN TRANG THỐNG KÊ] ---");
                if (currentPage < totalPages) System.out.println("N. Trang tiếp theo (Next)");
                if (currentPage > 1)         System.out.println("P. Trang trước đó (Previous)");
                System.out.println("E. Thoát xem thống kê");
                System.out.print("Nhập hành động (N/P/E): ");

                String action = scanner.nextLine().trim().toUpperCase();
                switch (action) {
                    case "N":
                        if (currentPage < totalPages) currentPage++;
                        else System.out.println("[i] Đang ở trang cuối cùng!");
                        break;
                    case "P":
                        if (currentPage > 1) currentPage--;
                        else System.out.println("[i] Đang ở trang đầu tiên!");
                        break;
                    case "E":
                        viewing = false;
                        break;
                    default:
                        System.out.println("[X] Lệnh không hợp lệ! Chỉ chọn N, P hoặc E.");
                }
            } catch (DatabaseException e) {
                System.out.println("\n[LỖI HỆ THỐNG] Gặp sự cố khi kết nối dữ liệu báo cáo: " + e.getMessage());
                viewing = false;
            }
        }
    }

    private void handleShowCrowdedCoursesWithPagination() {
        int currentPage = 1;
        int pageSize = 5;
        boolean viewing = true;

        while (viewing) {
            try {
                int totalCourses = statisticBusiness.countCrowdedCoursesForStatistic();
                int totalPages = (int) Math.ceil((double) totalCourses / pageSize);
                if (totalPages == 0) totalPages = 1;

                List<CourseStatisticDTO> reportList = statisticBusiness.getCrowdedCoursesWithPagination(currentPage, pageSize);

                System.out.println("\n================ DANH SÁCH KHÓA HỌC TRÊN 10 HỌC VIÊN - PHÂN TRANG (TRANG " + currentPage + "/" + totalPages + ") ================");
                if (reportList.isEmpty()) {
                    System.out.println("[ℹ] Hiện tại không có khóa học nào đạt số lượng trên 10 học viên.");
                    break;
                }

                System.out.println("+------------+----------------------------------------------------+------------------------+");
                System.out.printf("| %-10s | %-50s | %-22s |\n", "Mã Số (ID)", "Tên Khóa Học Đông", "Số Học Viên Thực Tế");
                System.out.println("+------------+----------------------------------------------------+------------------------+");

                for (CourseStatisticDTO dto : reportList) {
                    System.out.printf("| %-10d | %-50s | %-22d |\n",
                            dto.getCourseId(),
                            dto.getCourseName(),
                            dto.getStudentCount()
                    );
                }
                System.out.println("+------------+----------------------------------------------------+------------------------+");

                System.out.println("\n--- [ĐIỀU HƯỚNG PHÂN TRANG LỌC] ---");
                if (currentPage < totalPages) System.out.println("N. Trang tiếp theo (Next)");
                if (currentPage > 1)         System.out.println("P. Trang trước đó (Previous)");
                System.out.println("E. Thoát xem kết quả lọc");
                System.out.print("Nhập hành động (N/P/E): ");

                String action = scanner.nextLine().trim().toUpperCase();
                switch (action) {
                    case "N":
                        if (currentPage < totalPages) currentPage++;
                        else System.out.println("[i] Đang ở trang kết quả cuối cùng!");
                        break;
                    case "P":
                        if (currentPage > 1) currentPage--;
                        else System.out.println("[i] Đang ở trang kết quả đầu tiên!");
                        break;
                    case "E":
                        viewing = false;
                        break;
                    default:
                        System.out.println("[X] Lệnh không hợp lệ! Chỉ chọn N, P hoặc E.");
                }
            } catch (DatabaseException e) {
                System.out.println("\n[LỖI HỆ THỐNG] Gặp sự cố khi kết nối dữ liệu lọc khóa học: " + e.getMessage());
                viewing = false;
            }
        }
    }

    private void pressEnterToContinue() {
        System.out.print("\nNhấn phím [Enter] để tiếp tục...");
        scanner.nextLine();
    }
}