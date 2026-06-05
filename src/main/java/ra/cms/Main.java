package ra.cms;

import ra.cms.utils.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection= null;
        try {
             connection = DBUtil.getConnection();
            System.out.println("Kết nối thành công");
        }catch (SQLException e)
        {
            System.err.println("Đã có lỗi xảy ra!");
            e.printStackTrace();
        }
        finally {
            DBUtil.closeConnection(connection);
        }

    }
}
