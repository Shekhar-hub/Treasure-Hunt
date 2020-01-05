package pack;

import java.sql.*;

public class DBLoader {

        static Connection conn;
    public static ResultSet executeStatement(String sqlquery) throws Exception {
       
            /////  ##CODE //////
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("driver loaded successfully");
         
            DBLoader.conn
                    = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/chatroom", "root", "shekhar");
            System.out.println("connection built");
            Statement stmt
                    = DBLoader.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE , ResultSet.CONCUR_UPDATABLE);
            System.out.println("statement created");
            ResultSet rs = stmt.executeQuery(sqlquery);
            System.out.println("Resultset created");
            //////////  ##Code Ends Here ////////
//            conn.close();
            return rs;
    }
}
