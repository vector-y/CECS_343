/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author blahb
 */
public class Payments {
    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    public Payments(int customerId, int orderNumber, String date, double amount){
        insertPaymentsToDatabase(customerId,orderNumber,date,amount);
    }

    private void insertPaymentsToDatabase(int customerID, int orderNumber, String date, double amount) {
       try {
            String query = "INSERT INTO customers (CUSTOEMRID,ORDERNUMBER,PAYMENTDATE,AMOUNT) VALUES (?,?,?,?)";
            PreparedStatement customer = conn.prepareStatement(query);
            customer.setInt(1, customerID); //set values for newBook
            customer.setInt(2, orderNumber);
            customer.setString(3, date);
            customer.setDouble(4, amount);
            customer.executeUpdate();

                
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
