package main;

import java.sql.*;
import java.util.Arrays;

/**
 * Customer class that handles all functionalities related to customer. 
 */
public class Customer {
    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    private int cID;
    
    /**
     * Default constructor
     */
    public Customer() {
        cID = 0;
    }
    
    /**
     * Overloaded constructor to add customer. Inserts the customer data into the database. 
     * @param connect - Connection to the database.
     * @param firstName - First name of the customer.
     * @param lastName - Last name of the customer.
     * @param phone - Phone number of the customer.
     * @param mail - Mail address of the customer.
     * @param delivery - Delivery address of the customer.
     */
    public Customer(String firstName, String lastName, String phone, String mail, String delivery) {
        setCID();
        try {
            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                String query = "INSERT INTO customers (cID, firstName, lastName, phoneNumber, mailAddress, deliveryAddress) VALUES (?,?,?,?,?,?)";
                try {
                    PreparedStatement customer = conn.prepareStatement(query);
                    customer.setInt(1, this.cID);
                    customer.setString(2, firstName); //set values for newBook
                    customer.setString(3, lastName);
                    customer.setString(4, phone);
                    customer.setString(5, mail);
                    customer.setString(6, delivery);
                    customer.executeUpdate();
                    System.out.println("\nSuccessfully Added New Customer");

                } catch (SQLIntegrityConstraintViolationException exception) {
                    System.out.println("Error: Customer already exists in the record"); //error: duplicate data 
                }
            } else {
                System.out.println("ERROR: Please enter in first name and last name.");
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * To check if the customer is an existing customer in the database. 
     * @param fName - First name of the customer.
     * @param lName - Last name of the customer. 
     * @return true if customer exists in database. 
     */
    public boolean isValidCustomer(String fName, String lName) {
        String query = "SELECT firstName, lastName FROM customers WHERE firstName = '" + fName + "' AND lastName = '" + lName + "'";
        String firstName = "";
        String lastName = "";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                firstName = rs.getString("firstName");
                lastName = rs.getString("lastName");
            }
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return firstName.equals(fName) && lastName.equals(lName);
    }
    private void setCID() {
        int cID = 0;
        String query = "SELECT COALESCE(number, 0) cID FROM \n"
                + "(SELECT MAX(cID) number from customers) x";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                cID = rs.getInt("cID");
            }
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        if (cID == 0) {
            this.cID = 1;
        } else {
            this.cID = cID + 1;
        }
    }
}
