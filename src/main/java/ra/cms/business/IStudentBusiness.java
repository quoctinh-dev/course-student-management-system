package ra.cms.business;

import ra.cms.dto.CourseStatisticDTO;
import ra.cms.exception.BusinessException;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Student;

import java.util.List;
import java.util.Optional;

public interface IStudentBusiness {
    Optional<Student> login(String email, String password) throws ValidationException, DatabaseException;
    List<Student> findAll() throws DatabaseException;
    void createStudent(Student student) throws ValidationException, BusinessException, DatabaseException;
    Student getStudentById(Long id) throws ValidationException, BusinessException, DatabaseException;
    void updateStudent(Student student, boolean isPasswordChanged) throws ValidationException, BusinessException, DatabaseException;
    void deleteStudent(Long id) throws ValidationException, BusinessException, DatabaseException;
    List<Student> searchStudents(String keyword) throws ValidationException, DatabaseException;
    List<Student> getSortedStudents(int option) throws ValidationException, DatabaseException;
    void changePassword(Student currentStudent, String oldPassword, String verificationInput, String newPassword, String confirmPassword) throws ValidationException, BusinessException,DatabaseException;

    // PHÂN TRANG NÂNG CAO
    List<Student> findWithPaginationAndSort(int page, int size, int sortOption) throws DatabaseException;
    int countAll() throws DatabaseException;
    List<Student> searchWithPagination(String keyword, int page, int size) throws DatabaseException;
    int countSearch(String keyword) throws DatabaseException;

    // ĐỀ XUẤT KHÓA HỌC NÂNG CAO
    List<CourseStatisticDTO> getRecommendedCoursesWithPagination(long studentId, int page, int size) throws DatabaseException;
    int countTotalRecommendedCourses(long studentId) throws DatabaseException;
}

