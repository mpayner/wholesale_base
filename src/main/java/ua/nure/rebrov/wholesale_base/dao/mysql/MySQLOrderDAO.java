package ua.nure.rebrov.wholesale_base.dao.mysql;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MongoDBDAO;
import ua.nure.rebrov.wholesale_base.dao.OrderDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLConnector;
import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.Order;

import java.sql.*;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

public class MySQLOrderDAO extends MySQLschema implements OrderDAO {

    @Override
    public void create(Order order) {
        try {
            Savepoint savepoint = con.setSavepoint("save");
            con.setAutoCommit(false);
            try {
                PreparedStatement ps = con.prepareStatement("insert into `order`(customer_id, distributor_id, waybill) values (?,?,?)");
                ps.setInt(1, Integer.valueOf(order.getCustomer().getId()));
                ps.setInt(2, Integer.valueOf(order.getDistributor().getId()));
                FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
                ps.setString(3, fakeValuesService.numerify("59000########"));
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
                //
                DAOFactory dao = new MongoDBDAO();
                dao.createOrderDAO().create(order);
                //
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
}
