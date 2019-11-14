/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.*;

/**
 *
 * @author cnaei
 */
public class Customer {
    Connection conn;

    /**
     * Default constructor for customer. Inserts the customer data into the database. 
     * @param connect - Connection to the database.
     * @param firstName - First name of the customer.
     * @param lastName - Last name of the customer.
     * @param phone - Phone number of the customer.
     * @param mail - Mail address of the customer.
     * @param delivery - Delivery address of the customer.
     */
    public Customer(Connection connect, String firstName, String lastName, String phone, String mail, String delivery) throws SQLException {
        this.conn = connect;
        if (!firstName.isEmpty() && !lastName.isEmpty()) {
            String query = "INSERT INTO customers (firstName, lastName, phoneNumber, mailAddress, deliveryAddress) VALUES (?,?,?,?,?)";
            try {
                PreparedStatement customer = conn.prepareStatement(query);
                customer.setString(1, firstName); //set values for newBook
                customer.setString(2, lastName);
                customer.setString(3, phone);
                customer.setString(4, mail);
                customer.setString(5, delivery);
                customer.executeUpdate();

            } catch (SQLIntegrityConstraintViolationException exception) {
                System.out.println("Error: Data already exists in the record"); //error: duplicate data 
            }
        } else {
            System.out.println("ERROR: Please enter in first name and last name.");
        } 
    }
 
    
    
    public void getCustomer(String firstName, String lastName) {
        String query = "SELECT firstName, lastName, phoneNumber, mailAddress, deliveryAddress FROM customers ORDER BY lastName asc";
        System.out.println(getTable(query));    
    }
    
    public String getTable(String query) {
        Statement stmt = null;
        String table = "";
        String label = "";
        int columnSize = 0;
        int column = 0;
        try {
            // To list the column labels.
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            column = rsmd.getColumnCount();
            
            for (int i = 1; i <= column; i++) {
                label = rsmd.getColumnName(i);
                columnSize = rsmd.getColumnDisplaySize(i); // Each column may have a different length
                table += String.format("%-" + columnSize + "s", label); // To format by column size from left.
            }
            table += "\n";
            // To store all the rows with format. 
            if (!rs.next()) {
                return "No Data. Please ensure you entered data correctly.";
            } else {
                do {
                    for (int i = 1; i <= column; i++) {
                        columnSize = rsmd.getColumnDisplaySize(i);
                        table += String.format("%-" + columnSize + "s", rs.getString(i));
                    }
                    table += "\n";
                } while (rs.next());
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return table;
    }
}
