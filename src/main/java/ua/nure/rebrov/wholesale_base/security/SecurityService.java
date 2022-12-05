package ua.nure.rebrov.wholesale_base.security;

import org.springframework.security.core.context.SecurityContextHolder;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MySQLDAO;
import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SecurityService {
    public static User getAuthorized(){
        DAOFactory dao = new MySQLDAO();
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return dao.createUserDAO().getByEmail(login);
    }

    public static Map<String, Integer> getCart(HttpServletRequest request){
        try {
            HttpSession session = request.getSession();
            Object c = session.getAttribute("cart");
            if(c == null){
                Map<String, Integer> cart = new HashMap<>();
                session.setAttribute("cart", cart);
                return new HashMap<>();
            }else{
                return (Map<String, Integer>) c;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Integer addToCart(HttpServletRequest request, String goodId, Integer quantity){
        try{
            Map<String, Integer> cart = getCart(request);
            Integer oldQuantity = null;
            if (cart != null) {
                oldQuantity = cart.get(goodId);
                cart.put(goodId, quantity);
            }
            return oldQuantity;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static Integer editCart(HttpServletRequest request, String goodId, Integer quantity){
        try{
            Map<String, Integer> cart = getCart(request);
            Integer oldQuantity = null;
            if (cart != null) {
                oldQuantity = cart.get(goodId);
                cart.replace(goodId, quantity);
            }
            return oldQuantity;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Integer removeFromCart(HttpServletRequest request,String id){
        try{
            Map<String, Integer> cart = getCart(request);
            Integer oldQuantity = null;
            if (!cart.isEmpty()) {
                oldQuantity = cart.get(id);
                if(oldQuantity==null){
                    throw new Exception("Неможливо видалити товар: товару не існує в корзині");
                }
                cart.remove(id);
                return oldQuantity;
            }else {
                throw new Exception("Неможливо видалити товар: кошик пустий");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }





}
