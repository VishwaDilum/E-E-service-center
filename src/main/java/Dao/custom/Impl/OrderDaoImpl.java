package Dao.custom.Impl;

import DBConnection.DBConnection;
import Dao.Util.HibernateUtil;
import Dao.custom.OrderDao;
import Dto.tm.orderTm;
import Dto.updateStatusDto;
import com.jfoenix.controls.JFXButton;
import entity.Additional_Item;
import entity.Item;
import entity.Orderz;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class OrderDaoImpl implements OrderDao {
    @Override
    public boolean save(Orderz entity) throws SQLException, ClassNotFoundException {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(entity);
        transaction.commit();
        session.close();
        return true;

    }

    @Override
    public boolean update(Orderz entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM orderz\n" +
                "WHERE Order_ID = ?";
        try (PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            pstm.setString(1, value);
            int rowsAffected = pstm.executeUpdate();
            return rowsAffected > 0;
        }
    }


    @Override
    public List<Orderz> getAll() throws SQLException, ClassNotFoundException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Orderz> query = session.createQuery("FROM Orderz", Orderz.class);
            List<Orderz> list = query.getResultList();
            return list;
        } catch (HibernateException e) {
            // Handle Hibernate exceptions
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Orderz getLastOrder() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Orderz ORDER BY Order_ID DESC LIMIT 1";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            return new Orderz(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDate(6),
                    resultSet.getDouble(8),
                    resultSet.getString(5),
                    resultSet.getString(3),
                    resultSet.getString(7),
                    resultSet.getString(4),
                    null
            );
        }
        return null;
    }

    public List<orderTm> getAllTableItem() throws SQLException, ClassNotFoundException {
            JFXButton btn = new JFXButton();
            List<orderTm> list = new ArrayList<>();
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("SELECT Orderz.Order_ID, Customer.Customer_Name, Customer.Customer_Number, Customer.Customer_Email, Orderz.Item_Description, Orderz.Order_Status FROM Orderz Orderz LEFT JOIN Customer customer ON Orderz.Customer_id = customer.Customer_Name");
            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                list.add(new orderTm(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        btn
                ));
            }
            return list;

    }

    @Override
    public boolean saveAddItem(Additional_Item entity) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = session.beginTransaction();
        Additional_Item additionalItem = new Additional_Item();
        additionalItem.setOrderID(entity.getOrderID());
        additionalItem.setCost_of_Part(entity.getCost_of_Part());
        additionalItem.setAdditional_item(entity.getAdditional_item());
        session.save(additionalItem);
        transaction.commit();
        session.close();
        return true;
    }

    @Override
    public boolean updateStatus(updateStatusDto selectedItem) {
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Orderz orderz = session.find(Orderz.class, selectedItem.getOrder_ID());

            if (orderz != null) {
                orderz.setOrder_Status(selectedItem.getStatus());
                session.update(orderz);

                transaction.commit(); // Commit the transaction after successful update
                System.out.println(selectedItem.getOrder_ID() + " " + selectedItem.getStatus());
                return true;
            } else {
                System.err.println("Order with ID " + selectedItem.getOrder_ID() + " not found.");
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback the transaction in case of an exception
            }
            e.printStackTrace(); // Log or handle the exception as needed
            return false;
        } finally {
            session.close(); // Always close the session to avoid resource leaks
        }
    }



}
