/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.sql.*;
import java.util.Scanner;


/**
 * user: cecs343 password: cecs343
 */
public class main {
    
    static String MASTERPASSWORD;
    
    static DBConnect db = new DBConnect();
    static Connection conn = db.connect();

    /**
 * Takes the input string and outputs "N/A" if the string is empty or null.
 * @param input The string to be mapped.
 * @return  Either the input string or "N/A" as appropriate.
 */
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
     public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (!isSetup()) {
            System.out.println(">> Welcome to initial setup!  Please enter the password you would like to set as. <<");
            System.out.print(">> ");
            MASTERPASSWORD = in.nextLine().trim();
            setPassword(MASTERPASSWORD);
        }
        String password = "";
        do {        
            System.out.print("Please enter password: ");
            password = in.nextLine();
        } while (!isValid(password));
        
        System.out.println("Great! Let's log you on...\n\n");
        System.out.println("----------------------------------------------------------");
        Menu menu = new Menu();
        menu.printMenu();
        String option = in.nextLine();
        
        while (!option.equals("6")){
            System.out.println("Selected Option " + option);
            System.out.println("----------------------------------------------------------");
            switch (option) {
            // New Sale
                case "1":
                    menu.newSale();
                    break;
            // Add customer
                case "2":
                    menu.addCustomer();
                    break;
            // Add employee
                case "3":
                    menu.addSalesPerson();
                    break;
            // View Employee Information
                case "4":
                    menu.displayEmployeeInfoMenu();
                    break;
            // View Report
                case "5":
                    break;
                default:
                    break;
            }
            System.out.println("\n----------------------------------------------------------\n");
            menu.printMenu();
            option = in.nextLine();
        }        
    }
    

    private static boolean isSetup() {
        Statement stmt = null;
        String query = "SELECT password, master FROM passwords";  
        String password = "";
        String master = "";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                password = rs.getString("password");
                master = rs.getString("master");
                if (password.length() > 0 && master.equals("Y")) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Verifying if password exists failed.");
            System.out.println("Please restart program");
        }
        return false;
    }
    private static boolean setPassword(String password) {
        try {
            if (!password.isEmpty()) {
                String query = "INSERT INTO passwords (password, master) VALUES (?,?)";
                PreparedStatement setup = conn.prepareStatement(query);
                setup.setString(1, password); //set values for newBook
                setup.setString(2, "Y");
                setup.executeUpdate();
                System.out.println("\nPassword Is Set.");
                return true;
            } else {
                System.out.println("The two password entries are different. Please try again.");
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static boolean isValid(String password) {
        Statement stmt = null;
        String query = "SELECT password, master FROM passwords";  
        String master = "Y";
        String dbPassword = "";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                dbPassword = rs.getString("password");
                master = rs.getString("master");
                if (dbPassword.equals(password) && master.equals("Y")) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Failed to verify password.");
            System.out.println("Please try again.");
            return false;
        }
        System.out.println("Invalid Password.  Plesae try again.");
        return false;
    }
}

