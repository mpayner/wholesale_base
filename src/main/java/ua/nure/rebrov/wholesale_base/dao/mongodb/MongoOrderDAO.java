package ua.nure.rebrov.wholesale_base.dao.mongodb;

import com.mongodb.Cursor;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import ua.nure.rebrov.wholesale_base.dao.OrderDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLOrderDAO;
import ua.nure.rebrov.wholesale_base.model.Order;
import ua.nure.rebrov.wholesale_base.model.OrderStatus;
import ua.nure.rebrov.wholesale_base.model.User;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class MongoOrderDAO extends MongoDBschema implements OrderDAO {

    MongoCollection<Document> collection =  con.getCollection("order");

    @Override
    public void create(Order order) {
        collection.insertOne(order.toDocument());
    }

    @Override
    public void create(List<Order> orderList) {
        int errors = 0;
        for(Order o : orderList){
            try {
                create(o);
            } catch(Exception e){
                e.printStackTrace();
                if(errors>3){
                    break;
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                errors++;
            }
        }
    }

    private List<Order> find(Bson request) {
        MongoCursor<Document> f = collection.find(request).limit(20).cursor();
        List<Order> list = new LinkedList<>();
        while (f.hasNext()){
            list.add(new Order(f.next()));
        }
        return list;
    }

    @Override
    public List<Order> findByCusAndDis(User customer, User distributor) {

        Bson bFilter = Filters.and(
                Filters.regex("customer.name", ".*"+customer.getName()+".*", "i"),
                Filters.regex("distributor.name", ".*"+distributor.getName()+".*", "i")
        );
        System.out.println(bFilter);
        return find(bFilter);
    }

    @Override
    public List<Order> findAll() {
        MongoCursor<Document> f = collection.find().cursor();
        List<Order> list = new LinkedList<>();
        while (f.hasNext()){
            list.add(new Order(f.next()));
        }
        return list;
    }

    @Override
    public List<Order> findByDistributor(User distributor) {
        Bson bFilter = Filters.or(
                Filters.eq("customer.id",distributor.getId()),
                Filters.regex("distributor.name", ".*"+distributor.getName()+".*", "i")
        );
        return find(bFilter);
    }

    @Override
    public List<Order> findByCustomer(User customer) {
        Bson bFilter = Filters.or(
                Filters.eq("customer.id",customer.getId()),
                Filters.regex("customer.name", ".*"+customer.getName()+".*", "i")
        );
        return find(bFilter);
    }

    @Override
    public List<Order> findAll(int q) {
        MongoCursor<Document> f = collection.find().limit(q).cursor();
        List<Order> list = new LinkedList<>();
        while (f.hasNext()){
            list.add(new Order(f.next()));
        }
        return list;
    }

    @Override
    public boolean updateStatus(Order order, String status) {
        Bson updates = Updates.set("status", status);
        UpdateResult result = collection.updateOne(new Document("_id", new ObjectId(order.getId())), updates);
        return result.getModifiedCount()>0;
    }

    @Override
    public boolean delete(Order order) {
        DeleteResult result = collection.deleteOne(new Document("id", new ObjectId(order.getId())));
        return result.getDeletedCount()>0;
    }

    @Override
    public void migrate(Integer q) {
        List<Order> orderList = q==null ? findAll() : findAll(q);
        MySQLOrderDAO dao = new MySQLOrderDAO();
        dao.create(orderList);
    }
}
