package ua.nure.rebrov.wholesale_base.controller;

import org.springframework.data.repository.query.Param;
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

    @GetMapping("/myOrders")
    public String myOrders(Model model, @Param("type") String type){
        if(type==null){
            type = "sales";
        }
        daoFactory = new MongoDBDAO();
        User u = SecurityService.getAuthorized();
        List<Order> orderList = null;
        if(u.getType().equals(UserType.Distributor.toString()) || u.getType().equals(UserType.Admin.toString()) && type.equals("sales")){
            orderList = daoFactory.createOrderDAO().findByDistributor(u);
        }
        if(u.getType().equals(UserType.Admin.toString()) && type.equals("orders") || u.getType().equals(UserType.Client.toString())){
            orderList = daoFactory.createOrderDAO().findByCustomer(u);
        }
        model.addAttribute("curUrl", "/myOrders");
        model.addAttribute("Orders", OrderStatus.availableStatusesForOrders(orderList));
        return "order.html";
    }

    @GetMapping("/orders")
    public String orders(Model model,  @Param("customer") String customer, @Param("distributor") String distributor){
        daoFactory = new MongoDBDAO();
        try {
            if (customer == null && distributor == null || customer.isEmpty() && distributor.isEmpty()) {
                model.addAttribute("Orders", OrderStatus.availableStatusesForOrders(daoFactory.createOrderDAO().findAll(10)));
                throw new Exception();
            }
            User c = new User();
            c.setName(customer);
            User d = new User();
            d.setName(distributor);
            if (!customer.isEmpty() && !distributor.isEmpty()) {
                model.addAttribute("Orders", OrderStatus.availableStatusesForOrders(daoFactory.createOrderDAO().findByCusAndDis(c, d)));
                throw new Exception();
            }
            if (!customer.isEmpty()) {
                model.addAttribute("Orders", OrderStatus.availableStatusesForOrders(daoFactory.createOrderDAO().findByCustomer(c)));
                throw new Exception();
            }
            if (!distributor.isEmpty()) {
                model.addAttribute("Orders", OrderStatus.availableStatusesForOrders(daoFactory.createOrderDAO().findByDistributor(d)));
                throw new Exception();
            }
        }catch (Exception e){
            model.addAttribute("curUrl", "/orders");
            return "order.html";
        }
        return null;
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
