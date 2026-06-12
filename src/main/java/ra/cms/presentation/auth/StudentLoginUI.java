package ra.cms.presentation.auth;

import ra.cms.business.ICourseBusiness;
import ra.cms.business.IEnrollmentBusiness;
import ra.cms.business.IStudentBusiness;
import ra.cms.business.impl.CourseBusinessImpl;
import ra.cms.business.impl.EnrollmentBusinessImpl;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Student;
import ra.cms.presentation.student.StudentMenuUI;

import java.util.Optional;
import java.util.Scanner;

public class StudentLoginUI {
    private final IStudentBusiness studentBusiness;
    private final Scanner scanner;

    public StudentLoginUI(IStudentBusiness studentBusiness, Scanner scanner) {
        this.studentBusiness = studentBusiness;
        this.scanner = scanner;
    }

    public void showStudentLoginMenu() {
        try {
            System.out.println("\n--- ĐĂNG NHẬP HỌC VIÊN ---");
            System.out.print("Nhập email học viên: ");
            String email = scanner.nextLine().trim();
            System.out.print("Nhập mật khẩu: ");
            String password = scanner.nextLine().trim();

            System.out.println("Đang kết nối xác thực tài khoản Học viên...");
            Optional<Student> studentOpt = studentBusiness.login(email, password);

            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                System.out.println("Đăng nhập thành công! Chào mừng Học viên: " + student.getName());

                ICourseBusiness courseBusiness = new CourseBusinessImpl();
                IEnrollmentBusiness enrollmentBusiness = new EnrollmentBusinessImpl();

                StudentMenuUI studentMenuUI = new StudentMenuUI(studentBusiness, enrollmentBusiness, courseBusiness, scanner, student);
                studentMenuUI.showStudentMenu();

            } else {
                System.err.println("Đăng nhập thất bại: Sai tài khoản email hoặc mật khẩu Học viên!");
            }

        } catch (ValidationException e) {
            System.err.println("Cảnh báo nhập liệu: " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("Hệ thống gặp sự cố: " + e.getMessage());
        }
    }
}