package ra.cms.business.impl;

import ra.cms.business.IAdminbusiness;
import ra.cms.dao.IAdminDao;
import ra.cms.dao.impl.AdminDAOImpl;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Admin;
import ra.cms.utils.BCryptUtil;

import java.util.Optional;

public class Adminbusinessimpl implements IAdminbusiness  {


    private final IAdminDao adminDao = new AdminDAOImpl();

    // CHỨC NĂNG ĐĂNG NHẬP CỦA ADMIN; ĐĂNG NHẬP DỰA TRÊN USERNAME VÀ MẬT KHẨU
    @Override
    public Optional<Admin> login(String username, String password) throws ValidationException, DatabaseException {

        // 1. KIỂM TRA TÍNH HỢP LỆ CỦA DỮ LIỆU
        if (username == null || username.isBlank()) {
            throw new ValidationException("Tên đăng nhập không được để trống hoặc chỉ chứa dấu cách!");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Mật khẩu không được để trống hoặc chỉ chứa dấu cách!");
        }

        // 2. SỬ DỤNG DAO ĐỂ TÌM ADMIN THEO HỌ TÊN
        Optional<Admin> adminOpt = adminDao.findByUsername(username);

        // 3. DÙNG OPTINAL ĐỂ KIỂM TRA CÓ TỒN TẠI KHÔNG, NẾU TỒN TẠI THÌ DÙNG GET ĐỂ LẤY RA
        if (adminOpt.isPresent())
        {
            // TẠO RA ADMIN MỚI ĐỂ NHẬN
            Admin admin = adminOpt.get();

            // DÙNG HÀM NÀY DỂ MÀ XÁC NHẬN CÓ ĐÚNG MẬT KHẨU KHÔNG
            if(BCryptUtil.verify(password,admin.getPassword()))
            {
                // DÙNG OF CỦA OPT ĐỂ CHUYỂN ADMIN THÀNH OPT
                return Optional.of(admin);
            }
        }

        // TRẢ VỀ EMPTY
        return Optional.empty();
    }
}
