package ua.nure.rebrov.wholesale_base.dao.mongodb;

import ua.nure.rebrov.wholesale_base.dao.UserDAO;
import ua.nure.rebrov.wholesale_base.model.User;

import java.util.List;

public class MongoUserDAO extends MongoDBschema implements UserDAO {
    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public List<User> getDistributors() {
        return null;
    }

    @Override
    public List<User> getAdmins() {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public User getById(String id) {
        return null;
    }

    @Override
    public Boolean deleteById(String id) {
        return null;
    }
}
