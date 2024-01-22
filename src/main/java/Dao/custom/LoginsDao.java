package Dao.custom;

import Dto.Logins;
import entity.logins;

import java.util.List;

public interface LoginsDao {
    List<logins> allLogins();

    boolean save(entity.logins logins);
}
