package ra.cms.business;

import ra.cms.model.Student;
import java.util.Optional;

public interface IStudentBusiness {
    Optional<Student> login(String email, String password);
}