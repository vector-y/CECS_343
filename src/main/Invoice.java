package main;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Invoice class that handles all functionalities related to the invoice or payments. 
 */
public class Invoice {  
    
    private static int ORDERNUMBER = 0;

    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    
    private double totalDue;
    private double totalPaid;
    private int paymentCount;
    private int dayCount;
    private float invoiceCharge;
    private double deliveryCharge = 0;
    private float invoiceDiscount;
    private ArrayList<String> invoiceState;
    private Map<String, Integer> items;
    static Scanner in = new Scanner(System.in);
    Date now;
    Time time;
    Items item;
    
    // Customer name
    private String firstName;
    private String lastName;
    
    // Employee id
    private int eID;

    public Invoice () {
        this.item = new Items();
    }

    /**
     * Default constructor for Invoice
     * @param date - The date of sale
     * @param time - The time of sale
     * @param firstName - The first name of the customer.
     * @param lastName - The last name of the customer. 
     */
    public Invoice(Date date, Time time, String firstName, String lastName, int eID) {
        this.now = date;
        this.time = time;
        this.firstName = firstName;
        this.lastName = lastName;
        this.eID = eID;
        this.item = new Items();
        items = new HashMap<String, Integer>();
    }
    
    /**
     * Helper function to print the menu
     */
    public void electronicMenu(){
        Items items = new Items();
        System.out.println(items.getItemsForSale());
    }
    
    /**
     * To add new invoice for a valid customer. 
     */
    public void addNewInvoice(){
        Scanner in = new Scanner(System.in);
        int choice;
        System.out.println("\n\nItems For Sale:");
        System.out.println(item.getItemsForSale());
        System.out.print("Please select an item. (Enter -1 when done): ");
        choice = Integer.parseInt(in.nextLine());
        while (choice != -1) {
            System.out.println("Selected Option " + choice);
            addItem(this.item.getName(choice));
            System.out.print("Please select an item. (Enter -1 when done): ");
            choice = Integer.parseInt(in.nextLine());
        }
        ORDERNUMBER = getOrderNumber();
        System.out.println("Do you need it delivered? (Y/N)");
        String delivery = in.nextLine().toUpperCase().trim();

        System.out.println("\nOrder Summary -----------------------");
        System.out.println("Order Number " + ORDERNUMBER);
        for(Map.Entry<String, Integer> electronic : items.entrySet()){
            System.out.printf("%-25s%d\n", electronic.getKey(), electronic.getValue());
        }

        if(delivery.equals("Y")){
            totalDue = finalizeSale(delivery);
            totalDue += (totalDue * deliveryCharge);
            String orderSummary = String.format("TOTAL DUE: %f", totalDue);
            System.out.println(orderSummary);
            System.out.println("Thanks for shopping with us!");
        } else if(delivery.equals("N")){
            totalDue = finalizeSale(delivery);
            String orderSummary = String.format("TOTAL DUE: $%.2f", totalDue);
            System.out.println(orderSummary);
            System.out.println("Thanks for shopping with us!");
        }
    }
    
    public int getOrderNumber() {
        int orderNumber = 0;
        String query = "SELECT COALESCE(number, 0) orderNumber FROM \n"
                + "(SELECT MAX(orderNumber) number from orders) x";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                orderNumber = rs.getInt("orderNumber");
            }
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        if (orderNumber == 0) {
            orderNumber = 1;
        } else {
            orderNumber = orderNumber + 1;
        }
        return orderNumber;
    }
    /**
     * Helper function that prompts for payment. 
     */
    public void makePayment(){
        System.out.println("Enter payment: ");
        double payment = Double.parseDouble((in.nextLine()));
    }
 
    /**
     * query to add new deliveryAddress into database
     * @param address - delivery address 
     * @param city - city for delivery
     * @param zipCode - zip code for delivery
     */
    void insertDeliveryToDatabase(String address, String city, String zipCode) {
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            String overallAddress = String.format("%s %s %s", address, city, zipCode); 
            String insertNewSPSQL = String.format("INSERT INTO Customers(DELIVERYADDRESS) values ('%s')", overallAddress);
            System.out.println(insertNewSPSQL);
            stmt.executeUpdate(insertNewSPSQL);
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    /**
     * Function to prompt for delivery information.
     * @return the delivery address
     */
    public String addDeliveryInfo(){
        Scanner in = new Scanner(System.in);
        System.out.println("What's your address?");
        String address = in.nextLine();
        System.out.println("What's your city?");
        String city = in.nextLine();
        System.out.println("What's your zip code?");
        String zipCode = in.nextLine();
        String overallAddress = String.format("%s %s %s", address, city, zipCode);
        insertDeliveryToDatabase(address, city, zipCode);
        return overallAddress;
    }
 
    
    /**
     * Helper function that adds item to the hash map
     * @param item 
     */
    private void addItem(String item) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1);
        } else {
            items.put(item, 1);
        }
    }
    
    /**
     * Function that finalizes the sale and places all products purchased to database. 
     * @param delivery - "Y" or "N" to denote if delivery option was selected.
     * @return the total amount due for the order as a double. 
     */
    public double finalizeSale(String delivery) {
        // Entering data into Orders
        String query = "INSERT INTO orders (orderNumber, firstName, lastName, delivery, orderDate, orderTime, salesRepID) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement orders = conn.prepareStatement(query);
            orders.setInt(1, ORDERNUMBER);
            orders.setString(2, firstName);
            orders.setString(3, lastName);
            orders.setString(4, delivery);
            orders.setDate(5, Date.valueOf(now.toString()));
            orders.setTime(6, time);
            orders.setInt(7, eID); 
            orders.executeUpdate(); 
            
            query = "INSERT INTO orderDetails (orderNumber, productName, quantity, priceEach) VALUES (?,?,?,?)";
            for(Map.Entry<String, Integer> electronic : items.entrySet()){
                PreparedStatement orderDetails = conn.prepareStatement(query);
                orderDetails.setInt(1, ORDERNUMBER);
                orderDetails.setString(2, electronic.getKey());
                orderDetails.setInt(3, electronic.getValue());
                orderDetails.setDouble(4, item.getCost(electronic.getKey()));        
                orderDetails.executeUpdate();
            }
            
            // To calculate total due.
            query = "SELECT SUM(priceEach * quantity) total FROM orderDetails INNER JOIN orders USING (orderNumber) WHERE orderNumber = "
                    + ORDERNUMBER + " group by ordernumber";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                this.totalDue = rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("ERROR in finalizing sale.");
            e.printStackTrace();
        }
        return this.totalDue;
    }
    
    
    /**
     * Helper function that prints out a formatted table. 
     * @param query
     * @return the table as a String.
     */
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