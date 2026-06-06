package ra.cms.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DBUtil {

    private static final String URL = "jdbc:postgresql://localhost:5434/cms_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "19052004";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy PostgreSQL Driver!", e);
        }
    }

    private DBUtil() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng ResultSet: " + e.getMessage()
            );
        }
    }

    public static void closePreparedStatement(
            PreparedStatement ps) {

        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng PreparedStatement: " + e.getMessage());
        }
    }

    public static void closeConnection(
            Connection connection) {

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println(
                    "Lỗi khi đóng Connection: " + e.getMessage()
            );
        }
    }

    public static void closeResources(ResultSet rs, PreparedStatement ps, Connection connection) {
        closeResultSet(rs);
        closePreparedStatement(ps);
        closeConnection(connection);
    }

}
