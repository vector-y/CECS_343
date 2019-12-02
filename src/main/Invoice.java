/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private int deliveryCharge;
    private float invoiceDiscount;
    private ArrayList<String> invoiceState;
    private Map<String, Integer> items;
    static Scanner in = new Scanner(System.in);
    Date now;
    Time time;
    private String firstName;
    private String lastName;

    /**
     * Default constructor for Invoice
     * @param date - The date of sale
     * @param time - The time of sale
     * @param firstName - The first name of the customer.
     * @param lastName - The last name of the customer. 
     */
    Invoice(Date date, Time time, String firstName, String lastName) {
        this.now = date;
        this.time = time;
        this.firstName = firstName;
        this.lastName = lastName;
        items = new HashMap<String, Integer>();
    }

    /**
     * Helper function to print the invoice menu
     */
    public void invoiceMenu(){
        System.out.println("	(1) Add an electronic");
        System.out.println("	(2) End the sale");
        System.out.print("Please select an option: ");
    }
    
    /**
     * Helper function to print the menu
     */
    public void electronicMenu(){
        System.out.println("	(1) Airpods Pro");
        System.out.println("	(2) Surface Pro X");
        System.out.println("	(3) Macbook Pro");
        System.out.println("	(4) Google Nest Wifi");
        System.out.println("	(5) Bose Noise Cancelling Headphones 700");
        System.out.println("	(6) Google Pixel 4");
        System.out.println("	(7) iPhone 11 Pro");
        System.out.println("	(8) Galaxy Note 10");
        System.out.println("	(9) Amazon Fire TV Stick");
        System.out.println("	(10) Samsung Smart TV 4K with HDR");
    }
    
    /**
     * To add new invoice for a valid customer. 
     */
    public void addNewInvoice(){
        boolean saleOver = false;
        int choice;
        while(saleOver == false){
            invoiceMenu();
            choice = menu(Integer.parseInt(in.nextLine()));
            System.out.println("Selected Option " + choice);
            switch(choice){
                case 1:
                    newElectronicOrder();
                    break;
                case 2:
                    System.out.println("Do you need it delivered? (Y/N)");
                    String delivery = in.nextLine().toUpperCase().trim();
                    System.out.println("\nOrder Summary -----------------------");
                    for(Map.Entry<String, Integer> electronic : items.entrySet()){
                        System.out.println(electronic.getKey());
                    }
                    if(delivery.equals("Y")){
                        ORDERNUMBER += 1;
                        totalDue = finalizeSale(delivery);
                        totalDue += deliveryCharge;
                        String orderSummary = String.format("TOTAL DUE: %f", totalDue);
                        System.out.println(orderSummary);
                        System.out.println("Thanks for shopping with us!");
                    } else if(delivery.equals("N")){
                        totalDue = finalizeSale(delivery);
                        String orderSummary = String.format("TOTAL DUE: %.2f", totalDue);
                        System.out.println(orderSummary);
                        System.out.println("Thanks for shopping with us!");
                    }
                    saleOver = true;
                    break;
            }
        }
    }
    
    /**
     * Helper function that prompts for payment. 
     */
    public void makePayment(){
        System.out.println("Enter payment: ");
        double payment = Double.parseDouble((in.nextLine()));
        //double change =
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
     * Helper function that adds the items into the hash map and keeps count of items. 
     */
    public void newElectronicOrder(){
        electronicMenu();
        int choice;
        choice = menu(Integer.parseInt(in.nextLine()));
        switch(choice) {
            case 1:
                addItem("AirPods Pro");
                break;
            case 2:
                addItem("Surface Pro X");
                break;
            case 3:
                addItem("Macbook Pro");
                break;
            case 4:
                addItem("Google Nest Wifi");
                break;
            case 5:
                addItem("Bose Noise Cancelling Headphones 700");
                break;
            case 6:
                addItem("Google Pixel 4");
                break;
            case 7:
                addItem("iPhone 11 Pro");
                break;
            case 8:
                addItem("Galaxy Note 10");
                break;
            case 9:
                addItem("Amazon Fire TV Stick");
                break;
            case 10:
                addItem("Samsung Smart TV 4K with HDR");
                break;
        }
    }
    
    /**
     * Helper function that gets the cost of the items.
     * @param selection
     * @return cost as double
     */
    public double getCost(String selection) {
        if(selection.equals("AirPods Pro")) {
            return 100;
        }
        if(selection.equals("Surface Pro X")) {
            return 200;
        }
        if(selection.equals("Macbook Pro")) {
            return 300;
        }
        if(selection.equals("Google Nest Wifi")) {
            return 400;
        }
        if(selection.equals("Bose Noise Cancelling Headphones 700")) {
            return 500;
        }
        if(selection.equals("Google Pixel 4")) {
            return 600;
        }
        if(selection.equals("iPhone 11 Pro")) {
            return 700;
        }
        if(selection.equals("Galaxy Note 10")) {
            return 800;
        }
        if(selection.equals("Amazon Fire TV Stick")) {
            return 900;
        }
        if(selection.equals("Samsung Smart TV 4K with HDR")) {
            return 1000;
        }
        return 0;
    }
    
    /**
     * Returns an int to let us know what the user picked
     * @param x - user input as an integer.
     * @return an integer that the user picked.
     */
    public static int menu(int x) {
        return x;
    }
    
    /**
     * Function to prompt for delivery information.
     * @return the delivery address
     */
    public String addDeliveryInfo(){
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
     * To display the open invoice 
     * @param orderNumber - order number as an integer
     */
    public void displayOpenInvoice(int orderNumber){
        Statement stmt = null;
        ResultSet rs = null;
        //query to check for the name
        boolean valid = checkInvoice(orderNumber);
        //FULL VERSION: String invoice_display_format = "%f,%f,%d,%d,%f,%d,%f,'%s','%s'\n";
        String invoice_display_format = "%f,%f,%d,%d,%f,%d,%f \n";
        System.out.printf(invoice_display_format, "TOTALDUE","TOTALPAID","PAYMENTCOUNT","DAYCOUNT","INVOICECHARGE","DELIVERYCHARGE","INVOICEDISCOUNT");
        try{
            if (valid){
                stmt = conn.createStatement();
                String salesPersonSQL = "SELECT TOTALDUE,TOTALPAID,PAYMENTCOUNT,DAYCOUNT,INVOICECHARGE,DELIVERYCHARGE,INVOICEDISCOUNT FROM INVOICES";
                rs = stmt.executeQuery(salesPersonSQL);
                
                while (rs.next()){
                    double totalDue = rs.getDouble("TOTALDUE");
                    double totalPaid = rs.getDouble("TOTALPAID");
                    int paymentCount = rs.getInt("PAYMENYCOUNT");
                    int dayCount = rs.getInt("DAYCOUNT");
                    float invoiceCharge = rs.getFloat("INVOICECHARGE");
                    int deliveryCharge = rs.getInt("DELIVERYCHARGE");
                    float invoiceDiscount = rs.getFloat("INVOICEDISCOUNT");
                    System.out.printf(invoice_display_format, totalDue,totalPaid,paymentCount,dayCount,invoiceCharge,deliveryCharge,invoiceDiscount);
                }
                rs.close();
                stmt.close();
            }
            else{
                System.out.println("No valid invoice in the database");
            }
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    /**
     * Helper function to determine if the invoice is valid
     * @param orderNumber
     * @return True if valid
     */
    public boolean checkInvoice(int orderNumber){
        Statement stmt = null;
        ResultSet rs = null;
        System.out.println(orderNumber);
        
        System.out.println(orderNumber);
        try {
            stmt = conn.createStatement();
            String invoiceSQL = "SELECT * FROM ORDERS";
            //initlize for salesperson flag
            boolean i_name_flag = false;
            rs = stmt.executeQuery(invoiceSQL);
            
            //loop through database and see if the user input's salesperson's name exists or not
            //if exists, set the flag to be true
            while (rs.next()) {
                int dataOrderNum = rs.getInt("ORDERNUMBER");
                //i_first/lastname is the name we can return true
                if (orderNumber == dataOrderNum) {
                    i_name_flag  = true;
                }
            }
            rs.close();
            stmt.close();
            return i_name_flag;
        } 
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        return false;
    }
    
    
    /*To view the current invoices that have been fully paid. */
    public String displayClosedInvoice(){
        String orderSummary = "";//string for all sales
        for(String c : invoiceState) {
            if(c.equals("Closed")){
               orderSummary += c;
               orderSummary += "\n"; 
            }    
        }
        return orderSummary;
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
            
            orders.setInt(1, ORDERNUMBER); //set values for newBook
            orders.setString(2, firstName);
            orders.setString(3, lastName);
            orders.setString(4, delivery);
            orders.setDate(5, Date.valueOf(now.toString()));
            orders.setTime(6, time);
            orders.setInt(7, 1); // NEED TO LINK --------------------------
            orders.executeUpdate(); 
            
            query = "INSERT INTO orderDetails (orderNumber, productName, quantity, priceEach) VALUES (?,?,?,?)";
            for(Map.Entry<String, Integer> electronic : items.entrySet()){
                PreparedStatement orderDetails = conn.prepareStatement(query);
                orderDetails.setInt(1, ORDERNUMBER);
                orderDetails.setString(2, electronic.getKey());
                orderDetails.setInt(3, electronic.getValue());
                orderDetails.setDouble(4, getCost(electronic.getKey()));
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