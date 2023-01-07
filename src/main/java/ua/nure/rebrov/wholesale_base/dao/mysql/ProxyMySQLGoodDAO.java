package ua.nure.rebrov.wholesale_base.dao.mysql;

import ua.nure.rebrov.wholesale_base.dao.GoodDAO;
import ua.nure.rebrov.wholesale_base.model.CurUser;
import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.User;
import ua.nure.rebrov.wholesale_base.model.UserType;

import java.util.List;

public class ProxyMySQLGoodDAO implements GoodDAO {

    private MySQLGoodDAO dao = new MySQLGoodDAO();

    private void checkUserType() throws Exception{
        User curUser = CurUser.get();
        if(curUser==null || curUser.getType().equals(UserType.Client.toString())){
            throw new Exception(curUser.getName()+", доступ на зміну даних заборонений!");
        }
    }

    @Override
    public List<Good> getAll() {
        return dao.getAll();
    }

    @Override
    public List<Good> getPriceList(User distributor) {
        try {
            checkUserType();
            return dao.getPriceList(distributor);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Good> getBaseGoods() {
        return dao.getBaseGoods();
    }

    @Override
    public Good getById(String id) {
        return dao.getById(id);
    }

    @Override
    public boolean updateQuantity(Good good, Integer quantity) {
        try {
            checkUserType();
            return dao.updateQuantity(good, quantity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean increaseQuantity(Good good, Integer quantity) {
        try {
            checkUserType();
            return dao.increaseQuantity(good, quantity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean decreaseQuantity(Good good, Integer quantity) {
        try {
            checkUserType();
            return dao.decreaseQuantity(good, quantity);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer getQuantity(Good good) {
        return dao.getQuantity(good);
    }

    @Override
    public boolean deleteById(String id) {
        try {
            checkUserType();
            return dao.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void add(Good good) {
        try {
            checkUserType();
            dao.add(good);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void add(List<Good> good) {
        try {
            checkUserType();
            dao.add(good);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Good good) {
        try {
            checkUserType();
            dao.update(good);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
