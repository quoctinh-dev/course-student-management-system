package ra.cms.presentation.admin;

import ra.cms.business.ICourseBusiness;
import ra.cms.exception.BusinessException;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Course;

import java.util.List;
import java.util.Scanner;

public class CourseUI {
    private final ICourseBusiness courseBusiness;
    private final Scanner scanner;

    public CourseUI(ICourseBusiness courseBusiness, Scanner scanner) {
        this.courseBusiness = courseBusiness;
        this.scanner = scanner;
    }

    public void showCourseManagementMenu() {
        boolean inLoop = true;
        while (inLoop) {
            System.out.println("\n--- [2.1] MENU QUẢN LÝ KHÓA HỌC ---");
            System.out.println("1. Hiển thị danh sách khóa học");
            System.out.println("2. Thêm mới khóa học");
            System.out.println("3. Chỉnh sửa thông tin khóa học");
            System.out.println("4. Xóa khóa học (Xác nhận trước khi xóa)");
            System.out.println("5. Tìm kiếm theo tên (Tương đối)");
            System.out.println("6. Sắp xếp theo tên hoặc ID (Tăng/Giảm dần)");
            System.out.println("7. Quay về menu chính");
            System.out.print("Nhập lựa chọn (1-7): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":handleDisplayAllCoursesWithPagination();
                        pressEnterToContinue()
                        ;break;
                case "2": handleCreateCourse(); break;
                case "3": handleUpdateCourse(); break;
                case "4": handleDeleteCourse(); break;
                case "5": handleSearchCourses(); break;
                case "6": handleSortCourses(); break;
                case "7": inLoop = false; break;
                default: System.err.println("[LỖI] Lựa chọn không hợp lệ!");
            }
        }
    }

    /***
     * CHỨC NĂNG 1: Hiển thị danh sách khóa học
     */
    // NÂNG CAO PHÂN TRANG
    private void handleDisplayAllCoursesWithPagination() {
        int currentPage = 1;
        int pageSize = 5;
        boolean viewing = true;

        while (viewing) {
            try {
                int totalCourses = courseBusiness.countAll();
                int totalPages = (int) Math.ceil((double) totalCourses / pageSize);
                if (totalPages == 0) totalPages = 1;

                List<Course> pagedList = courseBusiness.findWithPagination(currentPage, pageSize);

                System.out.println("\n================ DANH SÁCH KHÓA HỌC (TRANG " + currentPage + "/" + totalPages + ") ================");

                if (pagedList.isEmpty()) {
                    System.out.println("[ℹ] Không có dữ liệu ở trang này.");
                } else {
                    printCourseTable(pagedList);
                }

                System.out.println("\n--- [ĐIỀU HƯỚNG PHÂN TRANG] ---");
                if (currentPage < totalPages)
                    System.out.println("N. Trang tiếp theo (Next)");
                if (currentPage > 1)
                    System.out.println("P. Trang trước đó (Previous)");
                System.out.println("E. Thoát xem danh sách");
                System.out.print("Nhập hành động (N/P/E): ");

                String action = scanner.nextLine().trim().toUpperCase();
                switch (action) {
                    case "N":
                        if (currentPage < totalPages) currentPage++;
                        else System.out.println("[i] Bạn đang ở trang cuối cùng!");
                        break;
                    case "P":
                        if (currentPage > 1) currentPage--;
                        else System.out.println("[i] Bạn đang ở trang đầu tiên!");
                        break;
                    case "E":
                        viewing = false;
                        break;
                    default:
                        System.out.println("[x] Lệnh không hợp lệ! Chỉ chọn N, P hoặc E.");
                }

            } catch (DatabaseException e) {
                System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
                viewing = false;
            }
        }
    }

    // HIỆN THỊ DANH SÁCH KHÓA HỌC KHÔNG CÓ PHẦN TRANG
    private void handleDisplayAllCourses() {
        try {
            List<Course> list = courseBusiness.getAllCourses();
            printCourseTable(list);
        } catch (DatabaseException e) {
            System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
        }

    }

    /***
     * CHỨC NĂNG 2: Thêm mới khóa học
     */

    private void handleCreateCourse() {
        System.out.println("\n================ THÊM MỚI KHÓA HỌC ================");
        System.out.print("Nhập tên khóa học: ");
        String name = scanner.nextLine();

        System.out.print("Nhập thời lượng (giờ): ");
        int duration = inputInteger();
        if (duration == -1) return;

        System.out.print("Nhập tên giảng viên phụ trách: ");
        String instructor = scanner.nextLine();

        Course newCourse = new Course();
        newCourse.setName(name);
        newCourse.setDuration(duration);
        newCourse.setInstructor(instructor);

        try {
            courseBusiness.createCourse(newCourse);
            System.out.println("\n[THÀNH CÔNG] Thêm mới khóa học thành công!");
        } catch (ValidationException e) {
            System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
        } catch (BusinessException e) {
            System.err.println("[LỖI NGHIỆP VỤ] " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
        }
        pressEnterToContinue();
    }

    /***
     * CHỨC NĂNG 3: chỉnh sủa khóa học
     */
    private void handleUpdateCourse() {
        System.out.println("\n================ CHỈNH SỬA KHÓA HỌC ================");
        System.out.print("Nhập ID khóa học cần chỉnh sửa: ");
        long id = inputLong();
        if (id == -1) return;

        try {
            Course existingCourse = courseBusiness.getCourseById(id);

            boolean inSubMenu = true;
            while (inSubMenu) {
                System.out.println("\n--- THÔNG TIN HIỆN TẠI CỦA KHÓA HỌC ---");
                System.out.println("ID: " + existingCourse.getId());
                System.out.println("1. Tên khóa học: " + existingCourse.getName());
                System.out.println("2. Thời lượng:  " + existingCourse.getDuration() + " giờ");
                System.out.println("3. Giảng viên:   " + existingCourse.getInstructor());
                System.out.println("4. [Xong] Lưu cấu hình và thoát");
                System.out.print("Chọn thuộc tính muốn thay đổi (1-4): ");

                String subChoice = scanner.nextLine().trim();
                switch (subChoice) {
                    case "1":
                        System.out.print("Nhập Tên mới: ");
                        existingCourse.setName(scanner.nextLine());
                        break;
                    case "2":
                        System.out.print("Nhập Thời lượng mới (giờ): ");
                        int newDuration = inputInteger();
                        if (newDuration != -1) existingCourse.setDuration(newDuration);
                        break;
                    case "3":
                        System.out.print("Nhập Giảng viên mới: ");
                        existingCourse.setInstructor(scanner.nextLine());
                        break;
                    case "4":
                        inSubMenu = false;
                        break;
                    default:
                        System.err.println("[LỖI] Lựa chọn thuộc tính không hợp lệ!");
                }
            }

            courseBusiness.updateCourse(existingCourse);
            System.out.println("\n[THÀNH CÔNG] Cập nhật thông tin khóa học thành công!");

        } catch (ValidationException e) {
            System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
        } catch (BusinessException e) {
            System.err.println("[LỖI NGHIỆP VỤ] " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
        }
        pressEnterToContinue();
    }

    /***
     * CHỨC NĂNG 4: Xóa khóa học
     */
    private void handleDeleteCourse() {
        System.out.println("\n================ XÓA KHÓA HỌC ================");
        System.out.print("Nhập ID khóa học cần xóa: ");
        long id = inputLong();
        if (id == -1) return;

        try {
            Course course = courseBusiness.getCourseById(id);
            System.out.println("\n-> Bạn đang chọn xóa khóa học: " + course.getName() + " (Giảng viên: " + course.getInstructor() + ")");

            System.out.print("Bạn có chắc chắn muốn xóa khóa học này không? (Y/N): ");
            String confirm = scanner.nextLine().trim();

            if (confirm.equalsIgnoreCase("Y")) {
                courseBusiness.deleteCourse(id);
                System.out.println("\n[THÀNH CÔNG] Khóa học đã được loại bỏ khỏi hệ thống!");
            } else {
                System.out.println("\n[THÔNG BÁO] Thao tác xóa đã bị hủy bỏ.");
            }

        } catch (ValidationException e) {
            System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
        } catch (BusinessException e) {
            System.err.println("[LỖI NGHIỆP VỤ] " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
        }
        pressEnterToContinue();
    }

    /***
     * CHỨC NĂNG 5: Tìm kiếm khóa học
     */
    private void handleSearchCourses() {
        System.out.println("\n================ TÌM KIẾM KHÓA HỌC ================");
        System.out.print("Nhập từ khóa tên khóa học muốn tìm: ");
        String keyword = scanner.nextLine();

        try {
            List<Course> result = courseBusiness.searchCoursesByName(keyword);
            printCourseTable(result);
        } catch (ValidationException e) {
            System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
        } catch (BusinessException e) {
            System.err.println("[LỖI NGHIỆP VỤ] " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
        }
        pressEnterToContinue();
    }

    /***
     * CHỨC NĂNG 6: Xắp xếp khóa học theo 4 tiêu chí
     */
    private void handleSortCourses() {
        System.out.println("\n================ SẮP XẾP DANH SÁCH KHÓA HỌC ================");
        System.out.println("1. Sắp xếp theo ID tăng dần");
        System.out.println("2. Sắp xếp theo ID giảm dần");
        System.out.println("3. Sắp xếp theo Tên từ A-Z");
        System.out.println("4. Sắp xếp theo Tên từ Z-A");
        System.out.println("5. Quay lại");
        System.out.print("Mời nhập lựa chọn tiêu chí (1-5): ");

        try {
            int option = Integer.parseInt(scanner.nextLine().trim());
            if (option == 5) return;

            List<Course> sortedList = courseBusiness.getSortedCourses(option);
            printCourseTable(sortedList);

        } catch (NumberFormatException e) {
            System.err.println("[LỖI] Tiêu chí lựa chọn phải là số nguyên!");
        } catch (ValidationException e) {
            System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
        } catch (BusinessException e) {
            System.err.println("[LỖI NGHIỆP VỤ] " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
        }
        pressEnterToContinue();
    }

    /***
     * Các hàm tiện ích
     */

    // 1. In bảng của khóa học
    private void printCourseTable(List<Course> list) {
        if (list.isEmpty()) {
            System.out.println("\n[THÔNG BÁO] Không tìm thấy khóa học phù hợp với yêu cầu.");
            return;
        }

        System.out.println("\n+------+------------------------------------------+------------+---------------------------+");
        System.out.printf("| %-4s | %-40s | %-10s | %-25s |\n", "ID", "Tên Khóa Học", "Thời Lượng", "Giảng Viên");
        System.out.println("+------+------------------------------------------+------------+---------------------------+");

        for (Course c : list) {
            System.out.printf("| %-4d | %-40s | %-10s | %-25s |\n",
                    c.getId(), c.getName(), c.getDuration() + " giờ", c.getInstructor());
        }
        System.out.println("+------+------------------------------------------+------------+---------------------------+");
        System.out.println("Tổng cộng: " + list.size() + " kết quả.");
    }


    // 2.Kiểm tra nhập số nguyên
    private int inputInteger() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("[LỖI] Dữ liệu nhập vào bắt buộc phải là một số nguyên!");
            return -1;
        }
    }

    // 3. Kiểm tra nhập số nguyên Long
    private long inputLong() {
        try {
            return Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.err.println("[LỖI] ID nhập vào bắt buộc phải là số nguyên Long!");
            return -1;
        }
    }

    // 4. Dùng để dùng màn hình
    private void pressEnterToContinue() {
        System.out.print("\nNhấn phím [Enter] để tiếp tục...");
        scanner.nextLine();
    }
}