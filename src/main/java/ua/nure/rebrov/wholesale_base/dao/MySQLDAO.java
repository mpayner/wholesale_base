package ua.nure.rebrov.wholesale_base.dao;


import ua.nure.rebrov.wholesale_base.dao.*;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLCategoryDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLGoodDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLOrderDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLUserDAO;

public class MySQLDAO implements DAOFactory {

    @Override
    public OrderDAO createOrderDAO() {
        return new MySQLOrderDAO();
    }

    @Override
    public UserDAO createUserDAO() {
        return new MySQLUserDAO();
    }

    @Override
    public GoodDAO createGoodDAO() {
        return new MySQLGoodDAO();
    }

    @Override
    public CategoryDAO createCategoryDAO() {
        return new MySQLCategoryDAO();
    }
}
