package ua.nure.rebrov.wholesale_base;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.github.javafaker.Faker;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MongoDBDAO;
import ua.nure.rebrov.wholesale_base.dao.MySQLDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLConnector;
import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.Order;
import ua.nure.rebrov.wholesale_base.model.OrderStatus;
import ua.nure.rebrov.wholesale_base.model.User;

import java.sql.Timestamp;
import java.util.*;

public class Test {
    public static void main(String[] args){
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);

        /*MySQLDAO dao = new MySQLDAO();
        List<Good> goodList = dao.createGoodDAO().getAll();
        System.out.println("--Товари створені--");
        Random rnd = new Random();
        List<Order> orderList = new LinkedList<>();
        Faker faker = new Faker();
        int k = 50000;
        for(int i=0;i<k;i++){
            Order o = new Order();
            if(rnd.nextBoolean()){
                o.setCustomer(User.client());
                o.setDistributor(User.admin());
            }else {
                o.setCustomer(User.admin());
                o.setDistributor(User.distributor());
            }
            int cart_count = rnd.nextInt(7)+1;
            Map<Good, Integer> cart= new HashMap<>();
            for(int j=0; j< cart_count;j++){
                cart.put(goodList.get(rnd.nextInt(goodList.size())), rnd.nextInt(100)+4);
            }
            o.setCart(cart);
            OrderStatus[] statuses = OrderStatus.values();
            o.setStatus(statuses[rnd.nextInt(statuses.length-1)]);
            Timestamp date = new Timestamp(faker.date().between(new Date(1449237912000L),new Date(1670162712000L)).getTime());
            o.setCreateDate(date);
            if(rnd.nextBoolean()){
                o.setAddress(faker.address().fullAddress());
                if (rnd.nextBoolean()){
                    date = new Timestamp(date.getTime()+10000000L);
                    o.setDeliveryDate(date);
                    o.setWaybill(faker.numerify("59000##########"));
                }
            }

            orderList.add(o);
        }
        System.out.println("--Замовлення створені--\nПроцес...");
        DAOFactory daoFactory = new MySQLDAO();
        Long start = System.currentTimeMillis();
        for(Order o: orderList){
            daoFactory.createOrderDAO().create(o);
        }
        Long end = System.currentTimeMillis();
        Double time = (end-start)/1000D;
        System.out.println("Виконання(с):"+time);*/

        Long start = System.currentTimeMillis();
        DAOFactory daoFactory = new MongoDBDAO();
        List<Order> orderList = daoFactory.createOrderDAO().findAll(50000);
        Long end = System.currentTimeMillis();
        Double time = (end-start)/1000D;
        System.out.println("Виконання(с):"+time);

        /*Long start = System.currentTimeMillis();
        DAOFactory daoFactory = new MySQLDAO();
        User cus = new User();
        cus.setName("щур");
        User dis = new User();
        dis.setName("симонов");
        List<Order> orderList = daoFactory.createOrderDAO().findByCusAndDis(cus,dis);
        Long end = System.currentTimeMillis();
        Double time = (end-start)/1000D;
        System.out.println("Кількість елементів:"+orderList.size()+"\nВиконання(с):"+time);*/


    }
}
