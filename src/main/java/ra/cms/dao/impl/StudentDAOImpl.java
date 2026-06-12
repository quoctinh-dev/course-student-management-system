package ra.cms.dao.impl;

import ra.cms.dao.IStudentDao;
import ra.cms.dto.CourseStatisticDTO;
import ra.cms.exception.DatabaseException;
import ra.cms.model.Student;
import ra.cms.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.*;

public class StudentDAOImpl implements IStudentDao {

    // THỰC THI CHỨC NĂNG TÌM KIẾM EMAIL TRẢ VỀ ĐỐI TƯỢNG
    @Override
    public Optional<Student> findByEmail(String email) throws DatabaseException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM Students WHERE email = ?";

        try {
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getLong("id"));
                student.setName(resultSet.getString("name"));

                if (resultSet.getDate("dob") != null) {
                    student.setDob(resultSet.getDate("dob").toLocalDate());
                }

                student.setEmail(resultSet.getString("email"));
                student.setSex(resultSet.getBoolean("sex"));
                student.setPhone(resultSet.getString("phone"));
                student.setPassword(resultSet.getString("password"));

                if (resultSet.getTimestamp("created_at") != null) {
                    student.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                }

                return Optional.of(student);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Gặp sự cố khi truy vấn tài khoản Học viên trong Database!", e);
        } finally {
            DBUtil.closeResources(resultSet, preparedStatement, connection);
        }
        return Optional.empty();
    }


    @Override
    public List<Student> findAll() throws DatabaseException {
        List<Student> students = new ArrayList<>();
        // Lấy toàn bộ các trường khớp 100% với cấu trúc bảng thực tế
        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM Students";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setPassword(rs.getString("password"));

                Date dobDate = rs.getDate("dob");
                if (dobDate != null) {
                    student.setDob(dobDate.toLocalDate());
                }

                Timestamp createdAtTs = rs.getTimestamp("created_at");
                if (createdAtTs != null) {
                    student.setCreatedAt(createdAtTs.toLocalDateTime());
                }

                students.add(student);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể tải danh sách học viên!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return students;
    }
    @Override
    public boolean existsByEmail(String email) throws DatabaseException {
        String sql = "SELECT COUNT(id) FROM Students WHERE email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống khi kiểm tra email tồn tại!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return false;
    }

    @Override
    public void create(Student student) throws DatabaseException {
        String sql = "INSERT INTO Students(name, dob, email, sex, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student.getName());
            pstmt.setDate(2, java.sql.Date.valueOf(student.getDob())); // Chuyển LocalDate sang java.sql.Date
            pstmt.setString(3, student.getEmail());
            pstmt.setBoolean(4, student.isSex());
            pstmt.setString(5, student.getPhone());
            pstmt.setString(6, student.getPassword()); // Mật khẩu đã băm sẵn từ Business

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể thêm mới học viên!", e);
        } finally {
            DBUtil.closeResources(null, pstmt, conn);
        }
    }
    @Override
    public Student getById(Long id) throws DatabaseException {
        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM Students WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setDob(rs.getDate("dob").toLocalDate());
                student.setEmail(rs.getString("email"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setPassword(rs.getString("password"));
                student.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return student;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống khi tìm kiếm học viên theo ID!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return null;
    }

    @Override
    public boolean existsByEmailForUpdate(String email, Long currentId) throws DatabaseException {
        String sql = "SELECT COUNT(id) FROM Students WHERE email = ? AND id != ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setLong(2, currentId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống khi kiểm tra trùng lặp email cập nhật!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return false;
    }

    @Override
    public void update(Student student) throws DatabaseException {
        String sql = "UPDATE Students SET name = ?, dob = ?, email = ?, sex = ?, phone = ?, password = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student.getName());
            pstmt.setDate(2, java.sql.Date.valueOf(student.getDob()));
            pstmt.setString(3, student.getEmail());
            pstmt.setBoolean(4, student.isSex());
            pstmt.setString(5, student.getPhone());
            pstmt.setString(6, student.getPassword());
            pstmt.setLong(7, student.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể cập nhật thông tin học viên!", e);
        } finally {
            DBUtil.closeResources(null, pstmt, conn);
        }
    }
    @Override
    public void delete(Long id) throws DatabaseException {
        String sql = "DELETE FROM Students WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                throw new DatabaseException("Không thể xóa học viên này vì dữ liệu học viên đang liên kết với danh sách đăng ký học!", e);
            }
            throw new DatabaseException("Lỗi hệ thống: Không thể xóa học viên khỏi cơ sở dữ liệu!", e);
        } finally {
            DBUtil.closeResources(null, pstmt, conn);
        }
    }
    @Override
    public List<Student> search(String keyword) throws DatabaseException {
        List<Student> students = new ArrayList<>();

        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM Students " +
                "WHERE name ILIKE ? OR email ILIKE ? OR CAST(id AS VARCHAR) LIKE ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            String searchPattern = "%" + keyword.trim() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setDob(rs.getDate("dob").toLocalDate());
                student.setEmail(rs.getString("email"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setPassword(rs.getString("password"));
                student.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                students.add(student);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống khi thực hiện tìm kiếm học viên đa năng!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return students;
    }
    @Override
    public List<Student> getSortedStudents(int option) throws DatabaseException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM Students ORDER BY ";

        switch (option) {
            case 1: sql += "id ASC"; break;
            case 2: sql += "id DESC"; break;
            case 3: sql += "name ASC"; break;
            case 4: sql += "name DESC"; break;
            default: sql += "id ASC";
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setDob(rs.getDate("dob").toLocalDate());
                student.setEmail(rs.getString("email"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setPassword(rs.getString("password"));
                student.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                students.add(student);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể sắp xếp danh sách học viên!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return students;
    }

    @Override
    public void updatePassword(Long studentId, String newPassword) throws DatabaseException {
        String sql = "UPDATE students SET password = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setLong(2, studentId);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Lỗi hệ thống: Không thể cập nhật mật khẩu mới cho học viên!", ex);
        } finally {
            DBUtil.closeResources(null, pstmt, conn);
        }
    }

    // PHÂN TRANG NÂNG CAO
    @Override
    public List<Student> findWithPaginationAndSort(int page, int size, int sortOption) throws DatabaseException {
        List<Student> students = new ArrayList<>();
        int offset = (page - 1) * size;

        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM Students ORDER BY ";
        switch (sortOption) {
            case 1: sql += "id ASC"; break;
            case 2: sql += "id DESC"; break;
            case 3: sql += "name ASC"; break;
            case 4: sql += "name DESC"; break;
            default: sql += "id ASC";
        }
        sql += " LIMIT ? OFFSET ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setPassword(rs.getString("password"));

               Date dobDate = rs.getDate("dob");
                if (dobDate != null) {
                    student.setDob(dobDate.toLocalDate());
                }

               Timestamp createdAtTs = rs.getTimestamp("created_at");
                if (createdAtTs != null) {
                    student.setCreatedAt(createdAtTs.toLocalDateTime());
                }
                students.add(student);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể tải danh sách học viên phân trang!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return students;
    }

    @Override
    public int countAll() throws DatabaseException {
        String sql = "SELECT COUNT(id) FROM Students";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống khi đếm tổng số lượng học viên!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return 0;
    }

    @Override
    public List<Student> searchWithPagination(String keyword, int page, int size) throws DatabaseException {
        List<Student> students = new ArrayList<>();
        int offset = (page - 1) * size;

        String sql = "SELECT id, name, dob, email, sex, phone, password, created_at FROM Students " +
                "WHERE name ILIKE ? OR email ILIKE ? OR CAST(id AS VARCHAR) LIKE ? " +
                "ORDER BY id ASC LIMIT ? OFFSET ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            String searchPattern = "%" + keyword.trim() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setInt(4, size);
            pstmt.setInt(5, offset);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setSex(rs.getBoolean("sex"));
                student.setPhone(rs.getString("phone"));
                student.setPassword(rs.getString("password"));

               Date dobDate = rs.getDate("dob");
                if (dobDate != null) {
                    student.setDob(dobDate.toLocalDate());
                }

                Timestamp createdAtTs = rs.getTimestamp("created_at");
                if (createdAtTs != null) {
                    student.setCreatedAt(createdAtTs.toLocalDateTime());
                }
                students.add(student);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống khi tìm kiếm học viên phân trang!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return students;
    }

    @Override
    public int countSearch(String keyword) throws DatabaseException {
        String sql = "SELECT COUNT(id) FROM Students WHERE name ILIKE ? OR email ILIKE ? OR CAST(id AS VARCHAR) LIKE ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            String searchPattern = "%" + keyword.trim() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống khi đếm kết quả tìm kiếm học viên!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return 0;
    }

    // ĐỀ XUẤT KHÓA HỌC NÂNG CAO
    @Override
    public List<CourseStatisticDTO> getRecommendedCoursesWithPagination(long studentId, int page, int size) throws DatabaseException {
        List<CourseStatisticDTO> list = new ArrayList<>();
        String sql = "SELECT c.id, c.name, COUNT(e2.student_id) AS student_count " +
                "FROM enrollments e1 " +
                "JOIN enrollments e2 ON e1.course_id = e2.course_id AND e2.status = 'CONFIRM' " +
                "JOIN enrollments e3 ON e2.student_id = e3.student_id AND e3.status = 'CONFIRM' " +
                "JOIN courses c ON e3.course_id = c.id " +
                "WHERE e1.student_id = ? AND e1.status = 'CONFIRM' " +
                "AND e3.course_id NOT IN (SELECT course_id FROM enrollments WHERE student_id = ? AND status = 'CONFIRM') " +
                "GROUP BY c.id, c.name " +
                "ORDER BY student_count DESC " +
                "LIMIT ? OFFSET ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            int offset = (page - 1) * size;
            pstmt.setLong(1, studentId);
            pstmt.setLong(2, studentId);
            pstmt.setInt(3, size);
            pstmt.setInt(4, offset);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                CourseStatisticDTO dto = new CourseStatisticDTO();
                dto.setCourseId(rs.getLong("id"));
                dto.setCourseName(rs.getString("name"));
                dto.setStudentCount(rs.getInt("student_count"));
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể phân trang dữ liệu đề xuất khóa học!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return list;
    }

    @Override
    public int countTotalRecommendedCourses(long studentId) throws DatabaseException {
        String sql = "SELECT COUNT(*) AS total FROM (" +
                "SELECT c.id FROM enrollments e1 " +
                "JOIN enrollments e2 ON e1.course_id = e2.course_id AND e2.status = 'CONFIRM' " +
                "JOIN enrollments e3 ON e2.student_id = e3.student_id AND e3.status = 'CONFIRM' " +
                "JOIN courses c ON e3.course_id = c.id " +
                "WHERE e1.student_id = ? AND e1.status = 'CONFIRM' " +
                "AND e3.course_id NOT IN (SELECT course_id FROM enrollments WHERE student_id = ? AND status = 'CONFIRM') " +
                "GROUP BY c.id" +
                ") AS temp_table";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, studentId);
            pstmt.setLong(2, studentId);
            rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể đếm tổng số khóa học đề xuất!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return 0;
    }
}