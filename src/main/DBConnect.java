/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.sql.*;

/**
 * Database Connection class 
 */
public class DBConnect {
    private static Connection connection = null;
    private static String USER;
    private static String PASS;
    private static String DBNAME;
    private static final String displayFormat="%-5s%-15s%-15s%-15s\n";
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private static String DB_URL = "jdbc:derby://localhost:1527/";
   
    Connection conn = null; 
    Statement stmt = null; 
    
    /**
     * Connection class that returns the connection. 
     * @return connection
     */
    public Connection connect() {
        DB_URL = DB_URL + "BitsPlease;user=cecs343;password=cecs343";
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_URL);
            return conn;
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
            return null;
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
            return null;
        } 
    }
    
    /**
     * To close the connection.
     */
    public void closeConnection(){
         try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(DB_URL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept) {
            System.out.println("ERROR: Database connection close failed.");
        }
    }
}
