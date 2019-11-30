/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
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

/** @author victor
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
//    private ArrayList<String> listOfItems;
    private ArrayList<String> invoiceState;
    private Map<String, Integer> items;
    static Scanner in = new Scanner(System.in);
    Date now;
    Time time;
    private String firstName;
    private String lastName;
    public static void main(String[] args) {

    }

    //private Boolean debugValue = false;
    /*
        float totalDue : How much needs to be paid
        float paymentReceiptcount : How many payments receipts paid the invoice
        int dayCount: How many days it took them to pay
        float invoiceCharge: A 2% charge if invoice isn't paid
        int deliveryCharge: A flat fee for delivery
        float invoiceDiscount : A 10% discount if invoice is paid in 10 days
        Items[] listOfItems : The list of items sold / to deliver to customer
        bool state : A list of services provided
        Date dateSold : The date of when the invoice first started
    */
    public Invoice(double totalDue, double totalPaid, int paymentCount, int dayCount, float invoiceCharge, int deliveryCharge, float invoiceDiscount, ArrayList<String> listOfItems, ArrayList<String> invoiceState) {
        insertInvoiceToDatabase(totalDue,totalPaid,paymentCount,dayCount,invoiceCharge,deliveryCharge,invoiceDiscount,listOfItems,invoiceState);
    }

    Invoice(Date now, Time time, String firstName, String lastName) {
        this.now = now;
        this.time = time;
        this.firstName = firstName;
        this.lastName = lastName;
        items = new HashMap<String, Integer>();

//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /*
        this.totalDue = totalDue;
        this.totalPaid = totalPaid;
        this.paymentReceiptCount = paymentReceiptCount;
        this.dayCount = dayCount;
        this.invoiceCharge = invoiceCharge;
        deliveryCharge = 10;
        this.invoiceDiscount = invoiceDiscount;
        listOfItems = new ArrayList<String>();
        openInvoices = new ArrayList<String>();
        closedInvoices = new ArrayList<String>();
    */
    
    public void invoiceMenu(){
        System.out.println("	(1) Add an electronic");
        System.out.println("	(2) End the sale");
    }
    
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
    
    /* To add an invoice for a new order. */
    public void addNewInvoice(){
        boolean saleOver = false;
        int choice;
        while(saleOver == false){
            invoiceMenu();
            choice = menu(Integer.parseInt(in.nextLine()));
            System.out.println("input is..." + choice);
            switch(choice){
                case 1:
                    newElectronicOrder();
                    break;
                case 2:
                    System.out.println("What you have purchased...");
                    for(Map.Entry<String, Integer> electronic : items.entrySet()){
                        System.out.println(electronic.getKey());
                    }
                    System.out.println("Do you need it delivered? (Y/N)");
                    String delivery = in.nextLine().toUpperCase().trim();
                    if(delivery.equals("Y")){
                        ORDERNUMBER += 1;
                        totalDue = finalizeSale(delivery);
                        totalDue += deliveryCharge;
                        String orderSummary = String.format("Here's the total due: %f", totalDue);
                        System.out.println(orderSummary);
                        System.out.println("Thanks for shopping with us!");
                    } else if(delivery.equals("N")){
                        totalDue = finalizeSale(delivery);
                        String orderSummary = String.format("Here's the total due: %f", totalDue);
                        System.out.println(orderSummary);
                        System.out.println("Thanks for shopping with us!");
                    }
                    saleOver = true;
                    break;
            }
        }
    }
    
    public void makePayment(){
        System.out.println("Enter payment: ");
        double payment = Double.parseDouble((in.nextLine()));
        //double change =
    }
    
    //query to add new invoice into database
    void insertInvoiceToDatabase(double totalDue, double totalPaid, int paymentCount, int dayCount, float invoiceCharge, int deliveryCharge, float invoiceDiscount, ArrayList<String> listOfItems, ArrayList<String> invoiceState) {
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            String insertNewSPSQL = String.format("INSERT INTO Invoices(TOTALDUE,TOTALPAID,PAYMENTCOUNT,DAYCOUNT,INVOICECHARGE,DELIVERYCHARGE,INVOICEDISCOUNT,LISTOFITEMS,INVOICESTATE) values (%f,%f,%d,%d,%f,%d,%f,'%s','%s')", totalDue,totalPaid,paymentCount,dayCount,invoiceCharge,deliveryCharge,invoiceDiscount,listOfItems,invoiceState);
            System.out.println(insertNewSPSQL);
            stmt.executeUpdate(insertNewSPSQL);
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    
    
    //query to add new deliveryAddress into database
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
    
    public void newElectronicOrder(){
//        listOfItems = new ArrayList<String>();        

        electronicMenu();
        int choice;
        choice = menu(Integer.parseInt(in.nextLine()));
        switch(choice) {
            case 1:
//                listOfItems.add("AirPods Pro");
                addItem("AirPods Pro");
            case 2:
//                listOfItems.add("Surface Pro X");
                addItem("Surface Pro X");
            case 3:
//                listOfItems.add("Macbook Pro");
                addItem("Macbook Pro");
            case 4:
                addItem("Google Nest Wifi");
            case 5:
                addItem("Bose Noise Cancelling Headphones 700");
            case 6:
                addItem("Google Pixel 4");
            case 7:
                addItem("iPhone 11 Pro");
            case 8:
                addItem("Galaxy Note 10");
            case 9:
                addItem("Amazon Fire TV Stick");
            case 10:
                addItem("Samsung Smart TV 4K with HDR");
        }
    }
    
    public double getTotal(){
        double totalDue = 0;
        for(Map.Entry<String, Integer> electronic : items.entrySet()){
            
            totalDue += (getCost(electronic.getKey()) * electronic.getValue());
        }
        return totalDue;
    }
    
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
    
    //Returns an int to let us know what the user picked
    public static int menu(int x) {
        return x;
    }
    
    public void add(String s){
//        listOfItems.add(s);
        
    }
    
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
    
    /* To view the current invoices that have not been fully paid. */
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
    
    private void addItem(String item) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1);
        } else {
            items.put(item, 1);
        }
    }
    
    public double finalizeSale(String delivery) {
        // Entering data into Orders
        System.out.println("Finalizing sale...");
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
            
            for(Map.Entry<String, Integer> electronic : items.entrySet()){
                query = "INSERT INTO orderDetails (orderNumber, productName, quantity, priceEach) VALUES (?,?,?,?)";
                PreparedStatement orderDetails = conn.prepareStatement(query);
                orderDetails.setInt(1, ORDERNUMBER);
                orderDetails.setString(2, electronic.getKey());
                orderDetails.setInt(3, electronic.getValue());
                orderDetails.setDouble(4, getCost(electronic.getKey()));
            }
            
            // To calculate total due.
            query = "SELECT SUM(quantity * priceEach) AS total FROM orderDetails WHERE orderNumber = " + ORDERNUMBER + " GROUP BY orderNumber";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                this.totalDue = rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("ERROR in finalizing sale.");
            e.printStackTrace();
        }
        return this.totalDue;
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
/*
 String orderSummary = "";//string for all sales
        for(String o : invoiceState) {
            if(o.equals("Open")){
               orderSummary += o;
               orderSummary += "\n"; 
            }    
        }
        return orderSummary;
*/