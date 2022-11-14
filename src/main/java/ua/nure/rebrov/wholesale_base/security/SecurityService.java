package ua.nure.rebrov.wholesale_base.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MySQLDAO;
import ua.nure.rebrov.wholesale_base.model.User;

public class SecurityService {
    public static User getAuthorized(){
        DAOFactory dao = new MySQLDAO();
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return dao.createUserDAO().getByEmail(login);
    }
}
