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
public class Order {
    @BsonIgnore
    private String id;
    private User customer;
    private User distributor;
    @BsonIgnore
    private String waybill;
    @BsonRepresentation(BsonType.TIMESTAMP)
    private Timestamp createDate;
    private String status;
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
        private String status;
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

        public Builder setStatus(String status){
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


    public Order(String id, User customer, User distributor, String waybill, Timestamp createDate, String status, String address, Timestamp deliveryDate, Map<Good, Integer> cart) {
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
        Gson gson = new Gson();
        this.customer = gson.fromJson(((Document)document.get("customer")).toJson(), User.class);
        this.distributor = gson.fromJson(((Document)document.get("distributor")).toJson(), User.class);
        this.waybill = document.get("waybill", String.class);
        this.createDate = new Timestamp(((Date)document.get("createDate")).getTime());
        this.status = document.get("status", String.class);
        this.address = document.get("address", String.class);
        Object tmp = document.get("deliveryDate");
        this.deliveryDate = tmp == null ? null : new Timestamp(((Date) tmp).getTime());
        List<Document> documentCart = document.getList("cart", Document.class);
        this.cart = new TreeMap<>();
        for(Document d: documentCart){
            this.cart.put(gson.fromJson(((Document)d.get("good")).toJson(), Good.class), d.get("quantity", Integer.class));
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public Document toDocument(){
        return new Document()
                .append("customer", customer)
                .append("distributor",distributor)
                .append("createDate", createDate)
                .append("deliveryDate", deliveryDate)
                .append("address", address)
                .append("status", status)
                .append("cart", cartToArrayOfDocuments());
    }

    public List<Document> cartToArrayOfDocuments(){
        List<Document> list = null;
        try {
            list = new LinkedList<>();
            for (Map.Entry entry : this.cart.entrySet()) {
                Object o = entry.getKey();
                list.add(new Document().append("good", o).append("quantity", entry.getValue()));
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
}
