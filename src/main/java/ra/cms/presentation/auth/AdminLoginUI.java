package ra.cms.presentation.auth;

import ra.cms.business.IAdminbusiness;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Admin;
import ra.cms.presentation.admin.AdminMenuUI;

import java.util.Optional;
import java.util.Scanner;

public class AdminLoginUI {
    private final IAdminbusiness adminBusiness;
    private final Scanner scanner;

    public AdminLoginUI(IAdminbusiness adminBusiness, Scanner scanner) {
        this.adminBusiness = adminBusiness;
        this.scanner = scanner;
    }

    public void showLoginMenu() {
        try {
            System.out.println("\n--- ĐĂNG NHẬP QUẢN TRỊ VIÊN ---");
            System.out.print("Nhập username: ");
            String username = scanner.nextLine();
            System.out.print("Nhập password: ");
            String password = scanner.nextLine();

            System.out.println("Đang xác thực thông tin...");
            Optional<Admin> adminOpt = adminBusiness.login(username, password);

            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                System.out.println("Đăng nhập thành công! Chào mừng Admin: " + admin.getUsername());

                AdminMenuUI adminMenuUI = new AdminMenuUI(adminBusiness, scanner, admin);
                adminMenuUI.showAdminMenu();
            } else {
                System.err.println("Đăng nhập thất bại: Sai tài khoản hoặc mật khẩu!");
            }
        } catch (ValidationException e) {
            System.err.println("Cảnh báo nhập liệu: " + e.getMessage());
        } catch (DatabaseException e) {
            System.err.println("Hệ thống gặp sự cố: " + e.getMessage());
        }
    }
}