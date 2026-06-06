package ra.cms.dao.impl;

import ra.cms.dao.IStudentDao;
import ra.cms.exception.DatabaseException;
import ra.cms.model.Student;
import ra.cms.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StudentDAOImpl implements IStudentDao {

    @Override
    public Optional<Student> findByEmail(String email) {
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
}