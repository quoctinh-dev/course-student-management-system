    package ra.cms.dao;

    import ra.cms.exception.DatabaseException;
    import ra.cms.model.Admin;
    import ra.cms.model.Student;

    import java.sql.SQLException;
    import java.util.Optional;

    public interface IAdminDao  {
        // HÀM TÌM KIẾM TÊN CỦA ADMIN
        Optional<Admin> findByUsername(String username) throws DatabaseException;
    }
