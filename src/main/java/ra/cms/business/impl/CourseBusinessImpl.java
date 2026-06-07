package ra.cms.business.impl;

import ra.cms.business.ICourseBusiness;
import ra.cms.dao.ICourseDAO;
import ra.cms.dao.impl.CourseDAOImpl;
import ra.cms.exception.BusinessException;
import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Course;

import java.util.List;

public class CourseBusinessImpl implements ICourseBusiness {

    private final ICourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createCourse(Course course) throws ValidationException, BusinessException, DatabaseException {
        if (course == null) {
            throw new ValidationException("Dữ liệu khóa học không được để trống!");
        }

        if (course.getName() == null || course.getName().isBlank()) {
            throw new ValidationException("Tên khóa học không được để trống hoặc chỉ chứa khoảng trắng!");
        }

        if (course.getDuration() <= 0) {
            throw new ValidationException("Thời lượng khóa học phải là số nguyên dương lớn hơn 0!");
        }

        if (course.getInstructor() == null || course.getInstructor().isBlank()) {
            throw new ValidationException("Tên giảng viên không được để trống hoặc chỉ chứa khoảng trắng!");
        }

        if (courseDAO.existsByName(course.getName())) {
            throw new BusinessException("Tên khóa học '" + course.getName() + "' đã tồn tại trên hệ thống!");
        }

        courseDAO.save(course);
    }

    @Override
    public List<Course> getAllCourses() throws DatabaseException {
        return courseDAO.findAll();
    }
    @Override
    public Course getCourseById(Long id) throws ValidationException, BusinessException, DatabaseException {
        if (id == null || id <= 0) {
            throw new ValidationException("ID khóa học không hợp lệ!");
        }

        return courseDAO.findById(id)
                .orElseThrow(() -> new BusinessException("Không tìm thấy khóa học nào có ID là: " + id));
    }

    @Override
    public void updateCourse(Course course) throws ValidationException, BusinessException, DatabaseException {
        if (course == null || course.getId() == null || course.getId() <= 0) {
            throw new ValidationException("Dữ liệu cập nhật không hợp lệ!");
        }

        if (course.getName() == null || course.getName().isBlank()) {
            throw new ValidationException("Tên khóa học không được để trống hoặc chỉ chứa khoảng trắng!");
        }

        if (course.getDuration() <= 0) {
            throw new ValidationException("Thời lượng khóa học phải là số nguyên dương lớn hơn 0!");
        }

        if (course.getInstructor() == null || course.getInstructor().isBlank()) {
            throw new ValidationException("Tên giảng viên không được để trống hoặc chỉ chứa khoảng trắng!");
        }

        if (courseDAO.existsByNameAndIdNot(course.getName(), course.getId())) {
            throw new BusinessException("Tên khóa học '" + course.getName() + "' đã được sử dụng bởi một khóa học khác!");
        }

        courseDAO.update(course);
    }
    @Override
    public void deleteCourse(Long id) throws ValidationException, BusinessException, DatabaseException {
        if (id == null || id <= 0) {
            throw new ValidationException("ID khóa học cần xóa không hợp lệ!");
        }
        courseDAO.findById(id)
                .orElseThrow(() -> new BusinessException("Không thể xóa vì không tìm thấy khóa học nào có ID là: " + id));

        if (courseDAO.hasEnrollments(id)) {
            throw new BusinessException("Không thể xóa khóa học vì hiện tại đã có học viên đăng ký tham gia!");
        }

        courseDAO.deleteById(id);
    }

    @Override
    public List<Course> searchCoursesByName(String keyword) throws ValidationException, BusinessException, DatabaseException {
        if (keyword == null || keyword.isBlank()) {
            throw new ValidationException("Từ khóa tìm kiếm không được để trống hoặc chỉ chứa khoảng trắng!");
        }

        return courseDAO.findByNameContaining(keyword);
    }
    @Override
    public List<Course> getSortedCourses(int option) throws ValidationException, BusinessException, DatabaseException {
        String sortField;
        String direction;

        switch (option) {
            case 1:
                sortField = "id";
                direction = "ASC";
                break;
            case 2:
                sortField = "id";
                direction = "DESC";
                break;
            case 3:
                sortField = "name";
                direction = "ASC";
                break;
            case 4:
                sortField = "name";
                direction = "DESC";
                break;
            default:
                throw new ValidationException("Lựa chọn tiêu chí sắp xếp không hợp lệ!");
        }

        List<Course> sortedList = courseDAO.findAllSorted(sortField, direction);

        if (sortedList.isEmpty()) {
            throw new BusinessException("Danh sách khóa học hiện tại đang trống, không thể thực hiện sắp xếp!");
        }

        return sortedList;
    }
}