package ua.nure.rebrov.wholesale_base;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.GoodDAO;
import ua.nure.rebrov.wholesale_base.dao.MySQLDAO;
import ua.nure.rebrov.wholesale_base.dao.OrderDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLGoodDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLOrderDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.ProxyMySQLGoodDAO;
import ua.nure.rebrov.wholesale_base.model.*;
import ua.nure.rebrov.wholesale_base.patterns.GoodHistory;
import ua.nure.rebrov.wholesale_base.patterns.ProxyTest;

import java.sql.Timestamp;
import java.util.*;

public class Test {
    public static void main(String[] args){
        /*LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);*/

        // Тестування різних типів DAO
        /*
        System.out.print("=======================ABSTRACT_FACTORY & SINGLETON=======================\n\n");
        DAOTest daoTest = new DAOTest(new MongoDBDAO());
        daoTest.test();
        daoTest.setDAO(new MySQLDAO());
        daoTest.test();*/

        // Builder
        /*
        System.out.print("\n\n=======================BUILDER=======================\n\n");
        Order o = new Order.Builder(User.client(), User.admin())
                .setStatus(OrderStatus.Created)
                .setAddress("Київ")
                .setId("3")
                .setCreateDate(new Timestamp(System.currentTimeMillis()))
                .addCartItem(Good.random(), 4)
                .addCartItem(Good.random(), 2)
                .build();
        System.out.println(o);
        */


        // Observer
        /*
        System.out.print("\n\n=======================OBSERVER=======================\n\n");
        GoodDAO dao = new MySQLDAO().createGoodDAO();
        dao.add(Good.random());
        dao.add(Good.random());
        Good good = dao.getById("44");
        good.setName("Смаколик");
        good.setPrice(3000.0);
        dao.update(good);
        */

        // MEMENTO
        /*
        System.out.print("\n\n=======================MEMENTO=======================\n\n");
        Good good = new MySQLGoodDAO().getById("25");
        GoodHistory history = new GoodHistory(good.getId());
        System.out.println("[Товар до змінення]");
        System.out.println(good.toDocument().toJson());
        history.save(good);
        good.setName("ОООРЦЙ");
        good.setPrice(4323.0);
        history.save(good);
        System.out.println("[Товар після змінення та збереження]");
        System.out.println(good.toDocument().toJson());
        good.setName("2222");
        history.save(good);
        System.out.println("[Товар після змінення та збереження]");
        System.out.println(good.toDocument().toJson());
        good.restore(history.undo());
        System.out.println("[Дані товару з останньої збереженої версії]");
        System.out.println(good.toDocument().toJson());
        good.restore(history.undo());
        System.out.println("[Дані товару з останньої збереженої версії]");
        System.out.println(good.toDocument().toJson());
        good.restore(history.undo());
        System.out.println("[Дані товару з останньої збереженої версії]");
        System.out.println(good.toDocument().toJson());
        */

        // Proxy
        System.out.print("\n\n=======================PROXY=======================\n\n");
        CurUser.login(User.admin());
        GoodDAO dao = new ProxyMySQLGoodDAO();
        ProxyTest proxy = new ProxyTest();
        proxy.test(dao);
        CurUser.logout();
        CurUser.login(User.client());
        proxy.test(dao);
    }
}
