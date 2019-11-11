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

        System.out.println(">> Welcome to initial setup!  Please enter the password you would like to set as. <<");
        
        System.out.println("Name of the database (not the user account): ");
        MASTERPASSWORD = in.nextLine().trim();
        
        System.out.println("Great! Let's log you on...");
        
        DBConnect db = new DBConnect();
        Connection conn = db.connect();
        
        System.out.println("-------------------------------------------------------");
        System.out.println("Main Menu");
        String menu =     "1. New Sale\n"
                        + "2. Add Customer\n"
                        + "3. Edit Customer\n"
                        + "4. Add Employee\n"
                        + "5. Edit Employee\n"
                        + "6. View Report\n"
                        + "7. Exit\n";
        
        System.out.println(menu); 
        String option = in.nextLine();
        while (option != "7") {
            if (option.equals("1")) {
                // New Sale
            } else if (option.equals("2")) {
                // Add customer
            } else if (option.equals("3")) {
                // Edit Customer
            } else if (option.equals("4")) {
                // Add employee
            } else if (option.equals("5")) {
                // Edit Employee
            } else if (option.equals("6")) {
                // List View Report options
                // while loop
            } else if (option.equals("7")) {
                break;
            }
        }        
    }
}//end FirstExample}

