package ua.nure.rebrov.wholesale_base.model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Base {
    private Integer id;
    private String name;
    private String address;

    public Base(Integer id,String name,String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
