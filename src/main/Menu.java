/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;


import java.sql.Date;
import java.sql.Time;
import java.util.Scanner;

/**
 *
 * @author blahb
 */
public class Menu {
    

    public Menu (){}
    void printMenu(){
        System.out.println("Please select an option.\n");
        System.out.println("Main Menu");
        String menu =    "1. New Sale\n"
                        + "2. Add Customer\n"
                        + "3. Add Employee\n"
                        + "4. Edit Items\n"
                        + "5. View Employee Info\n"
                        + "6. View Report\n"
                        + "7. Exit\n";
        System.out.print(menu + "\n>> "); 
    }
    void displayEmployeeInfoMenu(){
        
        Scanner in = new Scanner(System.in);
        System.out.println("View Employee Info Menu");
        String menu =    "1. Display Total Sales\n"
                       + "2. Display Total Commission\n"
                       + "3. Exit";
        System.out.println(menu);
        String option = in.nextLine();
        
        String firstName = "";
        String lastName = "";
        SalesPerson sp = new SalesPerson();
        while (option != "3"){
            switch (option) {
            // Display Total Sales
                case "1":
                    firstName = getFirstName();
                    lastName = getLastName();
                    sp.displayTotalSales(firstName, lastName);
                    break;
            // Add customer
                case "2":
                    firstName = getFirstName();
                    lastName = getLastName();
                    sp.displayTotalCommission(firstName, lastName);
                    break;
                   
            }
            System.out.println(menu);
            option = in.nextLine();
        }
    }
    private String getFirstName(){
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your sale person's first name(max: 20 characters)");
        String firstName = input.nextLine();
        while (firstName.length() > 20) {
            System.out.println("Please enter your sale person's first name(max: 20 characters)");
            firstName = input.nextLine();
        }
        return firstName;
    }
    private String getLastName(){
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your sale person's last name(max: 20 characters)");
        String lastName = input.nextLine();
        while (lastName.length() > 20) {
            System.out.println("Please enter your sale person's last name(max: 20 characters)");
            lastName = input.nextLine();
        }
        return lastName;
    }
    void addSalesPerson(){
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter all fields for new customer:");

        System.out.print("First Name: ");
        String firstName = input.nextLine();
        System.out.print("Last Name: ");
        String lastName = input.nextLine();
        // Verify if valid sales person is valid
        System.out.print("Phone Number: ");
        String phoneNum = input.nextLine();
        System.out.print("Address: ");
        String address = input.nextLine();
        System.out.print("Commission Rate: ");
        // verify if commision rate is valid. 
        while (!input.hasNextInt()) 
        {        
            input.next(); 
        System.out.println("Please enter valid sale person's commission rate");
        }
        int commission = input.nextInt(); 
        SalesPerson sp = new SalesPerson(firstName,lastName,phoneNum,address,commission);
    }
    
    void addCustomer() {
        Scanner console = new Scanner(System.in);
        System.out.println("Please enter all fields for new customer:");
        System.out.print("First Name: ");
        String firstName = console.nextLine();
        System.out.print("Last Name: ");
        String lastName = console.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = console.nextLine();
        System.out.print("Mail Address: ");
        String mail = console.nextLine();
        System.out.print("Delivery Address: ");
        String delivery = "";
        if (console.hasNextLine()) {
            delivery = console.nextLine();
        }
        Customer cust = new Customer(firstName, lastName, phoneNumber, mail, delivery);
    }
    
    void newSale() {
        Scanner console = new Scanner(System.in);
        Boolean isValid = true;            
        Customer c = new Customer();
        System.out.print("Enter Customer's First Name: ");
        String firstName = console.nextLine();
        System.out.print("Enter Customer's Last Name: ");
        String lastName = console.nextLine();

        // Verifying if customer exists
        isValid = c.isValidCustomer(firstName, lastName);
        if (!isValid) {
            System.out.println("Customer not found. ");
            System.out.println("Exiting sale.  Please add new customer first.");
            return;
        } else {
            System.out.println("Found Customer.");
        }
        long millis=System.currentTimeMillis();  
        Date today = new Date(millis);
        Time time = new java.sql.Time(today.getTime());

        
        //Starting sale prompt
        Invoice sale = new Invoice(today, time, firstName, lastName);
        sale.addNewInvoice();
        
    }
    
}
