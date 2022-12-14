package ua.nure.rebrov.wholesale_base.model;

import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class User implements Comparable {
    protected String id;
    protected String name;
    protected String phone;
    protected String email;
    @BsonIgnore
    protected String password;
    protected String address;
    @BsonIgnore
    protected Base base;
    protected String type;
    protected List<GoodCategory> specialization;

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

    public static User admin() {
        return new User(
                "3",
                "???????????????? ??????????????",
                "+380958733276",
                "qwert@gmail.com",
                "$2a$10$mc9F.6kMA/aUxN08mFlBcuLUCwjiM1QhSN5afdS7r0MnilyBp9k.2",
                null,
                new Base(1, "???????????? ???????? ???1","???????????????? ??????????????, ????????"),
                UserType.Admin.name(),
                null
        );
    }

    public static User client() {
        return new User(
                "1",
                "?????????? ???????????????? ??????",
                "+380501237865",
                "sdaf@gmail.com",
                "$2a$10$ii3jGOjKRTR0sM.L3ZmrJubUDGCR32fqzaazIxS3FfQkoJ6a10n7S",
                "???????????????? ??????????????, ????????",
                null,
                UserType.Client.name(),
                null
        );
    }

    public static User distributor() {
        return new User(
                "4",
                "??????????",
                "+380958733123",
                "123@gmail.com",
                "$2a$10$5ExFuE.9W4iW4uOIF2Zs4enwt/YWC2dmWGsoHg9sItaDglpxXuLOK",
                "???????????????? ??????????????, ????????, ?????? ???????????????? 12",
                null,
                UserType.Distributor.name(),
                Arrays.asList(new GoodCategory(5,"????????????????", null),new GoodCategory(7,"?????????????? ??????????????", 4))
        );
    }

}
