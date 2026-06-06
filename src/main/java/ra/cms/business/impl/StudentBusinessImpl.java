package ra.cms.business.impl;

import ra.cms.business.IStudentBusiness;
import ra.cms.dao.IStudentDao;
import ra.cms.dao.impl.StudentDAOImpl;
import ra.cms.exception.ValidationException;
import ra.cms.model.Student;
import ra.cms.utils.BCryptUtil;

import java.util.Optional;

public class StudentBusinessImpl implements IStudentBusiness {

    private final IStudentDao studentDao = new StudentDAOImpl();

    @Override
    public Optional<Student> login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email đăng nhập không được để trống hoặc chỉ chứa dấu cách!");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Mật khẩu không được để trống hoặc chỉ chứa dấu cách!");
        }

        Optional<Student> studentOpt = studentDao.findByEmail(email.trim());

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();

            if (BCryptUtil.verify(password, student.getPassword())) {
                return Optional.of(student);
            }
        }

        return Optional.empty();
    }
}