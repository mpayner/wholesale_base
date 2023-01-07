package ua.nure.rebrov.wholesale_base.dao.mongodb.aggregation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import ua.nure.rebrov.wholesale_base.dao.mongodb.MongoDBschema;

import java.sql.Timestamp;
import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Filters.*;
public class MongoAggregation extends MongoDBschema implements AggregationMethods{
    @Override
    public void groupByCustomerAndCount() {

        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        List<Document> documents =
                orders.aggregate(List.of(
                        project(and(eq("user", new Document("customer_id", "$customer.id").append("customer_name", "$customer.name")))),
                        group("$user",sum("quantityOfOrder",1))))
                        .into(new ArrayList<>());
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time with aggregation framework: " + time + "\n");
        for(Document doc: documents){
            System.out.println(doc.toJson());
        }


    }

    @Override
    public void getGoodByMinOrders() {
        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        Document document = orders.aggregate(List.of(
                unwind("$cart"), project(and(
                        eq("name", "$cart.good.name"),
                        new Document("order_price", new Document("$round", Arrays.asList(new Document("$multiply", Arrays.asList("$cart.good.price", "$cart.quantity")), 0)))
                )),
                group("$name", sum("order_amount", "$order_price")),
                Aggregates.sort(new Document("order_amount",1))
                )
        ).first();
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time with aggregation framework: " + time + "\n");
        System.out.println(document.toJson());
    }

    @Override
    public void getExecutedOrdersWithDelivery() {
        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        List<Document> documents = orders.aggregate(List.of(
                Aggregates.match(and(eq("status","Виконано"), ne("deliveryDate",null)))
        )).into(new ArrayList<>());
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time with aggregation framework: " + time + "\n");
        for(Document doc: documents){
            System.out.println(doc.toJson());
        }
    }

    @Override
    public void getOrderAndPrice(int quantity) {
        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        List<Document> documents = orders.aggregate(List.of(
                project(and(
                        new Document("total_goods", new Document("$sum", "$cart.quantity")),
                        eq("customerName", "$customer.name"),
                        eq("distributorName", "$distributor.name"),
                        eq("cart", "$cart")

                )),Aggregates.match(and(gte("total_goods", quantity)))
        )).into(new ArrayList<>());
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time with aggregation framework: " + time + "\n");
        for(Document doc: documents){
            System.out.println(doc.toJson());
        }
    }

    @Override
    public void getOrderWithLongDelivery() {
        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        Document ord = orders.aggregate(List.of(
                match((ne("deliveryDate",null))),
                project(eq("deliveryTime", new Document("$subtract", Arrays.asList("$deliveryDate", "$createDate")))),
                Aggregates.sort(new Document("deliveryDate", -1))
        )).first();
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time with aggregation framework: " + time + "\n");
        System.out.println("_id: "+ord.get("_id") + "\ndeliveryTime: "+new Timestamp(ord.getLong("deliveryTime")).toLocalDateTime().getHour() + " hours");

    }
}
