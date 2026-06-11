    package ra.cms.presentation.admin;

    import ra.cms.exception.BusinessException;
    import ra.cms.exception.DatabaseException;
    import ra.cms.business.IStudentBusiness;
    import ra.cms.exception.ValidationException;
    import ra.cms.model.Student;

    import java.time.format.DateTimeParseException;
    import java.util.List;
    import java.util.Scanner;

    public class StudentUI {
        private final IStudentBusiness studentBusiness;
        private final Scanner scanner;

        public StudentUI(IStudentBusiness studentBusiness, Scanner scanner) {
            this.studentBusiness = studentBusiness;
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
                    case "1": handleDisplayAllStudents(); break;
                    case "2": handleCreateStudent(); break;
                    case "3": handleUpdateStudent(); break;
                    case "4": handleDeleteStudent(); break;
                    case "5": handleSearchStudents(); break;
                    case "6":handleSortStudents(); break;
                    case "7": inLoop = false; break;
                    default: System.err.println("Lựa chọn không hợp lệ!");
                }
            }
        }
        private void handleDisplayAllStudents() {
            try {
                List<Student> list = studentBusiness.findAll();
                printStudentTable(list);
            } catch (DatabaseException e) {
                System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
            }
            pressEnterToContinue();
        }


        private void handleCreateStudent() {
            System.out.println("\n================ THÊM MỚI HỌC VIÊN ================");

            System.out.print("Nhập họ và tên học viên: ");
            String name = scanner.nextLine();

            System.out.print("Nhập ngày sinh (Định dạng YYYY-MM-DD - Ví dụ: 2005-10-15): ");
            java.time.LocalDate dob = null;
            try {
                dob = java.time.LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.err.println("[LỖI] Định dạng ngày sinh không chuẩn! Phải theo mẫu YYYY-MM-DD.");
                pressEnterToContinue();
                return;
            }

            System.out.print("Nhập email: ");
            String email = scanner.nextLine();

            System.out.print("Nhập giới tính (Chọn 1: Nam / Chọn 2: Nữ): ");
            String sexChoice = scanner.nextLine().trim();
            boolean sex = sexChoice.equals("1");

            System.out.print("Nhập số điện thoại (Ấn Enter nếu không muốn nhập): ");
            String phone = scanner.nextLine();
            if (phone.trim().isEmpty()) phone = null;

            System.out.print("Nhập mật khẩu tài học viên: ");
            String password = scanner.nextLine();

            Student newStudent = new Student();
            newStudent.setName(name);
            newStudent.setDob(dob);
            newStudent.setEmail(email);
            newStudent.setSex(sex);
            newStudent.setPhone(phone);
            newStudent.setPassword(password);

            try {
                studentBusiness.createStudent(newStudent);
                System.out.println("\n[THÀNH CÔNG] Đã tạo tài khoản học viên thành công!");
            } catch (ValidationException e) {
                System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
            } catch (BusinessException e) {
                System.err.println("[LỖI NGHIỆP VỤ] " + e.getMessage());
            } catch (DatabaseException e) {
                System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
            }
            pressEnterToContinue();
        }
        private void handleUpdateStudent() {
            System.out.println("\n================ CHỈNH SỬA THÔNG TIN HỌC VIÊN ================");
            System.out.print("Nhập ID học viên cần sửa: ");
            long id;
            try {
                id = Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("[LỖI] ID nhập vào phải là một số nguyên!");
                pressEnterToContinue();
                return;
            }

            try {
                Student existingStudent = studentBusiness.getStudentById(id);
                boolean isPasswordChanged = false; // Biến cờ hiệu theo dõi xem admin có chạm vào mật khẩu không

                boolean inSubMenu = true;
                while (inSubMenu) {
                    System.out.println("\n-- CHỌN THUỘC TÍNH CẦN SỬA ĐỔI --");
                    System.out.println("1. Sửa họ tên      (Hiện tại: " + existingStudent.getName() + ")");
                    System.out.println("2. Sửa ngày sinh   (Hiện tại: " + existingStudent.getDob() + ")");
                    System.out.println("3. Sửa giới tính   (Hiện tại: " + (existingStudent.isSex() ? "Nam" : "Nữ") + ")");
                    System.out.println("4. Sửa email       (Hiện tại: " + existingStudent.getEmail() + ")");
                    System.out.println("5. Sửa số ĐT       (Hiện tại: " + (existingStudent.getPhone() != null ? existingStudent.getPhone() : "Chưa cập nhật") + ")");
                    System.out.println("6. Đổi mật khẩu mới");
                    System.out.println("7. [Xong] Lưu tất cả cấu hình và thoát");
                    System.out.print("Lựa chọn thuộc tính (1-7): ");

                    String subChoice = scanner.nextLine().trim();
                    switch (subChoice) {
                        case "1":
                            System.out.print("Nhập họ tên mới: ");
                            existingStudent.setName(scanner.nextLine());
                            break;
                        case "2":
                            System.out.print("Nhập ngày sinh mới (YYYY-MM-DD): ");
                            try {
                                existingStudent.setDob(java.time.LocalDate.parse(scanner.nextLine().trim()));
                            } catch (java.time.format.DateTimeParseException e) {
                                System.err.println("[LỖI] Sai định dạng ngày! Thao tác sửa ngày sinh bị hủy bỏ.");
                            }
                            break;
                        case "3":
                            System.out.print("Chọn giới tính mới (1: Nam / 2: Nữ): ");
                            String sexOption = scanner.nextLine().trim();
                            existingStudent.setSex(sexOption.equals("1"));
                            break;
                        case "4":
                            System.out.print("Nhập địa chỉ email mới: ");
                            existingStudent.setEmail(scanner.nextLine());
                            break;
                        case "5":
                            System.out.print("Nhập số điện thoại mới: ");
                            String newPhone = scanner.nextLine();
                            existingStudent.setPhone(newPhone.trim().isEmpty() ? null : newPhone);
                            break;
                        case "6":
                            System.out.print("Nhập mật khẩu mới cho học viên: ");
                            existingStudent.setPassword(scanner.nextLine());
                            isPasswordChanged = true; // Bật cờ hiệu thông báo mật khẩu có thay đổi
                            break;
                        case "7":
                            inSubMenu = false;
                            break;
                        default:
                            System.err.println("[LỖI] Lựa chọn không nằm trong danh mục!");
                    }
                }

                studentBusiness.updateStudent(existingStudent, isPasswordChanged);
                System.out.println("\n[THÀNH CÔNG] Cập nhật thông tin học viên hoàn tất!");

            } catch (ra.cms.exception.ValidationException e) {
                System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
            } catch (ra.cms.exception.BusinessException e) {
                System.err.println("[LỖI NGHIỆP VỤ] " + e.getMessage());
            } catch (ra.cms.exception.DatabaseException e) {
                System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
            }
            pressEnterToContinue();
        }

        private void handleDeleteStudent() {
            System.out.println("\n================ XÓA TÀI KHOẢN HỌC VIÊN ================");
            System.out.print("Nhập ID học viên muốn xóa: ");
            long id;
            try {
                id = Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println("[LỖI] ID học viên nhập vào bắt buộc phải là số!");
                pressEnterToContinue();
                return;
            }

            try {
                Student student = studentBusiness.getStudentById(id);
                System.out.println("\n-> BẠN ĐANG CHỌN XÓA HỌC VIÊN THÔNG TIN SAU:");
                System.out.println("   Họ và tên: " + student.getName());
                System.out.println("   Email:     " + student.getEmail());
                System.out.println("   Ngày sinh: " + student.getDob());

                System.out.print("\nBạn có chắc chắn muốn xóa học viên này vĩnh viễn không? (Gõ Y để xóa / Phím bất kỳ để hủy): ");
                String confirm = scanner.nextLine().trim();

                if (confirm.equalsIgnoreCase("Y")) {
                    studentBusiness.deleteStudent(id);
                    System.out.println("\n[THÀNH CÔNG] Đã xóa tài khoản học viên ra khỏi hệ thống!");
                } else {
                    System.out.println("\n[THÔNG BÁO] Thao tác xóa đã được hủy bỏ an toàn.");
                }

            } catch (ra.cms.exception.ValidationException e) {
                System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
            } catch (ra.cms.exception.BusinessException e) {
                System.err.println("[LỖI NGHIỆP VỤ] " + e.getMessage());
            } catch (ra.cms.exception.DatabaseException e) {
                System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
            }
            pressEnterToContinue();
        }

        private void handleSearchStudents() {
            System.out.println("\n================ TÌM KIẾM HỌC VIÊN ĐA NĂNG ================");
            System.out.print("Nhập từ khóa cần tìm (Tên, Email hoặc ID): ");
            String keyword = scanner.nextLine();

            try {
                List<Student> resultList = studentBusiness.searchStudents(keyword);

                System.out.println("\nKẾT QUẢ TÌM KIẾM CHO TỪ KHÓA '" + keyword + "':");
                printStudentTable(resultList);

            } catch (ra.cms.exception.ValidationException e) {
                System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
            } catch (ra.cms.exception.DatabaseException e) {
                System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
            }
            pressEnterToContinue();
        }
        private void handleSortStudents() {
            System.out.println("\n================ SẮP XẾP DANH SÁCH HỌC VIÊN ================");
            System.out.println("1. Sắp xếp theo ID tăng dần");
            System.out.println("2. Sắp xếp theo ID giảm dần");
            System.out.println("3. Sắp xếp theo Tên từ A-Z");
            System.out.println("4. Sắp xếp theo Tên từ Z-A");
            System.out.println("5. Quay lại");
            System.out.print("Mời chọn tiêu chí (1-5): ");

            try {
                int option = Integer.parseInt(scanner.nextLine().trim());
                if (option == 5) return;

                List<Student> sortedList = studentBusiness.getSortedStudents(option);

                System.out.println("\nDANH SÁCH HỌC VIÊN SAU KHI SẮP XẾP:");
                printStudentTable(sortedList);

            } catch (NumberFormatException e) {
                System.err.println("[LỖI] Lựa chọn tiêu chí bắt buộc phải là số nguyên!");
            } catch (ra.cms.exception.ValidationException e) {
                System.err.println("[LỖI NHẬP LIỆU] " + e.getMessage());
            } catch (ra.cms.exception.DatabaseException e) {
                System.err.println("[LỖI HỆ THỐNG] " + e.getMessage());
            }
            pressEnterToContinue();
        }

        private void printStudentTable(List<Student> list) {
            if (list.isEmpty()) {
                System.out.println("\n[THÔNG BÁO] Hiện tại danh sách học viên trống.");
                return;
            }
            System.out.println("\n+------+---------------------------+------------+------------+--------------------------------+-----------------+");
            System.out.printf("| %-4s | %-25s | %-10s | %-10s | %-30s | %-15s |\n",
                    "ID", "Họ Và Tên", "Ngày Sinh", "Giới Tính", "Email", "Số Điện Thoại");
            System.out.println("+------+---------------------------+------------+------------+--------------------------------+-----------------+");

            for (Student s : list) {
                String dobStr = (s.getDob() != null) ? s.getDob().toString() : "N/A";
                String genderStr = s.isSex() ? "Nam" : "Nữ";

                System.out.printf("| %-4d | %-25s | %-10s | %-10s | %-30s | %-15s |\n",
                        s.getId(),
                        s.getName(),
                        dobStr,
                        genderStr,
                        s.getEmail(),
                        s.getPhone() != null ? s.getPhone() : "N/A");
            }
            System.out.println("+------+---------------------------+------------+------------+--------------------------------+-----------------+");
            System.out.println("Tổng số lượng: " + list.size() + " học viên.");
        }



        private void pressEnterToContinue() {
            System.out.print("\nNhấn phím [Enter] để tiếp tục...");
            scanner.nextLine();
        }

    }