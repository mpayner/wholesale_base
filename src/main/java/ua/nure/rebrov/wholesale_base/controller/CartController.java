package ua.nure.rebrov.wholesale_base.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MySQLDAO;
import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.model.Order;
import ua.nure.rebrov.wholesale_base.security.SecurityService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class CartController {

    private DAOFactory daoFactory;

    int cart_items = 0;

    @GetMapping("/addToCart")
    public String addCartItem(Model model, HttpServletResponse response,HttpServletRequest request, @Param("id") String id, @Param("quantity") Integer quantity) {
        if(id!=null && quantity!=null){
            daoFactory = new MySQLDAO();
            Good g = new Good();
            g.setId(id);
            if(daoFactory.createGoodDAO().decreaseQuantity(g, quantity)){
               /* response.addCookie(new Cookie(id, quantity));
                Cookie[] cookies = request.getCookies();
                for(Cookie c : cookies){
                    if(!c.getName().equals(id)){
                        response.addCookie(new Cookie("cart_items",String.valueOf(++cart_items)));
                        break;
                    }
                }*/
                response.addCookie(new Cookie("cart_items",String.valueOf(++cart_items)));
                SecurityService.addToCart(request,id, quantity);
            }else {
                return "redirect:/403.html";
            }
        }
        return "redirect.html";

    }

    @GetMapping("/increaseQuantity")
    public String increaseQuantity(Model model, HttpServletRequest request,HttpServletResponse response, @Param("id") String id, @Param("quantity") Integer quantity) {
        daoFactory = new MySQLDAO();
        if(id!=null && quantity!=null){
            Good good = new Good();
            good.setId(id);
            Integer oldValue = SecurityService.editCart(request,id, quantity);
            daoFactory.createGoodDAO().increaseQuantity(good, oldValue-quantity);

        }
        return "redirect.html";
    }

    @GetMapping("/decreaseQuantity")
    public String decreaseQuantity(Model model, HttpServletRequest request,HttpServletResponse response, @Param("id") String id, @Param("quantity") Integer quantity) {
        daoFactory = new MySQLDAO();
        if(id!=null && quantity!=null){
            Good good = new Good();
            good.setId(id);
            Integer oldValue = SecurityService.editCart(request,id, quantity);
            daoFactory.createGoodDAO().decreaseQuantity(good, quantity-oldValue);

        }

        return "redirect.html";
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpServletRequest request) {
        Map<Good, Integer> goods = new TreeMap<>();
        Cookie[] cookies = request.getCookies();
        daoFactory = new MySQLDAO();
        Map<String, Integer> cart = SecurityService.getCart(request);
        if(cart!=null){
            for(Map.Entry entry : cart.entrySet()){
                Good good = daoFactory.createGoodDAO().getById((String)entry.getKey());
                if(good!=null){
                    goods.put(good,(Integer) entry.getValue());
                }
            }
        }

        model.addAttribute("cartItems", cart_items);
        model.addAttribute("cart", goods);
        return "cart.html";
    }

    @GetMapping("/deleteCartItem={id}")
    public String delete(@PathVariable String id, Model model, HttpServletRequest request, HttpServletResponse response) {
        daoFactory = new MySQLDAO();
        Good good = new Good();
        good.setId(id);
        Integer oldValue = SecurityService.removeFromCart(request, id);
        if(daoFactory.createGoodDAO().increaseQuantity(good,oldValue)) {
            response.addCookie(new Cookie("cart_items", String.valueOf(--cart_items)));
            return "redirect:/cart";
        }else {
            return "redirect.html";
        }
    }


    @GetMapping("/cart/createBaseOrder")
    public String saveOrder(HttpServletRequest request, HttpServletResponse response) {
        Map<Good, Integer> goods = new TreeMap<>();
        daoFactory = new MySQLDAO();
        Map<String, Integer> cart = SecurityService.getCart(request);
        if(cart!=null){
            for(Map.Entry entry : cart.entrySet()){
                Good good = daoFactory.createGoodDAO().getById((String)entry.getKey());
                if(good!=null){
                    goods.put(good,(Integer) entry.getValue());
                }
            }
        }
        cart.clear();
        Cookie c = new Cookie("cart_items", String.valueOf(0));
        c.setPath("/");
        response.addCookie(c);
        daoFactory.createOrderDAO().create(Order.groupOrders(goods,SecurityService.getAuthorized()));
        return "redirect:/";
    }

}
