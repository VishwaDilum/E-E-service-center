package Dao.custom.Impl;

import Dao.Util.HibernateUtil;
import Dao.custom.LoginsDao;
import Dto.Logins;
import entity.logins;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class LoginsDaoImpl implements LoginsDao {
    @Override
    public List<logins> allLogins() {
        Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM logins ");
        List<logins> list = query.list();
        session.close();
        return list;
    }

    @Override
    public boolean save(entity.logins entity) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }
}
