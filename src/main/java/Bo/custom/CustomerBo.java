package Bo.custom;

import Bo.SuperBo;
import Dto.CustomerDto;
import Dto.OrderDto;
import entity.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBo extends SuperBo {
    boolean placeOrder(Customer dto) throws SQLException, ClassNotFoundException;
    String generateOrderID();

    boolean save(CustomerDto customer) throws SQLException, ClassNotFoundException;

    List<Customer> allCustomers() throws SQLException, ClassNotFoundException;
}
