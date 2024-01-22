package Bo.custom;

import Dto.OrderDto;
import Dto.addPartDto;
import Dto.updateStatusDto;
import entity.Orderz;

import java.sql.SQLException;
import java.util.List;

public interface OrderBo {
    boolean placeOrder(Orderz order) throws SQLException, ClassNotFoundException;

    boolean save(OrderDto orderDto) throws SQLException, ClassNotFoundException;

    List<Orderz> allOrders() throws SQLException, ClassNotFoundException;

    boolean saveAddItems(addPartDto addPartDto);

    boolean deleteCustomer(String orderId) throws SQLException, ClassNotFoundException;

    Orderz getOrder(String id) throws SQLException, ClassNotFoundException;

    boolean updateStatus(updateStatusDto selectedItem) throws SQLException, ClassNotFoundException;
}
