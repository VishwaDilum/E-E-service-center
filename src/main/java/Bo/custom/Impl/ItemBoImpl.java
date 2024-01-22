package Bo.custom.Impl;

import Bo.custom.ItemBo;
import Dao.Util.HibernateUtil;
import Dao.custom.Impl.ItemDaoImpl;
import Dao.custom.ItemDao;
import Dto.ItemDto;
import entity.Customer;
import entity.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.List;

public class ItemBoImpl implements ItemBo {
    ItemDao itemDao = new ItemDaoImpl();

    @Override
    public boolean save(Item entity) throws SQLException, ClassNotFoundException {
        return itemDao.save(entity);
        /*Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();*/
        //return true;
    }

    @Override
    public List<Item> allCustomers() throws SQLException, ClassNotFoundException {
        return  itemDao.getAll();
        /*Session session = HibernateUtil.getSession();
        Query query = session.createQuery("FROM Item");
        List<Item> list = query.list();
        session.close();
        return list;*/
    }

    @Override
    public boolean deleteCustomer(String value) throws SQLException, ClassNotFoundException {
        return itemDao.delete(value);
        /*Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(session.find(Item.class,value));
        transaction.commit();
        session.close();
        return true;*/
    }

    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        return itemDao.update(item);
    }
}
