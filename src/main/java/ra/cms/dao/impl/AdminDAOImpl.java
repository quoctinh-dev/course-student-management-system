package ra.cms.dao.impl;

import ra.cms.dao.IAdminDao;
import ra.cms.exception.DatabaseException;
import ra.cms.model.Admin;
import ra.cms.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminDAOImpl implements IAdminDao {

    // TÌM KIẾM THEO TÊN CỦA ADMIN
    @Override
    public Optional<Admin> findByUsername(String username) throws DatabaseException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql = "SELECT id, username, password FROM Admins WHERE username = ?";

        try {

            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Admin admin = new Admin();
                admin.setId(resultSet.getLong("id"));
                admin.setUsername(resultSet.getString("username"));
                admin.setPassword(resultSet.getString("password"));
                return Optional.of(admin);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi hệ thống: Gặp sự cố khi truy vấn tài khoản Admin trong Database!", e);
        } finally {
            DBUtil.closeResources(resultSet, preparedStatement, connection);
        }
        return Optional.empty();
    }
}