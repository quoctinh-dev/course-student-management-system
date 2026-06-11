package ra.cms.business;

import ra.cms.exception.BusinessException;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Course;

import java.util.List;

public interface ICourseBusiness {
    void createCourse(Course course) throws ValidationException, BusinessException, DatabaseException;
    List<Course> getAllCourses() throws DatabaseException ;
    Course getCourseById(Long id) throws ValidationException, BusinessException, DatabaseException;
    void updateCourse(Course course) throws ValidationException, BusinessException, DatabaseException;
    void deleteCourse(Long id) throws ValidationException, BusinessException, DatabaseException;
    List<Course> searchCoursesByName(String keyword) throws ValidationException, BusinessException, DatabaseException;
    List<Course> getSortedCourses(int option) throws ValidationException, BusinessException, DatabaseException;

    // NÂNG CAO PHÂN TRANG
    List<Course> findWithPagination(int page, int size) throws DatabaseException;
    int countAll() throws DatabaseException;

}
