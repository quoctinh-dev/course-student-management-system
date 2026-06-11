package ra.cms.dao.impl;

import ra.cms.dao.IStatisticDAO;
import ra.cms.dto.CourseStatisticDTO;
import ra.cms.exception.DatabaseException;
import ra.cms.utils.DBUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatisticDAOImpl implements IStatisticDAO {

    @Override
    public Map<String, Integer> getGeneralOverview() throws DatabaseException {
        Map<String, Integer> resultMap = new LinkedHashMap<>();

        String sqlCourses = "SELECT COUNT(id) AS total_courses FROM courses";

        String sqlStudents = "SELECT COUNT(DISTINCT student_id) AS total_students FROM enrollments WHERE status = 'CONFIRM'";

        Connection conn = null;
        PreparedStatement pstmtCourse = null;
        PreparedStatement pstmtStudent = null;
        ResultSet rsCourse = null;
        ResultSet rsStudent = null;

        try {
            conn = DBUtil.getConnection();

            pstmtCourse = conn.prepareStatement(sqlCourses);
            rsCourse = pstmtCourse.executeQuery();
            if (rsCourse.next()) {
                resultMap.put("totalCourses", rsCourse.getInt("total_courses"));
            }

            pstmtStudent = conn.prepareStatement(sqlStudents);
            rsStudent = pstmtStudent.executeQuery();
            if (rsStudent.next()) {
                resultMap.put("totalStudents", rsStudent.getInt("total_students"));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể truy xuất dữ liệu tổng quan thống kê!", e);
        } finally {
            DBUtil.closeResources(rsCourse, pstmtCourse, null);
            DBUtil.closeResources(rsStudent, pstmtStudent, conn);
        }
        return resultMap;
    }

    @Override
    public List<CourseStatisticDTO> getStudentCountByCourse() throws DatabaseException {
        List<CourseStatisticDTO> list = new ArrayList<>();

        String sql = "SELECT c.id, c.name, COUNT(CASE WHEN e.status = 'CONFIRM' THEN 1 END) AS student_count " +
                "FROM courses c " +
                "LEFT JOIN enrollments e ON c.id = e.course_id " +
                "GROUP BY c.id, c.name " +
                "ORDER BY c.name ASC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CourseStatisticDTO dto = new CourseStatisticDTO();
                dto.setCourseId(rs.getLong("id"));
                dto.setCourseName(rs.getString("name"));
                dto.setStudentCount(rs.getInt("student_count"));
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể tính toán số lượng học viên theo từng khóa!", e);
        } finally {

            DBUtil.closeResources(rs, pstmt, conn);
        }
        return list;
    }

    @Override
    public List<CourseStatisticDTO> getTop5HotCourses() throws DatabaseException {
        List<CourseStatisticDTO> list = new ArrayList<>();

        String sql = "SELECT c.id, c.name, COUNT(CASE WHEN e.status = 'CONFIRM' THEN 1 END) AS student_count " +
                "FROM courses c " +
                "LEFT JOIN enrollments e ON c.id = e.course_id " +
                "GROUP BY c.id, c.name " +
                "ORDER BY student_count DESC, c.name ASC " +
                "LIMIT 5";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CourseStatisticDTO dto = new CourseStatisticDTO();
                dto.setCourseId(rs.getLong("id"));
                dto.setCourseName(rs.getString("name"));
                dto.setStudentCount(rs.getInt("student_count"));
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể lấy danh sách top 5 khóa học đông học viên!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return list;
    }

    @Override
    public List<CourseStatisticDTO> getCrowdedCourses() throws DatabaseException {
        List<CourseStatisticDTO> list = new ArrayList<>();

        String sql = "SELECT c.id, c.name, COUNT(CASE WHEN e.status = 'CONFIRM' THEN 1 END) AS student_count " +
                "FROM courses c " +
                "LEFT JOIN enrollments e ON c.id = e.course_id " +
                "GROUP BY c.id, c.name " +
                "HAVING COUNT(CASE WHEN e.status = 'CONFIRM' THEN 1 END) > 10 " +
                "ORDER BY student_count DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CourseStatisticDTO dto = new CourseStatisticDTO();
                dto.setCourseId(rs.getLong("id"));
                dto.setCourseName(rs.getString("name"));
                dto.setStudentCount(rs.getInt("student_count"));
                list.add(dto);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Không thể lọc danh sách khóa học trên 10 học viên!", e);
        } finally {
            DBUtil.closeResources(rs, pstmt, conn);
        }
        return list;
    }

}