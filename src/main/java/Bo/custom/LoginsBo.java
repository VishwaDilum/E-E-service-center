package Bo.custom;

import Bo.SuperBo;
import Dto.Logins;
import entity.logins;

import java.sql.SQLException;
import java.util.List;

public interface LoginsBo extends SuperBo {
    List<logins> logins() throws SQLException, ClassNotFoundException;
    boolean registerUser(Dto.Logins dto);

    List<Logins> getAll();
}
