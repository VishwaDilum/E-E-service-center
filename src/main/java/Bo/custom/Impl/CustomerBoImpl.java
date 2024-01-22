package Bo.custom.Impl;

import Bo.custom.CustomerBo;
import Dao.custom.CustomerDao;
import Dao.custom.Impl.CustomerDaoImpl;
import Dao.custom.Impl.OrderDaoImpl;
import Dao.custom.OrderDao;
import Dto.CustomerDto;
import entity.Customer;
import entity.Orderz;

import java.sql.SQLException;
import java.util.List;

public class CustomerBoImpl implements CustomerBo {
    CustomerDao CustomerDao = new CustomerDaoImpl();
    OrderDao orderDao = new OrderDaoImpl();

    @Override
    public boolean placeOrder(Customer dto) throws SQLException, ClassNotFoundException {
        return CustomerDao.save(dto);
    }

    @Override
    public String generateOrderID() {
        try {
            Orderz dto = orderDao.getLastOrder();
            if (dto!=null){
                String id = dto.getOrder_ID();
                int num = Integer.parseInt(id.split("[D]")[1]);
                num++;
                return String.format("D%03d",num);
            }else{
                return "D001";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean save(CustomerDto dto) throws SQLException, ClassNotFoundException {
        Customer customer = new Customer(dto.getCustomer_Name(),dto.getCustomer_Email(),dto.getCustomer_Number());
        return CustomerDao.save(customer);
    }

    @Override
    public List<Customer> allCustomers() throws SQLException, ClassNotFoundException {
        return CustomerDao.getAll();
    }
}
