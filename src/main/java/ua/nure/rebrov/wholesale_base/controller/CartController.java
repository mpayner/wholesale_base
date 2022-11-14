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
import java.util.Map;
import java.util.TreeMap;

@Controller
public class CartController {

    private DAOFactory daoFactory;

    int cart_items = 0;

    @GetMapping("/addToCart")
    public String addCartItem(Model model, HttpServletResponse response,HttpServletRequest request, @Param("id") String id, @Param("quantity") String quantity) {
        if(id!=null && quantity!=null){
            daoFactory = new MySQLDAO();
            Good g = new Good();
            g.setId(id);
            if(daoFactory.createGoodDAO().decreaseQuantity(g, Integer.valueOf(quantity))){
                response.addCookie(new Cookie(id, quantity));
                Cookie[] cookies = request.getCookies();
                for(Cookie c : cookies){
                    if(!c.getName().equals(id)){
                        response.addCookie(new Cookie("cart_items",String.valueOf(++cart_items)));
                        break;
                    }
                }
            }else {
                return "redirect:/403.html";
            }
        }
        return "redirect.html";

    }

    @GetMapping("/increaseQuantity")
    public String increaseQuantity(Model model, HttpServletRequest request,HttpServletResponse response, @Param("id") String id, @Param("quantity") String quantity) {
        daoFactory = new MySQLDAO();
        if(id!=null && quantity!=null){
            Good good = new Good();
            good.setId(id);
                Cookie[] cookies = request.getCookies();
                for(Cookie c : cookies){
                    if(c.getName().equals(id)){
                        c.setMaxAge(0);
                        daoFactory.createGoodDAO().increaseQuantity(good, Integer.valueOf(c.getValue())-Integer.valueOf(quantity));
                        response.addCookie(new Cookie(id, quantity));
                        break;
                    }
                }


        }
        return "redirect.html";
    }

    @GetMapping("/decreaseQuantity")
    public String decreaseQuantity(Model model, HttpServletRequest request,HttpServletResponse response, @Param("id") String id, @Param("quantity") String quantity) {
        daoFactory = new MySQLDAO();
        if(id!=null && quantity!=null){
            Good good = new Good();
            good.setId(id);
            Cookie[] cookies = request.getCookies();
            for(Cookie c : cookies){
                if(c.getName().equals(id)){
                    c.setMaxAge(0);
                    daoFactory.createGoodDAO().decreaseQuantity(good, Integer.valueOf(quantity)-Integer.valueOf(c.getValue()));
                    response.addCookie(new Cookie(id, quantity));
                    break;
                }
            }
        }
        //sdf
        return "redirect.html";
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpServletRequest request) {
        Map<Good, Integer> goods = new TreeMap<>();
        Cookie[] cookies = request.getCookies();
        daoFactory = new MySQLDAO();
        for(Cookie c : cookies){
            Good good = daoFactory.createGoodDAO().getById(c.getName());
            if(good!=null){
                goods.put(good,Integer.valueOf(c.getValue()));
            }
        }
        model.addAttribute("cartItems", cart_items);
        model.addAttribute("cart", goods);
        return "cart.html";
    }

    @GetMapping("/deleteCartItem={id}")
    public String delete(@PathVariable String id, Model model, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie target = null;
        for(Cookie c : cookies){
            if(c.getName().equals(id)){
                target = c;
                break;
            }
        }
        daoFactory = new MySQLDAO();
        Good good = new Good();
        good.setId(target.getName());
        if(daoFactory.createGoodDAO().increaseQuantity(good,Integer.valueOf(target.getValue()))) {
            target.setMaxAge(0);
            response.addCookie(new Cookie("cart_items", String.valueOf(--cart_items)));
            response.addCookie(target);
            return "redirect:/cart";
        }else {
            return "redirect.html";
        }
    }


    @GetMapping("/cart/createBaseOrder")
    public String saveOrder(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Map<Good,Integer> cart = new TreeMap<>();
        daoFactory = new MySQLDAO();
        for(Cookie c : cookies){
            Good good = daoFactory.createGoodDAO().getById(c.getName());
            if(good!=null){
                cart.put(good,Integer.valueOf(c.getValue()));
                Cookie delete = new Cookie(c.getName(),c.getValue());
                delete.setMaxAge(0);
                response.addCookie(delete);
            }
        }
        cart_items=0;
        response.addCookie(new Cookie("cart_items", "0"));
        daoFactory.createOrderDAO().create(Order.groupOrders(cart,SecurityService.getAuthorized()));
        return "redirect:/";
    }

}
