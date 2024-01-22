package Dao;

import Dao.Util.DaoType;
import Dao.custom.Impl.CustomerDaoImpl;
import Dao.custom.Impl.ItemDaoImpl;
import Dao.custom.Impl.OrderDaoImpl;

import static Dao.Util.BoType.CUSTOMER;

public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory(){

    }
    public static DaoFactory getInstance(){
        return daoFactory!=null? daoFactory:(daoFactory=new DaoFactory());
    }

    public <T extends SuperDao>T getDao(DaoType type){
        switch (type){
            case CUSTOMER: return(T) new CustomerDaoImpl();
            case ORDER: return(T) new OrderDaoImpl();
            case ITEM: return(T) new ItemDaoImpl();
        }
        return null;
    }
}
