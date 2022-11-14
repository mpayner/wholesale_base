package ua.nure.rebrov.wholesale_base.dao;

import ua.nure.rebrov.wholesale_base.model.User;

import java.util.List;

public interface UserDAO {
    public List<User> getAll();

    public List<User> getDistributors();
    public void save(User user);
    public User getById(String id);

    public User getByEmail(String email);
    public Boolean deleteById(String id);
}
