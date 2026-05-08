package foodordering.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseDAO{

    private static final String URL=
        "jdbc:mysql://localhost:3306/food_ordering_db"+
        "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String DB_USER="root";
    private static final String DB_PASS="12345";

    private static Connection sharedConn;

    protected Connection getConnection(){
        try{
            if(sharedConn==null||sharedConn.isClosed()){
                Class.forName("com.mysql.cj.jdbc.Driver");
                sharedConn=DriverManager.getConnection(URL,DB_USER,DB_PASS);
            }
        }catch(ClassNotFoundException e){
            System.err.println("[DB] Driver missing — add mysql-connector-j JAR.");
        }catch(SQLException e){
            System.err.println("[DB] Connection failed: "+e.getMessage());
        }
        return sharedConn;
    }

    protected abstract String getTableName();
}