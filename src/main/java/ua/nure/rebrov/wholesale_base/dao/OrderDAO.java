package ua.nure.rebrov.wholesale_base.dao;

import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.Order;
import ua.nure.rebrov.wholesale_base.model.User;

import java.util.List;
import java.util.Map;

public interface OrderDAO {
    void create(Order order);
    void create(List<Order> orderList);

    List<Order> findByDistributor(User distributor);
    List<Order> findByCustomer(User customer);

    boolean updateStatus(Order order, String status);

    boolean delete(Order order);
}
