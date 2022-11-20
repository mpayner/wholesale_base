package ua.nure.rebrov.wholesale_base.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MongoDBDAO;
import ua.nure.rebrov.wholesale_base.model.*;
import ua.nure.rebrov.wholesale_base.security.SecurityService;

import java.util.LinkedList;
import java.util.List;
@org.springframework.stereotype.Controller
public class OrderController {
    private DAOFactory daoFactory;

    @GetMapping("/orders")
    public String orders(Model model, @PathVariable String type){
        daoFactory = new MongoDBDAO();
        User u = SecurityService.getAuthorized();
        List<Order> orderList = null;
        if(u.getType().equals(UserType.Distributor.toString()) || u.getType().equals(UserType.Admin.toString()) && type.equals("sales")){
            orderList = daoFactory.createOrderDAO().findByDistributor(u);
        }else if(u.getType().equals(UserType.Admin.toString()) && type.equals("orders") || u.getType().equals(UserType.Client.toString())){
            orderList = daoFactory.createOrderDAO().findByCustomer(u);
        }
        model.addAttribute("Orders", orderList);
        return "order.html";
    }

    @PostMapping("/updateStatus")
    public String updateStatus(Model model, String id, String status){
        daoFactory = new MongoDBDAO();
        Order o = new Order();
        o.setId(id);
        if(daoFactory.createOrderDAO().updateStatus(o, status)){
            return "redirect.html";

        }else{
            return "404.html";
        }
    }
}
