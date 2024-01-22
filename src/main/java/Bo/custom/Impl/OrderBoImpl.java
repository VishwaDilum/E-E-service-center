package Bo.custom.Impl;

import Bo.custom.OrderBo;
import Dao.custom.Impl.OrderDaoImpl;
import Dao.custom.OrderDao;
import Dto.OrderDto;
import Dto.addPartDto;
import Dto.updateStatusDto;
import entity.Orderz;

import java.sql.SQLException;
import java.util.List;

public class OrderBoImpl implements OrderBo {
    OrderDao orderDao = new OrderDaoImpl();
    @Override
    public boolean placeOrder(Orderz order) throws SQLException, ClassNotFoundException {
        return orderDao.save(order);
    }

    @Override
    public boolean save(OrderDto orderDto) throws SQLException, ClassNotFoundException {
        Orderz orderz = new Orderz();
        orderz.setOrder_ID(orderDto.getOrder_ID());
        orderz.setCustomer_Name(orderDto.getCustomer_Name());
        orderz.setOrder_Date(orderDto.getOrderDate());
        orderz.setItem_Category(orderDto.getItem_Category());
        orderz.setOrder_Status(orderDto.getOrder_Status());
        orderz.setCustomer(orderDto.getCustomer());
        orderz.setItem_Name(orderDto.getItem_Name());
        orderz.setOrder_Total(orderDto.getOrder_Total());
         return orderDao.save(orderz);
    }

    @Override
    public List<Orderz> allOrders() throws SQLException, ClassNotFoundException {
        return orderDao.getAll();
    }

    @Override
    public boolean saveAddItems(addPartDto addPartDto) {
        //Additional_Item additionalItem = new Additional_Item(addPartDto.getOrder_ID(),addPartDto.getPart_Price(),addPartDto.getPart_Name());
        //return orderDao.saveAddItem(additionalItem);
        return false;
    }

    @Override
    public boolean deleteCustomer(String orderId) throws SQLException, ClassNotFoundException {
        return orderDao.delete(orderId);
    }

    @Override
    public Orderz getOrder(String id) throws SQLException, ClassNotFoundException {
        List<Orderz> all = orderDao.getAll();

        for (Orderz order : all) {
            if (order.getOrder_ID().equals(id)) {
                return order;
            }
        }

        // Return null or throw an exception if the order with the specified ID is not found
        return null;
    }

    @Override
    public boolean updateStatus(updateStatusDto selectedItem) throws SQLException, ClassNotFoundException {
        return orderDao.updateStatus(selectedItem);
    }

}
