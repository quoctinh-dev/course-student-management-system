package ra.cms.dao.impl;

import ra.cms.dao.ICourseDAO;
import ra.cms.exception.DatabaseException;
import ra.cms.model.Course;
import ra.cms.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements ICourseDAO {
    @Override
    public List<Course> findAll() throws DatabaseException {
        List<Course> courses = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "SELECT id, name, duration, instructor, created_at FROM courses";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getLong("id"));
                course.setName(resultSet.getString("name"));
                course.setDuration(resultSet.getInt("duration"));
                course.setInstructor(resultSet.getString("instructor"));

                Timestamp timestamp = resultSet.getTimestamp("created_at");
                if (timestamp != null) {
                    course.setCreatedAt(timestamp.toLocalDateTime());
                }

                courses.add(course);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Không thể truy cập lấy danh sách khóa học", e);
        } finally {
            DBUtil.closeResources(resultSet, preparedStatement, connection);
        }
        return courses;
    }

    @Override
    public void save(Course course) throws DatabaseException {
        String sql = "INSERT INTO Courses (name, duration, instructor) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, course.getName());
            pstmt.setInt(2, course.getDuration());
            pstmt.setString(3, course.getInstructor());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Thêm khóa học thất bại, không có hàng nào được thay đổi.");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                long generatedId = rs.getLong(1);
                course.setId(generatedId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi database khi thêm khóa học: " + e.getMessage(), e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
    }

    @Override
    public boolean existsByName(String name) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM Courses WHERE LOWER(name) = LOWER(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name.trim());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi database khi kiểm tra tên khóa học: " + e.getMessage(), e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
    }

    @Override
    public Optional<Course> findById(Long id) throws DatabaseException {
        String sql = "SELECT id, name, duration, instructor, created_at FROM courses WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Course course = new Course();
                course.setId(rs.getLong("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));

                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    course.setCreatedAt(timestamp.toLocalDateTime());
                }
                return Optional.of(course);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi database khi tìm kiếm khóa học theo ID: " + e.getMessage(), e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM courses WHERE LOWER(name) = LOWER(?) AND id != ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name.trim());
            pstmt.setLong(2, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi database khi kiểm tra trùng tên khóa học: " + e.getMessage(), e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
    }

    @Override
    public void update(Course course) throws DatabaseException {
        String sql = "UPDATE courses SET name = ?, duration = ?, instructor = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getName());
            pstmt.setInt(2, course.getDuration());
            pstmt.setString(3, course.getInstructor());
            pstmt.setLong(4, course.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Cập nhật khóa học thất bại, không tìm thấy khóa học để cập nhật.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi database khi cập nhật khóa học: " + e.getMessage(), e);
        } finally {
            DBUtil.closeResources(null, pstmt, conn);
        }
    }
    @Override
    public boolean hasEnrollments(Long courseId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, courseId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Gặp sự cố khi kiểm tra dữ liệu đăng ký của khóa học!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
    }

    @Override
    public void deleteById(Long id) throws DatabaseException {
        String sql = "DELETE FROM courses WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Xóa khóa học thất bại, không tìm thấy khóa học để xóa.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Gặp sự cố khi thực hiện xóa khóa học trong Database!", e);
        } finally {
            DBUtil.closeResources(null, pstmt, conn);
        }
    }
    @Override
    public List<Course> findByNameContaining(String keyword) throws DatabaseException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT id, name, duration, instructor, created_at FROM courses WHERE name ILIKE ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            String searchPattern = "%" + keyword.trim() + "%";
            pstmt.setString(1, searchPattern);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();

                course.setId(rs.getLong("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));

                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    course.setCreatedAt(timestamp.toLocalDateTime());
                }

                courses.add(course);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Gặp sự cố khi tìm kiếm khóa học theo tên!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return courses;
    }

    @Override
    public List<Course> findAllSorted(String sortField, String direction) throws DatabaseException {
        List<Course> courses = new ArrayList<>();
        // 1. Khởi tạo câu lệnh SQL động bằng phép cộng chuỗi tại mệnh đề ORDER BY
        String sql = "SELECT id, name, duration, instructor, created_at FROM courses ORDER BY "
                + sortField + " " + direction;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            // 2. GỘP CHUNG: Luồng lặp quét ResultSet và mapping trực tiếp tại đây
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getLong("id"));
                course.setName(rs.getString("name"));
                course.setDuration(rs.getInt("duration"));
                course.setInstructor(rs.getString("instructor"));

                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    course.setCreatedAt(timestamp.toLocalDateTime());
                }

                courses.add(course);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Gặp sự cố khi sắp xếp danh sách khóa học!", e);
        } finally {
            DBUtil.closeResources(rs, stmt, conn);
        }
        return courses;
    }
}