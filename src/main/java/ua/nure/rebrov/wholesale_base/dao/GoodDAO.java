package ua.nure.rebrov.wholesale_base.dao;

import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.User;

import java.util.List;

public interface GoodDAO {
    public List<Good> getAll();

    public List<Good> getPriceList(User distributor);

    public List<Good> getBaseGoods();

    public Good getById(String id);

    public boolean updateQuantity(Good good, Integer quantity);

    public boolean increaseQuantity(Good good, Integer quantity);
    public boolean decreaseQuantity(Good good, Integer quantity);


    public Integer getQuantity(Good good);

    public boolean deleteById(String id);
    public void add(Good good);

    public void update(Good good);
}
