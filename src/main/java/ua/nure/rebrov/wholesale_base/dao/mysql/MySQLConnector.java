package ua.nure.rebrov.wholesale_base.dao.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class MySQLConnector {
    private static final String dbUser = "root";
    private static final String dbPassword = "1234";
    private static String dbUrl =
            "jdbc:mysql://localhost/wholesale_base";
    private static Connection con = null;
    public static Connection getDefaultConnection(){
        if (con == null)
        {
            try {
            Properties connInfo = new Properties();
            connInfo.put("user", dbUser);
            connInfo.put("password", dbPassword);
            connInfo.put("useUnicode", "true");
            connInfo.put("characterEncoding", "utf-8");
            return DriverManager.getConnection(dbUrl, connInfo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return con;
    }

    public static void closeConnection(){
        if(con!=null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}