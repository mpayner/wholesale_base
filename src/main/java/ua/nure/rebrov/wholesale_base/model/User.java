package ua.nure.rebrov.wholesale_base.model;

import org.bson.codecs.pojo.annotations.BsonId;

import java.util.List;
import java.util.Objects;

public class User implements Comparable{
    private String id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String address;
    private Base base;
    private String type;
    private List<GoodCategory> specialization;

    public User(){}
    public User(String id, String name, String phone, String email, String password, String address, Base base, String type, List<GoodCategory> specialization) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.base = base;
        this.type = type;
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<GoodCategory> getSpecialization() {
        return specialization;
    }

    public void setSpecialization(List<GoodCategory> specialization) {
        this.specialization = specialization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Base getBase() {
        return base;
    }

    public void setBase(Base base) {
        this.base = base;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Object o) {
        return id.compareTo(((User) o).id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(phone, user.phone) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && address.equals(user.address) && base.equals(user.base) && type.equals(user.type) && specialization.equals(user.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, email, password, address, base, type, specialization);
    }
}
