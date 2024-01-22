package Dao.custom.Impl;

import Dao.Util.HibernateUtil;
import Dao.custom.CustomerDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import DBConnection.DBConnection;
import entity.Customer;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public Customer getLastOrder() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM customer ORDER BY Order_ID DESC LIMIT 1";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()){
            /*return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getDate(8),
                    resultSet.getString(7)
                    );*/
        }
        return null;
    }

//    @Override
//    public boolean save(String entity) throws SQLException, ClassNotFoundException {
//        Session session = HibernateUtil.getSession();
//        Transaction transaction = session.beginTransaction();
//        session.save(entity);
//        transaction.commit();
//        session.close();
//        return true;
//    }

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<Customer> getAll() throws SQLException, ClassNotFoundException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Customer> query = session.createQuery("FROM Customer ", Customer.class);
            List<Customer> list = query.getResultList();
            return list;
        } catch (HibernateException e) {
            // Handle Hibernate exceptions
            e.printStackTrace();
            throw e;
        }
    }
    public Customer getCustomerById(Long customerId) {
        try (Session session = HibernateUtil.getSession()) {
            // Retrieve the customer by ID
            return session.get(Customer.class, customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
