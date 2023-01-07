package ua.nure.rebrov.wholesale_base.dao.mysql;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import ua.nure.rebrov.wholesale_base.dao.OrderDAO;
import ua.nure.rebrov.wholesale_base.dao.mongodb.MongoOrderDAO;
import ua.nure.rebrov.wholesale_base.model.*;

import java.sql.*;
import java.util.*;
import java.util.Map.Entry;

public class MySQLOrderDAO extends MySQLschema implements OrderDAO {
    public MySQLOrderDAO() {
        super();
    }

    @Override
    public void create(Order order) {
        try {
            Savepoint savepoint = con.setSavepoint("save");
            con.setAutoCommit(false);
            try {
                PreparedStatement ps = con.prepareStatement("insert into `order`(customer_id, distributor_id, waybill, create_date, status_id, address, delivery_date) values (?,?,?,?,?,?,?)");
                ps.setInt(1, Integer.valueOf(order.getCustomer().getId()));
                ps.setInt(2, Integer.valueOf(order.getDistributor().getId()));
                FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
                ps.setString(3, fakeValuesService.numerify("59000########"));
                ps.setTimestamp(4,order.getCreateDate());
                ps.setInt(5,order.getStatus().ordinal()+1);
                ps.setString(6, order.getAddress());
                ps.setTimestamp(7, order.getDeliveryDate());
                ps.executeUpdate();
                ps = con.prepareStatement("select last_insert_id();");
                ResultSet rs = ps.executeQuery();
                rs.next();
                Integer id = rs.getInt(1);
                rs.close();
                ps = con.prepareStatement("insert into cart(order_id, good_id, quantity) values (?,?,?)");
                for(Entry entry: order.getCart().entrySet()){
                    Integer good_id = Integer.valueOf(((Good) entry.getKey()).getId());
                    Integer quantity = ((Integer) entry.getValue());
                    ps.setInt(1, id);
                    ps.setInt(2, good_id);
                    ps.setInt(3, quantity);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                con.rollback(savepoint);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void create(List<Order> orderList) {
        for(Order order : orderList){
            create(order);
        }
    }

    private List<Order> fromResultSet(ResultSet rs){
        List<Order> orders = new LinkedList<>();
        try{
            while (rs.next()) {
                Order o = new Order(
                        rs.getString("id"),
                        new User(rs.getString("c_id"), rs.getString("c_name"), rs.getString("c_phone"), rs.getString("c_email"), null, null, null, rs.getString("c_type"), null),
                        new User(rs.getString("d_id"), rs.getString("d_name"), rs.getString("d_phone"), rs.getString("d_email"), null, null, null, rs.getString("d_type"), null),
                        rs.getString("waybill"),
                        rs.getTimestamp("create_date"),
                        OrderStatus.contains(rs.getString("status_name")),
                        rs.getString("address"),
                        rs.getTimestamp("delivery_date"), null
                );
                PreparedStatement ps2 = null;
                if(o.getCustomer().getType().equals(UserType.Admin.name())){
                    ps2 = con.prepareStatement("select b.address from user u join base b on u.base_id = b.id where u.id = ?");
                    ps2.setInt(1, Integer.valueOf(o.getCustomer().getId()));
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    o.setAddress(rs2.getString(1));
                    ps2.close();
                }else if(o.getAddress()==null){
                    ps2 = con.prepareStatement("select address from user where id = ?");
                    ps2.setInt(1, Integer.valueOf(o.getCustomer().getId()));
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    o.setAddress(rs2.getString(1));
                    ps2.close();
                }
                ps2 = con.prepareStatement("select g.id, g.name,g.description, g.price, m.id man_id, m.name man_name, c.id cat_id, c.name cat_name,c.parent_id, u.name unit_name, crt.quantity from cart crt join good g join category c join unit_type u join user m on crt.good_id = g.id and g.manufacturer_id = m.id and g.category_id = c.id and g.unit_type_id = u.id where crt.order_id = ?");
                ps2.setInt(1, rs.getInt("id"));
                Map<Good, Integer> cart = new LinkedHashMap<>();
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {

                    cart.put(new Good(rs2.getString("id"),
                                    null,
                                    new User(
                                            rs2.getString("man_id"),
                                            rs2.getString("man_name"),
                                            null, null, null, null, null, null, null),
                                    rs2.getString("name"),
                                    rs2.getString("description"),
                                    rs2.getDouble("price"),
                                    null,
                                    new GoodCategory(rs2.getInt("cat_id"), rs2.getString("cat_name"), rs2.getInt("parent_id")),
                                    rs2.getString("unit_name")
                            ),
                            rs2.getInt("quantity")
                    );
                }
                o.setCart(cart);
                orders.add(o);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = null;
        try{
            PreparedStatement ps = con.prepareStatement("select o.id, c.id c_id, c.name c_name, c.phone_number c_phone, c.email c_email,cus_st.name c_type, d.id d_id, d.name d_name, d.phone_number d_phone, d.email d_email, dis_st.name d_type, o.waybill, o.create_date, s.name status_name, o.address, o.delivery_date from `order` o join user c join user d join user_type cus_st join user_type dis_st join order_status s on o.customer_id = c.id and o.distributor_id = d.id and o.status_id = s.id and c.type_id = cus_st.id and d.type_id = dis_st.id");
            orders = fromResultSet(ps.executeQuery());
        }catch(Exception e){
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findAll(int q) {
        List<Order> orders = null;
        try{
            PreparedStatement ps = con.prepareStatement("select o.id, c.id c_id, c.name c_name, c.phone_number c_phone, c.email c_email,cus_st.name c_type, d.id d_id, d.name d_name, d.phone_number d_phone, d.email d_email, dis_st.name d_type, o.waybill, o.create_date, s.name status_name, o.address, o.delivery_date from `order` o join user c join user d join user_type cus_st join user_type dis_st join order_status s on o.customer_id = c.id and o.distributor_id = d.id and o.status_id = s.id and c.type_id = cus_st.id and d.type_id = dis_st.id limit 1, ?");
            ps.setInt(1,q);
            orders = fromResultSet(ps.executeQuery());
        }catch(Exception e){
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findByCusAndDis(User customer, User distributor) {
        List<Order> orders = null;
        try{
            PreparedStatement ps = con.prepareStatement("select o.id, c.id c_id, c.name c_name, c.phone_number c_phone, c.email c_email, d.id d_id, d.name d_name, d.phone_number d_phone, d.email d_email, o.waybill, o.create_date, s.name status_name, o.address, o.delivery_date from `order` o join user c join user d join order_status s on o.customer_id = c.id and o.distributor_id = d.id and o.status_id = s.id where c.name like concat('%',?,'%') and d.name like concat('%',?,'%') limit 1,20");
            ps.setString(1, customer.getName());
            ps.setString(2, distributor.getName());
            orders = fromResultSet(ps.executeQuery());
        }catch(Exception e){
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> findByDistributor(User distributor) {
        return null;
    }

    @Override
    public List<Order> findByCustomer(User customer) {
        return null;
    }

    @Override
    public boolean updateStatus(Order order, String status) {
        return false;
    }

    @Override
    public boolean delete(Order order) {
        return false;
    }

    @Override
    public void migrate(Integer q) {
        List<Order> orderList = q==null ? findAll() : findAll(q);
        MongoOrderDAO dao = new MongoOrderDAO();
        dao.create(orderList);
    }
}
