package Dao.custom.Impl;

import Dao.Util.HibernateUtil;
import Dao.custom.RestPasswordDao;
import entity.Item;
import entity.logins;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public class RestPasswordDaoImpl implements RestPasswordDao {

    @Override
    public boolean save(logins entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(logins entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.update(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<logins> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }
}
