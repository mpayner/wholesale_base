package ua.nure.rebrov.wholesale_base.dao.mysql;

import ua.nure.rebrov.wholesale_base.dao.CategoryDAO;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLConnector;
import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.GoodCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySQLCategoryDAO extends MySQLschema implements CategoryDAO {

    public MySQLCategoryDAO(){
        super();
    }

    @Override
    public List<GoodCategory> getAllParentCategories(Integer id) {
        List<GoodCategory> categoryList = null;
        try {
            PreparedStatement ps = con.prepareStatement("call get_all_parent_categories(?)");
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            categoryList = new LinkedList<>();
            while(rs.next()){
                categoryList.add(new GoodCategory(rs.getInt("id"), rs.getString("name"), rs.getInt("parent_id")));

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryList;
    }

    @Override
    public List<GoodCategory> getAllDistributorCategories(Integer id) {
        return null;
    }
}
