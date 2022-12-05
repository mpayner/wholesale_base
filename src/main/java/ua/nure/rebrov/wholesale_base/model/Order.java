package ua.nure.rebrov.wholesale_base.model;

import com.google.gson.Gson;
import org.bson.*;
import org.bson.codecs.pojo.annotations.BsonExtraElements;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class Order implements BsonUtils{
    @BsonIgnore
    private String id;
    private User customer;
    private User distributor;
    @BsonIgnore
    private String waybill;
    @BsonRepresentation(BsonType.TIMESTAMP)
    private Timestamp createDate;
    private OrderStatus status;
    private String address;
    @BsonRepresentation(BsonType.TIMESTAMP)
    private Timestamp deliveryDate;
    @BsonIgnore
    private Map<Good, Integer> cart;

    private Order(Builder builder) {
        this.id = builder.id;
        this.customer = builder.customer;
        this.distributor = builder.distributor;
        this.waybill = builder.waybill;
        this.createDate = builder.createDate;
        this.status = builder.status;
        this.address = builder.address;
        this.deliveryDate = builder.deliveryDate;
        this.cart = builder.cart;
    }




    public static class Builder{
        private String id;
        private User customer;
        private User distributor;
        private String waybill;
        private Timestamp createDate;
        private OrderStatus status;
        private String address;
        private Timestamp deliveryDate;
        final private Map<Good, Integer> cart;

        public Builder(User customer, User distributor_id, Map<Good, Integer> cart) {
            this.customer = customer;
            this.distributor = distributor_id;
            this.cart = cart;
        }

        public Builder setId(String id){
            this.id=id;
            return this;
        }

        public Builder setWaybill(String waybill){
            this.waybill = waybill;
            return this;
        }

        public Builder setCreateDate(Timestamp createDate){
            this.createDate=createDate;
            return this;
        }

        public Builder setStatus(OrderStatus status){
            this.status = status;
            return this;
        }

        public Builder setCart(Good good, Integer quantity){
            this.cart.put(good, quantity);
            return this;
        }


        public Builder setAddress(String address){
            this.address = address;
            return this;
        }
        public Builder setDeliveryDate(Timestamp deliveryDate){
            this.deliveryDate = deliveryDate;
            return this;
        }

        public Order build(){
            return new Order(this);
        }
    }

    public Order() {}


    public Order(String id, User customer, User distributor, String waybill, Timestamp createDate, OrderStatus status, String address, Timestamp deliveryDate, Map<Good, Integer> cart) {
        this.id = id;
        this.customer = customer;
        this.distributor = distributor;
        this.waybill = waybill;
        this.createDate = createDate;
        this.status = status;
        this.address = address;
        this.deliveryDate = deliveryDate;
        this.cart = cart;
    }

    public Order(Document document){
        this.id = document.get("_id", ObjectId.class).toString();
        Document user = document.get("customer", Document.class);
        this.customer =  new User(
                user.getString("id"),
                user.getString("name"),
                user.getString("phone"),
                user.getString("email"),
                null, null, null, user.getString("type"),
                null
        );
        user = document.get("distributor", Document.class);
        this.distributor = new User(
                user.getString("id"),
                user.getString("name"),
                user.getString("phone"),
                user.getString("email"),
                null, null, null, user.getString("type"),
                null
        );
        this.waybill = document.get("waybill", String.class);
        this.createDate = new Timestamp(((Date)document.get("createDate")).getTime());
        this.status = OrderStatus.contains(document.get("status", String.class));
        this.address = document.get("address", String.class);
        Object tmp = document.get("deliveryDate");
        this.deliveryDate = tmp == null ? null : new Timestamp(((Date) tmp).getTime());
        List<Document> documentCart = document.getList("cart", Document.class);
        this.cart = new TreeMap<>();
        for(Document d: documentCart){
            Document good = d.get("good", Document.class);
            user = good.get("manufacturer", Document.class);
            Document category = good.get("category", Document.class);
            this.cart.put(
                    new Good(
                            good.getString("id"),
                            null,
                            new User(user.getString("id"),user.getString("name"), null, null, null, null, null, null, null),
                            good.getString("name"),
                            good.getString("description"),
                            (double)Math.round(good.getDouble("price")),
                            null,
                            new GoodCategory(category.getInteger("id"), category.getString("name"), null),
                            good.getString("unitType")
                    ),
                    d.getInteger("quantity")
            );
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getDistributor() {
        return distributor;
    }

    public void setDistributor(User distributor) {
        this.distributor = distributor;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Map<Good, Integer> getCart() {
        return cart;
    }

    public void setCart(Map<Good, Integer> cart) {
        this.cart = cart;
    }

    @Override
    public Document toDocument(){
        Document doc = new Document()
                .append("customer", customer)
                .append("distributor",distributor)
                .append("createDate", createDate)
                .append("deliveryDate", deliveryDate)
                .append("waybill", waybill);
        if(customer.getType().equals(UserType.Admin.toString())){
            doc.append("address", customer.getBase().getAddress());
        }else if(customer.getType().equals(UserType.Client.toString()) && customer.getAddress()!=null){
            doc.append("address", customer.getAddress());
        }else {
            doc.append("address", address);
        }
        doc.append("status", status.getTitle()).append("cart", cartToArrayOfDocuments());
        return doc;
    }

    public List<Document> cartToArrayOfDocuments(){
        List<Document> list = null;
        try {
            list = new LinkedList<>();
            for (Map.Entry entry : this.cart.entrySet()) {
                Good o = (Good)entry.getKey();
                list.add(new Document().append("good", o.toDocument()).append("quantity", entry.getValue()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static List<Order> groupOrders(Map<Good,Integer> cart, User customer){
        Stream<Good> goodStream = cart.keySet().stream();
        Map<String, List<Good>> groupedGoods = goodStream.collect(Collectors.groupingBy(g ->g.getManufacturer().getId()));
        System.out.println(groupedGoods);
        List<Order> orders = new LinkedList<>();
        for(Map.Entry entry: groupedGoods.entrySet()){
            Map<Good, Integer> c = new HashMap<>();
            User distributor = null;
            for(Good good : ((List<Good>) entry.getValue())){
                c.put(good, cart.get(good));
                System.out.println(good.getName());
                if(distributor==null){
                    distributor = good.getManufacturer();
                }
            }
            System.out.println();
            orders.add(new Order.Builder(customer, distributor, c).build());
        }
        return orders;
    }

    public double generalSum(){
        double sum = 0.0D;
        for (Map.Entry entry : this.cart.entrySet()) {
            Good o = (Good)entry.getKey();
            sum+= o.getPrice() * (int)entry.getValue();
        }
        return sum;
    }


}
