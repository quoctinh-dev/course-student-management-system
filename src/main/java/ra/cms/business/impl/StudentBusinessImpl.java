package ra.cms.business.impl;

import ra.cms.business.IStudentBusiness;
import ra.cms.dao.IStudentDao;
import ra.cms.dao.impl.StudentDAOImpl;
import ra.cms.exception.BusinessException;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Student;
import ra.cms.utils.BCryptUtil;

import java.util.List;
import java.util.Optional;

public class StudentBusinessImpl implements IStudentBusiness {

    private final IStudentDao studentDao = new StudentDAOImpl();

    @Override
    public Optional<Student> login(String email, String password) throws ValidationException, DatabaseException {
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
    @Override
    public List<Student> findAll() throws DatabaseException {
        List<Student> students = studentDao.findAll();
        return students;
    }

    @Override
    public void createStudent(Student student) throws ValidationException, BusinessException, DatabaseException {
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new ValidationException("Tên học viên không được để trống!");
        }
        if (student.getDob() == null) {
            throw new ValidationException("Ngày sinh không được để trống!");
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email không được để trống!");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!student.getEmail().matches(emailRegex)) {
            throw new ValidationException("Định dạng email không hợp lệ (Ví dụ đúng: example@gmail.com)!");
        }
        if (student.getPhone() != null && !student.getPhone().trim().isEmpty()) {
            String phoneRegex = "^[0-9]{9,11}$";
            if (!student.getPhone().matches(phoneRegex)) {
                throw new ValidationException("Số điện thoại phải từ 9 đến 11 chữ số!");
            }
        }
        if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
            throw new ValidationException("Mật khẩu không được để trống!");
        }

        if (studentDao.existsByEmail(student.getEmail().trim())) {
            throw new BusinessException("Email này đã được đăng ký bởi một học viên khác!");
        }

        String hashedPassword = BCryptUtil.hash(student.getPassword());
        student.setPassword(hashedPassword);

        studentDao.create(student);
    }
    @Override
    public Student getStudentById(Long id) throws ValidationException, BusinessException, DatabaseException {
        if (id == null || id <= 0) {
            throw new ValidationException("ID học viên yêu cầu không hợp lệ!");
        }
        Student student = studentDao.getById(id);
        if (student == null) {
            throw new BusinessException("Không tìm thấy học viên nào tương ứng với ID: " + id);
        }
        return student;
    }

    @Override
    public void updateStudent(Student student, boolean isPasswordChanged) throws ValidationException, BusinessException, DatabaseException {

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new ValidationException("Tên học viên không được bỏ trống!");
        }
        if (student.getDob() == null) {
            throw new ValidationException("Ngày sinh không được bỏ trống!");
        }
        if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email không được bỏ trống!");
        }
        if (!student.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Định dạng email cập nhật không hợp lệ!");
        }
        if (student.getPhone() != null && !student.getPhone().trim().isEmpty()) {
            if (!student.getPhone().matches("^[0-9]{9,11}$")) {
                throw new ValidationException("Số điện thoại cập nhật phải từ 9 đến 11 chữ số!");
            }
        }

        if (studentDao.existsByEmailForUpdate(student.getEmail().trim(), student.getId())) {
            throw new BusinessException("Email cập nhật này đã tồn tại trên hệ thống ở một tài khoản khác!");
        }

        if (isPasswordChanged) {
            if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
                throw new ValidationException("Mật khẩu mới không được để trống!");
            }
            String hashedPassword = BCryptUtil.hash(student.getPassword());
            student.setPassword(hashedPassword);
        }

        studentDao.update(student);
    }
    @Override
    public void deleteStudent(Long id) throws ValidationException, BusinessException, DatabaseException {
        if (id == null || id <= 0) {
            throw new ValidationException("ID học viên để xóa không hợp lệ!");
        }

        Student student = studentDao.getById(id);
        if (student == null) {
            throw new BusinessException("Thao tác thất bại: Không tìm thấy học viên có ID " + id + " trên hệ thống!");
        }

        studentDao.delete(id);
    }
    @Override
    public List<Student> searchStudents(String keyword) throws ValidationException, DatabaseException {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Từ khóa tìm kiếm không được phép để trống!");
        }

        return studentDao.search(keyword.trim());
    }
    @Override
    public List<Student> getSortedStudents(int option) throws ValidationException, DatabaseException {
        if (option < 1 || option > 4) {
            throw new ValidationException("Tiêu chí sắp xếp lựa chọn không hợp lệ!");
        }
        return studentDao.getSortedStudents(option);
    }
}