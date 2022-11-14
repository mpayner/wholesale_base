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
    public static Connection getDefaultConnection() throws
            SQLException {
        if (con == null)
        {
            Properties connInfo = new Properties();
            connInfo.put("user", dbUser);
            connInfo.put("password", dbPassword);
            connInfo.put("useUnicode", "true");
            connInfo.put("characterEncoding", "utf-8");
            return DriverManager.getConnection(dbUrl, connInfo);}
        return con;
    }
}