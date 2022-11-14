package ua.nure.rebrov.wholesale_base.dao;

import ua.nure.rebrov.wholesale_base.dao.*;
import ua.nure.rebrov.wholesale_base.dao.mongodb.MongoGoodDAO;
import ua.nure.rebrov.wholesale_base.dao.mongodb.MongoOrderDAO;
import ua.nure.rebrov.wholesale_base.dao.mongodb.MongoUserDAO;

public class MongoDBDAO implements DAOFactory {
    @Override
    public OrderDAO createOrderDAO() {
        return new MongoOrderDAO();
    }

    @Override
    public UserDAO createUserDAO() {
        return new MongoUserDAO();
    }

    @Override
    public GoodDAO createGoodDAO() {
        return new MongoGoodDAO();
    }

    @Override
    public CategoryDAO createCategoryDAO() {
        return null;
    }
}
