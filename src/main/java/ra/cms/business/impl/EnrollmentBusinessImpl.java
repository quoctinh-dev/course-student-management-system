package ra.cms.business.impl;

import ra.cms.business.IEnrollmentBusiness;
import ra.cms.dao.ICourseDAO;
import ra.cms.dao.IEnrollmentDAO;
import ra.cms.dao.impl.CourseDAOImpl;
import ra.cms.dao.impl.EnrollmentDAOImpl;
import ra.cms.exception.BusinessException;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Course;
import ra.cms.model.Enrollment;
import ra.cms.model.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

public class EnrollmentBusinessImpl implements IEnrollmentBusiness {

    private final IEnrollmentDAO enrollmentDAO = new EnrollmentDAOImpl();

    private final ICourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void registerCourse(Long studentId, Long courseId) throws ValidationException, BusinessException, DatabaseException {
        if (courseId == null || courseId <= 0) {
            throw new ValidationException("Mã số ID khóa học không hợp lệ!");
        }

        Optional<Course> courseOptional = courseDAO.findById(courseId);
        if (courseOptional.isEmpty()) {
            throw new BusinessException("Đăng ký lỗi: Khóa học mang mã số " + courseId + " không tồn tại trên hệ thống!");
        }

        Course realCourse = courseOptional.get();


        if (enrollmentDAO.existByStudentAndCourse(studentId, courseId)) {
            throw new BusinessException("Bạn đã nộp đơn đăng ký khóa học này rồi và đơn đang được xử lý. Vui lòng không gửi trùng lặp!");
        }

        enrollmentDAO.register(studentId, courseId);
    }

    @Override
    public List<Enrollment> getHistoryByStudent(Long studentId, int sortOption) throws DatabaseException {
        return enrollmentDAO.getHistoryByStudent(studentId, sortOption);
    }

    @Override
    public void cancelEnrollment(Long studentId, Long enrollmentId) throws ValidationException, BusinessException, DatabaseException {
        if (enrollmentId == null || enrollmentId <= 0) {
            throw new ValidationException("Mã đơn đăng ký không hợp lệ!");
        }

        Optional<Enrollment> enrollOpt = enrollmentDAO.findById(enrollmentId);
        if (enrollOpt.isEmpty()) {
            throw new BusinessException("Hủy đơn thất bại: Không tìm thấy đơn đăng ký nào mang mã số #" + enrollmentId);
        }

        Enrollment enrollment = enrollOpt.get();

        if (!enrollment.getStudent().getId().equals(studentId)) {
            throw new BusinessException("Hành vi bị chặn: Bạn không có quyền hủy đơn đăng ký của học viên khác!");
        }

        if (enrollment.getStatus() != EnrollmentStatus.WAITING) {
            throw new BusinessException("Hủy đơn thất bại: Khóa học '" + enrollment.getCourse().getName() +
                    "' đã được xử lý ở trạng thái [" + enrollment.getStatus() + "], không thể tự ý hủy!");
        }

        enrollmentDAO.deleteById(enrollmentId);

    }

}