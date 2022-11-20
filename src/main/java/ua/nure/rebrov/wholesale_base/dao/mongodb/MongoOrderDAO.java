package ua.nure.rebrov.wholesale_base.dao.mongodb;

import com.mongodb.Cursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import ua.nure.rebrov.wholesale_base.dao.OrderDAO;
import ua.nure.rebrov.wholesale_base.model.Order;
import ua.nure.rebrov.wholesale_base.model.OrderStatus;
import ua.nure.rebrov.wholesale_base.model.User;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class MongoOrderDAO extends MongoDBschema implements OrderDAO {


    @Override
    public void create(Order order) {
        MongoCollection collection =  con.getCollection("order");
        order.setCreateDate(new Timestamp(System.currentTimeMillis()));
        order.setStatus(OrderStatus.Created.toString());
        collection.insertOne(order.toDocument());
    }

    @Override
    public void create(List<Order> orderList) {
        for(Order o : orderList){
            create(o);
        }
    }



    private List<Order> find(Document request) {
        MongoCollection<Document> collection =  con.getCollection("order");
        MongoCursor<Document> f = collection.find(request).cursor();
        List<Order> list = new LinkedList<>();
        while (f.hasNext()){
            list.add(new Order(f.next()));
        }
        return list;
    }

    @Override
    public List<Order> findByDistributor(User distributor) {
        return find(new Document("distributor", distributor));
    }

    @Override
    public List<Order> findByCustomer(User customer) {
        return find(new Document("customer", customer));
    }

    @Override
    public boolean updateStatus(Order order, String status) {
        MongoCollection collection = con.getCollection("order");
        UpdateResult result = collection.updateOne(new Document("_id", new ObjectId(order.getId())), new Document("status", status));
        return result.getModifiedCount()>0;
    }

    @Override
    public boolean delete(Order order) {
        return false;
    }
}
