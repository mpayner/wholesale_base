package ua.nure.rebrov.wholesale_base.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.nure.rebrov.wholesale_base.dao.DAOFactory;
import ua.nure.rebrov.wholesale_base.dao.MySQLDAO;
import ua.nure.rebrov.wholesale_base.model.*;
import ua.nure.rebrov.wholesale_base.security.SecurityService;

import java.util.*;

@org.springframework.stereotype.Controller
public class GoodController {

    //private IDAO dao = null;
    private DAOFactory daoFactory;


    @GetMapping("/")
    public String index(Model model) {
        daoFactory = new MySQLDAO();
        model.addAttribute("Goods",  daoFactory.createGoodDAO().getBaseGoods());
        return "index.html";
    }



    @GetMapping("/id={id}")
    public String get(@PathVariable String id, Model model) {
        daoFactory = new MySQLDAO();
        Good good = daoFactory.createGoodDAO().getById(id);
        if(good!=null){
            model.addAttribute("categories", daoFactory.createCategoryDAO().getAllParentCategories(Integer.valueOf(good.getId())));
            model.addAttribute("Good",good);
            return "good.html";
        }else{
            return "404.html";
        }
    }

    @GetMapping("/delete={id}")
    public String delete(@PathVariable String id, Model model) {
        daoFactory = new MySQLDAO();
        if(!daoFactory.createGoodDAO().deleteById(id)){
            return "404";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/addGoodToPrice")
    public String addAd(Model model){
        daoFactory = new MySQLDAO();
        User user = SecurityService.getAuthorized();
        Good good = new Good();
        good.setUser(user);
        good.setManufacturer(user);
        model.addAttribute("urlAction", "/addGoodToPrice");
        model.addAttribute("Good", good);
        model.addAttribute("GoodCategory", new GoodCategory());
        model.addAttribute("categories",user.getSpecialization());
        model.addAttribute("units", Arrays.asList(UnitType.Ящик.toString(), UnitType.Вроздріб.toString()));
        return "goodForm.html";
    }

    @PostMapping("/addGoodToPrice")
    public String saveAd(Good good, GoodCategory goodCategory){
        daoFactory = new MySQLDAO();
        User user = SecurityService.getAuthorized();
        try {
            if (user.getType().equals(UserType.Distributor.toString())) {
                good.setUser(user);
                good.setManufacturer(user);
            } else {
                throw new Exception(user.getType() + " | " + UserType.Distributor.toString());
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("Cat:"+goodCategory.getIdCategory()+"|"+goodCategory.getName());
        good.setCategory(goodCategory);
        daoFactory.createGoodDAO().add(good);
        return "redirect:/";
    }

    @GetMapping("/edit={id}")
    public String editGood(@PathVariable String id,Model model){
        daoFactory = new MySQLDAO();
        User user = SecurityService.getAuthorized();
        Good good = daoFactory.createGoodDAO().getById(id);
        model.addAttribute("urlAction", "/editGood");
        model.addAttribute("Good", good);
        model.addAttribute("GoodCategory", new GoodCategory());
        model.addAttribute("categories",user.getSpecialization());
        model.addAttribute("units", Arrays.asList(UnitType.Ящик.toString(), UnitType.Вроздріб.toString()));
        return "goodForm.html";
    }

    @PostMapping("/editGood")
    public String saveEditGood(Good good, GoodCategory goodCategory){
        daoFactory = new MySQLDAO();
        good.setCategory(goodCategory);

        /*Integer buf = good.getCategory().getId();
        good.getCategory().setId(Integer.valueOf(good.getId()));
        good.setId(String.valueOf(buf));*/
        daoFactory.createGoodDAO().update(good);
        return "redirect:/";
    }

}
