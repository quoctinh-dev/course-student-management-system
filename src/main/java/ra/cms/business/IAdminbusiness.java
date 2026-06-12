package ra.cms.business;

import ra.cms.exception.DatabaseException;
import ra.cms.exception.ValidationException;
import ra.cms.model.Admin;

import java.util.Optional;

public interface IAdminbusiness {

    // HÀM LOGIN CỦA ADMIN
    Optional<Admin> login(String username, String password) throws ValidationException, DatabaseException;
}
