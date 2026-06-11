package ra.cms.business;

import ra.cms.exception.BusinessException;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Enrollment;

import java.util.List;

public interface IEnrollmentBusiness {
    void registerCourse(Long studentId, Long courseId) throws ValidationException, BusinessException, DatabaseException;

    List<Enrollment> getHistoryByStudent(Long studentId, int sortOption) throws DatabaseException;

    void cancelEnrollment(Long studentId, Long enrollmentId) throws ValidationException, BusinessException, DatabaseException;
}
