package ra.cms.dao;

import ra.cms.dto.CourseStatisticDTO;
import ra.cms.exception.DatabaseException;
import ra.cms.model.Student;

import java.util.List;
import java.util.Optional;

public interface IStudentDao {
    // HÀM findByEmail hổ trợ tìm kiếm để mà đăng nhập cho student
    Optional<Student> findByEmail(String email) throws DatabaseException;

    List<Student> findAll() throws DatabaseException;
    boolean existsByEmail(String email) throws DatabaseException;
    void create(Student student) throws DatabaseException;
    Student getById(Long id) throws DatabaseException;
    boolean existsByEmailForUpdate(String email, Long currentId) throws DatabaseException;
    void update(Student student) throws DatabaseException;
    void delete(Long id) throws DatabaseException;
    List<Student> search(String keyword) throws DatabaseException;
    List<Student> getSortedStudents(int option) throws DatabaseException;
    void updatePassword(Long studentId, String newPassword) throws DatabaseException;

    // PHÂN TRANG NÂNG CAO
    List<Student> findWithPaginationAndSort(int page, int size, int sortOption) throws DatabaseException;
    int countAll() throws DatabaseException;
    List<Student> searchWithPagination(String keyword, int page, int size) throws DatabaseException;
    int countSearch(String keyword) throws DatabaseException;

    // ĐỀ XUẤT KHÓA HỌC NÂNG CAO
    List<CourseStatisticDTO> getRecommendedCoursesWithPagination(long studentId, int page, int size) throws DatabaseException;
    int countTotalRecommendedCourses(long studentId) throws DatabaseException;
}
