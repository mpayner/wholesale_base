package ua.nure.rebrov.wholesale_base.patterns;

import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.model.Order;

import java.util.List;

public class DAOTest {
    public DAOFactory daoFactory;

    public DAOTest(DAOFactory dao){
        this.daoFactory = dao;
    }

    public void test(){
        List<Order> orderList = daoFactory.createOrderDAO().findAll(10);
        orderList.forEach(order -> {
            System.out.println(order);
        });
    }

    public void setDAO(DAOFactory dao){
        this.daoFactory = dao;
    }
}
