package Bo.custom.Impl;

import Bo.custom.RestPasswordBo;
import Dao.custom.Impl.RestPasswordDaoImpl;
import Dao.custom.RestPasswordDao;
import entity.logins;

import java.sql.SQLException;

public class RestPasswordBoImpl implements RestPasswordBo {
    RestPasswordDao restPasswordDao = new RestPasswordDaoImpl();
    @Override
    public boolean updatePassword(logins entity) throws SQLException, ClassNotFoundException {

        return restPasswordDao.update(entity);
    }
}
