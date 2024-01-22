package Bo.custom;

import Bo.SuperBo;
import Dao.CrudDao;
import Dao.custom.ItemDao;
import Dto.ItemDto;
import entity.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemBo extends SuperBo {
    boolean save(Item item) throws SQLException, ClassNotFoundException;

    List<Item> allCustomers() throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(String name) throws SQLException, ClassNotFoundException;
    boolean update(Item item) throws SQLException, ClassNotFoundException;
}
