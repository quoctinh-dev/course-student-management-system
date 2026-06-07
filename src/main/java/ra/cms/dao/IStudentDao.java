package ra.cms.dao;

import ra.cms.exception.DatabaseException;
import ra.cms.model.Student;
import java.util.Optional;

public interface IStudentDao {
    Optional<Student> findByEmail(String email) throws DatabaseException;
}