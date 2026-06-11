package ra.cms.dao;

import ra.cms.exception.DatabaseException;
import ra.cms.model.Enrollment;
import ra.cms.model.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

public interface IEnrollmentDAO {
    boolean existByStudentAndCourse(Long studentId, Long courseId);

    void register (Long studentId, Long courseId) throws DatabaseException;

    List<Enrollment> getHistoryByStudent(Long studentId, int sortOption) throws DatabaseException;

    Optional<Enrollment> findById(Long enrollmentId) throws DatabaseException;
    void deleteById(Long enrollmentId) throws DatabaseException;

    List<Enrollment> findByCourseId(Long courseId) throws DatabaseException;

    void updateStatus(Long enrollmentId, EnrollmentStatus status) throws DatabaseException;


}