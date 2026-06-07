package ra.cms;

import ra.cms.business.IAdminbusiness;
import ra.cms.business.IStudentBusiness;
import ra.cms.business.impl.Adminbusinessimpl;
import ra.cms.business.impl.StudentBusinessImpl;
import ra.cms.presentation.MainMenuUI;

import java.util.Scanner;

public class Main {
    private static final IAdminbusiness adminBusiness = new Adminbusinessimpl();
    private static final IStudentBusiness studentBusiness = new StudentBusinessImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        MainMenuUI mainMenuUI = new MainMenuUI(adminBusiness, studentBusiness, scanner);
        mainMenuUI.showMainMenu();
    }
}