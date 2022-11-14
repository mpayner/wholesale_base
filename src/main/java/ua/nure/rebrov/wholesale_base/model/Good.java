package ua.nure.rebrov.wholesale_base.model;


public class Good implements Comparable{
    private String id;
    private User user;
    private User manufacturer;

    private String name;
    private String description;
    private Double price;
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
}