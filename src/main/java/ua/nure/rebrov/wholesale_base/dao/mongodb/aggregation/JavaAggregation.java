package ua.nure.rebrov.wholesale_base.dao.mongodb.aggregation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import ua.nure.rebrov.wholesale_base.dao.mongodb.MongoDBschema;

import java.sql.Timestamp;
import java.util.*;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class JavaAggregation extends MongoDBschema implements AggregationMethods{
    @Override
    public void groupByCustomerAndCount() {

        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        List<Document> customers = new LinkedList<>();
        List<Document> documents =
                orders.distinct("customer", Document.class).into(new ArrayList<>());
        documents.forEach(document -> {
            customers.add(
                    new Document("customer_id", document.get("id")).
                            append("customer_name", document.get("name")).
                            append("quantityOfOrder", orders.countDocuments(new Document("customer",document)))
            );
        });
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time without aggregation framework: " + time + "\n");
        for(Document doc: customers){
            System.out.println(doc.toJson());
        }
    }

    @Override
    public void getGoodByMinOrders() {
        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        List<String> goods= orders.distinct("cart.good.id",String.class).into(new ArrayList<>());
        Map<Document, Double> document = new HashMap<>();
        goods.forEach((good)->{
            Bson projection = fields(Filters.elemMatch("cart", Filters.eq("good.id", good)));
            MongoCursor<Document> doc = orders.find(new Document("cart.good.id", good)).projection(projection).cursor();
            double sum = 0.0;
            Document cur = null;
            while (doc.hasNext()){
                cur = doc.next().getList("cart", Document.class).get(0);
                sum += Math.round(cur.getInteger("quantity") * cur.get("good",Document.class).getDouble("price"));
            }
            document.put(cur,sum);
        });
        Map.Entry<Document, Double> maxEntry = document.entrySet().stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .orElse(null);
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time without aggregation framework: " + time + "\n");
        System.out.println(maxEntry.getKey().get("good", Document.class).toJson()+ "\n\"orderAmount\": "+ maxEntry.getValue() +"}");


    }

    @Override
    public void getExecutedOrdersWithDelivery() {
        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        Bson filter = and(ne("deliveryDate",null), eq("status", "Виконано"));
        List<Document> documents = orders.find(filter).into(new ArrayList<>());
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time without aggregation framework: " + time + "\n");
        for(Document doc: documents){
            System.out.println(doc.toJson());
        }
    }

    @Override
    public void getOrderAndPrice(int quantity) {
        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        List<Document> ord = new LinkedList<>();

        Bson projection = fields( Filters.and(
                new Document("total_goods", new Document("$sum", "$cart.quantity")),
                Filters.eq("customerName", "$customer.name"),
                Filters.eq("distributorName", "$distributor.name"),
                Filters.eq("cart", "$cart")
        ));
        List<Document> documents = orders.find().projection(projection).into(new ArrayList<>());
        documents.forEach((document) -> {
            if(document.getInteger("total_goods")>quantity){
                ord.add(document);
            }
        });
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time without aggregation framework: " + time + "\n");
        for(Document doc: ord){
            System.out.println(doc.toJson());
        }
    }

    @Override
    public void getOrderWithLongDelivery() {
        MongoCollection<Document> orders = con.getCollection("order");
        long start = System.currentTimeMillis();
        Bson projection = fields(eq("deliveryTime", new Document("$subtract", Arrays.asList("$deliveryDate", "$createDate"))));
        Bson match = Filters.ne("deliveryDate",null);
        Document ord = orders.find(match).projection(projection).sort(new Document("deliveryDate", -1)).first();
        long end = System.currentTimeMillis();
        double time = (end-start)/1000D;
        System.out.println("Time without aggregation framework: " + time + "\n");
        System.out.println("_id: "+ord.get("_id") + "\ndeliveryTime: "+new Timestamp(ord.getLong("deliveryTime")).toLocalDateTime().getHour() + " hours");


    }
}
