package ua.nure.rebrov.wholesale_base.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MySQLDAO;
import ua.nure.rebrov.wholesale_base.security.*;
@org.springframework.stereotype.Controller
public class DistributorController {
    private DAOFactory daoFactory;

    @GetMapping("/priceList")
    public String priceList(Model model) {
        daoFactory = new MySQLDAO();
        model.addAttribute("edit", true);
        model.addAttribute("Goods",  daoFactory.createGoodDAO().getPriceList(SecurityService.getAuthorized()));
        return "index.html";
    }
}
