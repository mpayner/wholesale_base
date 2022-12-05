package ua.nure.rebrov.wholesale_base.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MySQLDAO;
import ua.nure.rebrov.wholesale_base.model.Good;
import ua.nure.rebrov.wholesale_base.security.SecurityService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class SecurityController {
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Вхід");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DAOFactory daoFactory = new MySQLDAO();
        if (authentication != null) {
            Map<String, Integer> cart = SecurityService.getCart(request);
            for(Map.Entry entry : cart.entrySet()){
                Good good = new Good();
                good.setId((String)entry.getKey());
                daoFactory.createGoodDAO().increaseQuantity(good, (Integer)entry.getValue());
                cart.remove(entry.getKey());
            }
            request.getSession().invalidate();
        }
        return "redirect:/";
    }
}
