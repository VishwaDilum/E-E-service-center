package Bo;

import Bo.custom.Impl.CustomerBoImpl;
import Bo.custom.Impl.LoginsBoImpl;
import Bo.custom.Impl.OrderBoImpl;
import Dao.Util.BoType;
import Dao.custom.Impl.ItemDaoImpl;

public class BoFactory {
    private static BoFactory boFactory;
    private BoFactory(){

    }
    public static BoFactory getInstance(){
        return boFactory!=null? boFactory:(boFactory=new BoFactory());
    }

    public <T extends SuperBo>T getBo(BoType type){
        switch (type){
            case CUSTOMER: return (T) new CustomerBoImpl();
            case ORDER: return (T) new OrderBoImpl();
            case ITEM: return (T) new ItemDaoImpl();
        }
        return null;
    }
}
