package ra.cms.dao.impl;

import ra.cms.dao.IEnrollmentDAO;
import ra.cms.exception.DatabaseException;
import ra.cms.model.Course;
import ra.cms.model.Enrollment;
import ra.cms.model.EnrollmentStatus;
import ra.cms.model.Student;
import ra.cms.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentDAOImpl implements IEnrollmentDAO {
    @Override
    public boolean existByStudentAndCourse(Long studentId, Long courseId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql = "SELECT COUNT(id) FROM enrollments WHERE student_id = ? AND course_id = ? AND status != 'CANCELED'::status_enum";

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, studentId);
            preparedStatement.setLong(2, courseId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi: Không thể kiểm tra trùng lặp đơn đăng ký khóa học", e);
        } finally {
            DBUtil.closeResources(resultSet, preparedStatement, connection);
        }
        return false;
    }

    @Override
    public void register(Long studentId, Long courseId) throws DatabaseException {
        String sql = "INSERT INTO enrollments(student_id, course_id, status) VALUES (?, ?, 'WAITING'::status_enum)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, studentId);
            pstmt.setLong(2, courseId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hạ tầng: Gửi đơn đăng ký khóa học thất bại!", e);
        } finally {
            DBUtil.closeResources(null, pstmt, conn);
        }
    }

    @Override
    public List<Enrollment> getHistoryByStudent(Long studentId, int sortOption) throws DatabaseException {
        List<Enrollment> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT e.id, e.student_id, e.course_id, e.registered_at, e.status, c.name AS course_name " +
                        "FROM enrollments e " +
                        "JOIN courses c ON e.course_id = c.id " +
                        "WHERE e.student_id = ? "
        );

        switch (sortOption) {
            case 1:
                sql.append("ORDER BY c.name ASC");
                break;
            case 2:
                sql.append("ORDER BY c.name DESC");
                break;
            case 3:
                sql.append("ORDER BY e.registered_at ASC");
                break;
            case 4:
                sql.append("ORDER BY e.registered_at DESC");
                break;
            default:
                sql.append("ORDER BY e.registered_at DESC");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setLong(1, studentId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setId(rs.getLong("id"));
                e.setRegisteredAt(rs.getTimestamp("registered_at").toLocalDateTime());
                e.setStatus(EnrollmentStatus.valueOf(rs.getString("status")));

                Student stu = new Student();
                stu.setId(rs.getLong("student_id"));
                e.setStudent(stu);

                Course cou = new Course();
                cou.setId(rs.getLong("course_id"));
                cou.setName(rs.getString("course_name"));
                e.setCourse(cou);

                list.add(e);
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Lỗi hạ tầng: Không thể tải lịch sử đăng ký của học viên!", ex);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return list;
    }

    @Override
    public Optional<Enrollment> findById(Long enrollmentId) throws DatabaseException {
        String sql = "SELECT e.id, e.status, e.student_id, c.name AS course_name " +
                "FROM enrollments e " +
                "JOIN courses c ON e.course_id = c.id WHERE e.id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, enrollmentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Enrollment e = new Enrollment();
                e.setId(rs.getLong("id"));
                e.setStatus(EnrollmentStatus.valueOf(rs.getString("status")));

                Student s = new Student();
                s.setId(rs.getLong("student_id"));
                e.setStudent(s);

                Course c = new Course();
                c.setName(rs.getString("course_name"));
                e.setCourse(c);

                return Optional.of(e);
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Lỗi truy vấn đơn đăng ký số #" + enrollmentId, ex);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long enrollmentId) throws DatabaseException {
        String sql = "DELETE FROM enrollments WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, enrollmentId);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            throw new DatabaseException("Lỗi hệ thống: Không thể xóa đơn đăng ký số #" + enrollmentId, ex);
        } finally {
            DBUtil.closeResources(null, pstmt, conn);
        }
    }

    @Override
    public List<Enrollment> findByCourseId(Long courseId) throws DatabaseException {
        List<Enrollment> list = new java.util.ArrayList<>();
        String sql = "SELECT e.id, e.status, e.registered_at, " +
                "s.id AS student_id, s.name AS student_name, s.email, s.phone " +
                "FROM enrollments e " +
                "JOIN students s ON e.student_id = s.id " +
                "WHERE e.course_id = ? " +
                "ORDER BY e.registered_at DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, courseId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setId(rs.getLong("id"));

                String statusStr = rs.getString("status");
                if (statusStr != null) {
                    e.setStatus(EnrollmentStatus.valueOf(statusStr));
                }

                Timestamp ts = rs.getTimestamp("registered_at");
                if (ts != null) {
                    e.setRegisteredAt(ts.toLocalDateTime());
                }

                Student s = new Student();
                s.setId(rs.getLong("student_id"));
                s.setName(rs.getString("student_name"));
                s.setEmail(rs.getString("email"));
                s.setPhone(rs.getString("phone"));
                e.setStudent(s);

                list.add(e);
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Lỗi hệ thống: Không thể tải danh sách sinh viên của khóa học #" + courseId, ex);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return list;
    }

    @Override
    public void updateStatus(Long enrollmentId, EnrollmentStatus status) throws DatabaseException {
        String sql = "UPDATE enrollments SET status = ?::status_enum WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());
            pstmt.setLong(2, enrollmentId);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Lỗi hệ thống: Không thể cập nhật trạng thái đơn đăng ký #" + enrollmentId, ex);
        } finally {
            DBUtil.closeResources(null, pstmt, conn);
        }
    }
}