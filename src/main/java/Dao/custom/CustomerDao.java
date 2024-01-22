package Dao.custom;

import Dao.CrudDao;
import entity.Customer;
import entity.Item;
import entity.Orderz;

import java.sql.SQLException;

public interface CustomerDao extends CrudDao<Customer> {
    Customer getLastOrder() throws SQLException, ClassNotFoundException;
    Customer getCustomerById(Long e);
}
