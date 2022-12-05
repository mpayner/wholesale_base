package ua.nure.rebrov.wholesale_base.dao.mysql;

import ua.nure.rebrov.wholesale_base.dao.mysql.MySQLConnector;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLschema {
    protected Connection con;

    public MySQLschema(){
        con = MySQLConnector.getDefaultConnection();
    }
}
