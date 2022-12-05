package ua.nure.rebrov.wholesale_base.model;


import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Good implements BsonUtils,Comparable{
    private String id;
    @BsonIgnore
    private User user;
    private User manufacturer;

    private String name;
    private String description;
    private Double price;
    @BsonIgnore
    private Integer quantity;
    private GoodCategory category;
    private String unitType;

    public Good(){
        this.category = new GoodCategory();
    }

    public Good(String id, User user, User manufacturer, String name, String description, Double price, Integer quantity, GoodCategory category, String unitType) {
        this.id = id;
        this.user = user;
        this.manufacturer = manufacturer;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.unitType = unitType;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(User manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public GoodCategory getCategory() {
        return category;
    }

    public void setCategory(GoodCategory category) {
        this.category = category;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    @Override
    public int compareTo(Object o) {
        return id.compareTo(((Good)o).id);
    }

    @Override
    public Document toDocument() {
        Document doc = new Document()
                .append("id", id)
                .append("name", name)
                .append("manufacturer", new Document("id", manufacturer.getId()).append("name", manufacturer.getName()))
                .append("price", price)
                .append("category", new Document("id", category.getIdCategory()).append("name", category.getName()))
                .append("unitType", unitType);
        return doc;
    }

    public static Good random(){
        Faker faker = new Faker(new Locale("uk-UA"));
        Good g = new Good();
        Random rnd = new Random();
        g.setId(String.valueOf(rnd.nextInt()));
        if(rnd.nextBoolean()){
            g.setUser(User.distributor());
            g.setQuantity(null);
        }else{
            g.setUser(User.admin());
            g.setQuantity(rnd.nextInt(100)+1);
        }
        g.setManufacturer(User.distributor());
        Commerce c = faker.commerce();
        g.setName(c.productName());
        g.setDescription(faker.bothify("?????#???#?"));
        g.setPrice(rnd.nextDouble(400)+30);
        g.setCategory(new GoodCategory(5,"Солодощі", null));
        g.setUnitType(UnitType.Box.toString());
        return g;
    }

    public static List<Good> randomList(int i){
        List<Good> randomList = new LinkedList<>();
        for (int j=0; j<i;j++){
            randomList.add(random());
        }
        return randomList;
    }
}
