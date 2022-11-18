package ua.nure.rebrov.wholesale_base.dao.mongodb;

import com.mongodb.client.MongoCollection;
import ua.nure.rebrov.wholesale_base.dao.OrderDAO;
import ua.nure.rebrov.wholesale_base.model.Order;

import java.util.List;

public class MongoOrderDAO extends MongoDBschema implements OrderDAO {
    @Override
    public void create(Order order) {
        MongoCollection<Order> collection =  con.getCollection("order", Order.class);
        collection.insertOne(order);
    }

    @Override
    public void create(List<Order> orderList) {

    }
}
