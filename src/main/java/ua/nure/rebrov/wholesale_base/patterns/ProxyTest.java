package ua.nure.rebrov.wholesale_base.patterns;

import ua.nure.rebrov.wholesale_base.dao.GoodDAO;
import ua.nure.rebrov.wholesale_base.model.Good;

public class ProxyTest {
    public void test(GoodDAO dao){
        System.out.println("Вставка об'єктів");
        Good good = Good.random();
        dao.add(good);
        good.setPrice(3000.0);
        System.out.println("Зміна об'єктів");
        dao.update(good);
        System.out.println("Тест завершено");

    }
}
