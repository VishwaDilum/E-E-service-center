package Bo.custom.Impl;

import Bo.custom.LoginsBo;
import Dao.custom.Impl.LoginsDaoImpl;
import Dao.custom.LoginsDao;
import Dto.Logins;
import entity.logins;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginsBoImpl implements LoginsBo {

    LoginsDao loginsDao = new LoginsDaoImpl();
    @Override
    public List<logins> logins() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean registerUser(Dto.Logins dto) {
        logins logins = new logins(dto.getEmail(),dto.getPassword());
        return loginsDao.save(logins);
    }

    @Override
    public List<Logins> getAll() {
        List<logins> logins = loginsDao.allLogins();
        List<Logins> newLogin = new ArrayList<>();
        for (logins logins1 :logins) {
            Logins loginss = new Logins(
                    logins1.getEmail(),
                    logins1.getPassword()
            );
            newLogin.add(loginss);
        }
    return newLogin;
    }
}
