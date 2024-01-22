package Bo.custom;

import entity.logins;

import java.sql.SQLException;

public interface RestPasswordBo {
    boolean updatePassword(logins entity) throws SQLException, ClassNotFoundException;
}
