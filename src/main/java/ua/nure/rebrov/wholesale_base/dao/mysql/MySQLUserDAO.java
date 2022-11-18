package ua.nure.rebrov.wholesale_base.dao.mysql;

import ua.nure.rebrov.wholesale_base.dao.UserDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLConnector;
import ua.nure.rebrov.wholesale_base.model.Base;
import ua.nure.rebrov.wholesale_base.model.GoodCategory;
import ua.nure.rebrov.wholesale_base.model.User;
import ua.nure.rebrov.wholesale_base.model.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static ua.nure.rebrov.wholesale_base.model.UserType.*;

public class MySQLUserDAO extends MySQLschema implements UserDAO {

    @Override
    public List<User> getAll() {
        List<User> list = new LinkedList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select u.id, u.name, u.email, u.password, u.phone_number, u.type_id, u.address, u.base_id,  b.name base_name,  b.address base_address from user u left join base b on u.base_id = b.id");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setName(rs.getString("name"));
                user.setId(rs.getString("id"));
                user.setPhone(rs.getString("phone_number"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                int type_id = rs.getInt("type_id");
                switch (type_id) {
                    case 1: {
                        user.setType(UserType.Admin.toString());
                        user.setBase(new Base(rs.getInt("base_id"), rs.getString("base_name"), rs.getString("base_address")));
                        break;
                    }
                    case 2: {
                        user.setType(UserType.Distributor.toString());
                        ps = con.prepareStatement("select c.id, c.name, c.parent_id from distributor_specialization d join category c on d.category_id = c.id where d.user_id =?");
                        ps.setInt(1, Integer.valueOf(user.getId()));
                        ResultSet rs2 = ps.executeQuery();
                        List<GoodCategory> categoryList = new LinkedList<>();
                        while (rs2.next()) {
                            categoryList.add(new GoodCategory(
                                    rs2.getInt("id"),
                                    rs2.getString("name"),
                                    rs2.getInt("parent_id")
                            ));
                        }
                        user.setSpecialization(categoryList);
                        break;
                    }
                    case 3: {
                        user.setType(UserType.Client.toString());
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + type_id);
                }
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<User> getDistributors() {
        List<User> distributors = getAll();
        distributors.removeIf(user -> !user.getType().equals(Distributor.toString()));
        return distributors;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public User getById(String id) {
        User user = null;
        List<User> list = getAll();
        for(User u : list){
            if(u.getId().equals(id)){
                user = u;
                break;
            }
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = null;
        List<User> list = getAll();
        for(User u : list){
            if(u.getEmail().equals(email)){
                user = u;
                break;
            }
        }
        return user;
    }

    @Override
    public Boolean deleteById(String id) {
        return null;
    }
}
