package ua.nure.rebrov.wholesale_base.dao;

import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.Order;

import java.util.List;
import java.util.Map;

public interface OrderDAO {
    void create(Order order);
    void create(List<Order> orderList);

}
