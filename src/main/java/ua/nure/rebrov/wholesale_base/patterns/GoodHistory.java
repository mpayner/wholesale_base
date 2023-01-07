package ua.nure.rebrov.wholesale_base.patterns;

import com.google.gson.Gson;
import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLGoodDAO;
import ua.nure.rebrov.wholesale_base.model.Good;

import java.util.Stack;

public class GoodHistory {
    private Stack<Good> undo;
    private MySQLGoodDAO dao;

    private String id;

    public GoodHistory(String id){
        this.id = id;
        undo = new Stack<>();
        dao = new MySQLGoodDAO();
    }

    public void save(Good o){
        if(id.equals(o.getId())) {
            dao.update(o);
            undo.push(new Good(o));
        }
    }

    public Good undo(){
        if (dao.getById(id)==null ) {
            return null;
        }else if (undo.size() == 1){
            return undo.peek();
        }else {
            return undo.pop();
        }
    }

    public Stack<Good> getUndo() {
        return undo;
    }

}

