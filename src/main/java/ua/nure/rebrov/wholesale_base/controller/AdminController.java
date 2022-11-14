package ua.nure.rebrov.wholesale_base.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MySQLDAO;

@org.springframework.stereotype.Controller
public class AdminController {
    private DAOFactory daoFactory;

    @GetMapping("/distributors")
    public String getDistributors(Model model) {
        daoFactory = new MySQLDAO();
        model.addAttribute("distributors",  daoFactory.createUserDAO().getDistributors());
        return "distributors.html";
    }

    @GetMapping("/distributors/id={id}")
    public String getDistributorPrice(Model model, @PathVariable String id) {
        daoFactory = new MySQLDAO();
        System.out.println("SAAS");
        model.addAttribute("edit", false);
        model.addAttribute("Goods",  daoFactory.createGoodDAO().getPriceList(daoFactory.createUserDAO().getById(id)));
        return "index.html";
    }
}
