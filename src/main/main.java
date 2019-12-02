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
        
        System.out.println(">> Welcome to initial setup!  Please enter the password you would like to set as. <<");
        System.out.print(">> ");
        MASTERPASSWORD = in.nextLine().trim();
        
        System.out.println("Great! Let's log you on...\n\n");
        System.out.println("----------------------------------------------------------");
        Menu menu = new Menu();
        menu.printMenu();
        String option = in.nextLine();
        
        while (option != "7"){
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
            // Add Items
                case "4":
                    menu.displayItemSubMenu();
                    break;
            // View Employee Information
                case "5":
                    menu.displayEmployeeInfoMenu();
                    break;
             //View Report   
                case "6":
                    break;   
                 
                default:
                    break;
            }
            System.out.println("\n----------------------------------------------------------\n");
            menu.printMenu();
            option = in.nextLine();
        }        
    }
}//end FirstExample}

