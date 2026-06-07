    package ra.cms.dao;

    import ra.cms.exception.DatabaseException;
    import ra.cms.model.Admin;
    import ra.cms.model.Student;

    import java.sql.SQLException;
    import java.util.Optional;

    public interface IAdminDao  {
        Optional<Admin> findByUsername(String username) throws DatabaseException;
    }
