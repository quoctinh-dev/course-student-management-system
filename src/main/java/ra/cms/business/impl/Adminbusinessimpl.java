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
    @Override
    public Optional<Admin> login(String username, String password) throws ValidationException, DatabaseException {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Tên đăng nhập không được để trống hoặc chỉ chứa dấu cách!");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Mật khẩu không được để trống hoặc chỉ chứa dấu cách!");
        }
        Optional<Admin> adminOpt = adminDao.findByUsername(username);

        if (adminOpt.isPresent())
        {
            Admin admin = adminOpt.get();

            if(BCryptUtil.verify(password,admin.getPassword()))
            {
                return Optional.of(admin);
            }
        }

        return Optional.empty();
    }
}
