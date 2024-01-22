package Dao.custom.Impl;

import Dao.Util.HibernateUtil;
import Dao.custom.ItemDao;
import entity.Item;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public class ItemDaoImpl implements  ItemDao {


    @Override
    public boolean save(Item entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();

        Transaction transaction = session.beginTransaction();
        Item item = session.find(Item.class, entity.getProduct_Name());
        item.setProduct_Category(entity.getProduct_Category());
        item.setProduct_Name(entity.getProduct_Name());
        item.setQTY_ON_Hand(entity.getQTY_ON_Hand());
        item.setProduct_Img(entity.getProduct_Img());
        session.save(item);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(session.find(Item.class,value));
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public List<Item> getAll() throws SQLException, ClassNotFoundException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Item> query = session.createQuery("FROM Item", Item.class);
            List<Item> list = query.getResultList();
            return list;
        } catch (HibernateException e) {
            // Handle Hibernate exceptions
            e.printStackTrace();
            throw e;
        }
    }

}
