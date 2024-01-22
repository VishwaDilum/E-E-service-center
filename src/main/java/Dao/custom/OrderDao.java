package Dao.custom;

import Dao.CrudDao;
import Dto.tm.orderTm;
import Dto.updateStatusDto;
import entity.Additional_Item;
import entity.Orderz;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao extends CrudDao<Orderz> {
    Orderz getLastOrder() throws SQLException, ClassNotFoundException;

    default List<orderTm> getAllTableItem() throws SQLException, ClassNotFoundException {
        return null;
    }

    boolean saveAddItem(Additional_Item additionalItem);

    boolean updateStatus(updateStatusDto selectedItem);
}
