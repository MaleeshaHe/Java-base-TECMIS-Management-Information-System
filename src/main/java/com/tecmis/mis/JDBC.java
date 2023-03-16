package com.tecmis.mis;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBC {
    static Connection connection(){
        try {
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/tecmis", "root", "");
            return con;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
