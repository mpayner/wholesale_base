package ua.nure.rebrov.wholesale_base.dao.mysql;

import ua.nure.rebrov.wholesale_base.dao.GoodDAO;
import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.GoodCategory;
import ua.nure.rebrov.wholesale_base.model.UnitType;
import ua.nure.rebrov.wholesale_base.model.User;
import ua.nure.rebrov.wholesale_base.patterns.ConsoleLogObserver;
import ua.nure.rebrov.wholesale_base.patterns.Observed;
import ua.nure.rebrov.wholesale_base.patterns.Observer;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MySQLGoodDAO extends MySQLschema implements GoodDAO, Observed<Good> {

    List<Observer> subscribers;
    public MySQLGoodDAO(){
        super();
        subscribers = Arrays.asList(new ConsoleLogObserver());
    }


    @Override
    public List<Good> getAll() {
        List <Good> list = new LinkedList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select g.id, g.user_id, g.manufacturer_id, g.name, g.description, g.price, g.quantity, g.category_id,c.name category_name, c.parent_id, g.unit_type_id from good g join category c on g.category_id = c.id limit 300");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Good good = new Good();
                good.setId(rs.getString("id"));
                MySQLUserDAO user = new MySQLUserDAO();
                good.setUser(user.getById(rs.getString("user_id")));
                good.setManufacturer(user.getById(rs.getString("manufacturer_id")));
                good.setName(rs.getString("name"));
                good.setDescription(rs.getString("description"));
                good.setPrice(rs.getDouble("price"));
                good.setQuantity(rs.getInt("quantity"));
                good.setCategory(new GoodCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getInt("parent_id")
                ));
                String unit = rs.getInt("unit_type_id") == 1 ? UnitType.Box.toString() : UnitType.Pack.toString();
                good.setUnitType(unit);
                list.add(good);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public List<Good> getPriceList(User distributor) {
        List<Good> priceList = getAll();
        priceList.removeIf(good -> !good.getManufacturer().getId().equals(distributor.getId()) && !good.getUser().getId().equals(distributor.getId()));
        return priceList;
    }

    @Override
    public List<Good> getBaseGoods() {
        List<Good> baseGoods = getAll();
        baseGoods.removeIf(good -> good.getManufacturer() != good.getUser());
        return baseGoods;
    }

    @Override
    public Good getById(String id) {
        Good good = null;
        try {
            PreparedStatement ps = con.prepareStatement("select g.id, g.user_id, g.manufacturer_id, g.name, g.description, g.price, g.quantity, g.category_id,c.name category_name, c.parent_id, g.unit_type_id from good g join category c on g.category_id = c.id where g.id =?");
            ps.setInt(1, Integer.valueOf(id));
            ResultSet rs = ps.executeQuery();
            rs.next();
            if(!rs.wasNull()) {
                good = new Good();
                good.setId(rs.getString("id"));
                MySQLUserDAO user = new MySQLUserDAO();
                good.setUser(user.getById(rs.getString("user_id")));
                good.setManufacturer(user.getById(rs.getString("manufacturer_id")));
                good.setName(rs.getString("name"));
                good.setDescription(rs.getString("description"));
                good.setPrice(rs.getDouble("price"));
                good.setQuantity(rs.getInt("quantity"));
                good.setCategory(new GoodCategory(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getInt("parent_id")
                ));
                String unit = rs.getInt("unit_type_id") == 1 ? UnitType.Box.toString() : UnitType.Pack.toString();
                good.setUnitType(unit);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return good;
    }

    @Override
    public Integer getQuantity(Good good) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("select quantity from good where id =?");
            ps.setInt(1, Integer.valueOf(good.getId()));
            ResultSet rs = ps.executeQuery();
            rs.next();
            Integer q = rs.getInt(1);
            if(rs.wasNull()){
                return null;
            }else {
                return q;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean increaseQuantity(Good good, Integer quantity) {
        Integer check = getQuantity(good);
        if(check==null){
            return true;
        }else {
            return updateQuantity(good, check + quantity);
        }
    }

    @Override
    public boolean decreaseQuantity(Good good, Integer quantity) {
        Integer check = getQuantity(good);
        if(check==null){
            return true;
        }
        int q = check -quantity;

        try {
            if(q<0){
                throw new Exception("?????????????????? ???????????????? ???????? ?????????????????? ????????????, ???????????????? ???????? ?????????????????? ???????????? ???? ????????????");
            }else {
                return updateQuantity(good, q);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateQuantity(Good good, Integer quantity) {
        Savepoint save = null;
        try{
            con.setAutoCommit(false);
            save = con.setSavepoint("save");
            PreparedStatement ps = con.prepareStatement("UPDATE good SET `quantity` = ? where id=?;");
            ps.setInt(1, quantity);
            ps.setInt(2, Integer.valueOf(good.getId()));
            ps.executeUpdate();
            con.commit();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            try {
                con.rollback(save);
            } catch (SQLException ex) {
                e.printStackTrace();
            }
        }
        return false;
    }


    @Override
    public boolean deleteById(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from good where id=?");
            ps.setInt(1,Integer.valueOf(id));
            ps.executeUpdate();
            return true;
        }
        catch(SQLException e){
           e.printStackTrace();
           return false;
        }
    }
    @Override
    public void add(Good good) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into good(user_id, name, description, price, quantity, category_id, unit_type_id, manufacturer_id) values (?,?,?,?,?,?,?,?)");
            ps.setInt(1, Integer.valueOf(good.getUser().getId()));
            ps.setString(2, good.getName());
            ps.setString(3, good.getDescription());
            ps.setDouble(4, good.getPrice());
            if(good.getQuantity()!=null){
                ps.setInt(5,good.getQuantity());
            }else{
                ps.setNull(5, Types.INTEGER);
            }
            ps.setInt(6, good.getCategory().getIdCategory());
            if(UnitType.Box.toString().equals(good.getUnitType())){
                ps.setInt(7, UnitType.Box.ordinal()+1);
            }else {
                ps.setInt(7, UnitType.Pack.ordinal()+1);
            }
            ps.setInt(8, Integer.valueOf(good.getManufacturer().getId()));
            ps.executeUpdate();
            ps.close();
            // NOTIFY
            notifyObservers(good, "??????????");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(List<Good> good) {
        for(Good g:good){
            add(g);
        }
    }

    @Override
    public void update(Good good) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE good SET `name`=?,description=?,price=?,quantity=?,category_id=?,unit_type_id=? where id=?;");
            ps.setString(1, good.getName());
            ps.setString(2, good.getDescription());
            ps.setDouble(3, good.getPrice());
            if(good.getQuantity()!=null){
                ps.setInt(4,good.getQuantity());
            }else{
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, good.getCategory().getIdCategory());
            if(UnitType.Box.toString().equals(good.getUnitType())){
                ps.setInt(6, UnitType.Box.ordinal()+1);
            }else {
                ps.setInt(6, UnitType.Pack.ordinal()+1);
            }
            ps.setInt(7, Integer.valueOf(good.getId()));
            ps.executeUpdate();
            notifyObservers(good, "????????????");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addObserver(Observer observer) {

    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyObservers(Good good, String action) {
        User user = good.getUser();
        String message = user.getType() +" "+ user.getName()
                + " " +action+" ??????????:"+
        "#"+good.getId()+" = "+good.getName()+"; ";
        for(Observer observer : subscribers){
            observer.notify(message);
        }
    }

    @Override
    public void notifyObservers(List<Good> good, String action) {
        User user = good.get(0).getUser();
        String message = user.getType() + user.getName()
                + " " +action+" ???????? ????????????:";
        for(Good g : good){
            message +="#"+g.getId()+" = "+g.getName()+"; ";
        }
        for(Observer observer : subscribers){
            observer.notify(message);
        }
    }
}
