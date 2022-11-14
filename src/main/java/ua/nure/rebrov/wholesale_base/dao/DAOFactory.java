package ua.nure.rebrov.wholesale_base.dao;

public interface DAOFactory {
	public OrderDAO createOrderDAO();
	public UserDAO createUserDAO();
	public GoodDAO createGoodDAO();

	public CategoryDAO createCategoryDAO();
}

