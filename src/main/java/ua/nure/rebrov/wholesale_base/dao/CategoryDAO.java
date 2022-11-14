package ua.nure.rebrov.wholesale_base.dao;

import ua.nure.rebrov.wholesale_base.model.GoodCategory;

import java.util.List;

public interface CategoryDAO {
    public List<GoodCategory> getAllDistributorCategories(Integer id);
    public List<GoodCategory> getAllParentCategories(Integer id);
}
