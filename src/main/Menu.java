/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.Scanner;

/**
 *
 * @author blahb
 */
public class Menu {
    public Menu (){}
    void printMenu(){
         System.out.println("Main Menu");
         String menu =    "1. New Sale\n"
                        + "2. Add Customer\n"
                        + "3. Edit Customer\n"
                        + "4. Add Employee\n"
                        + "5. Edit Employee\n"
                        + "6. View Report\n"
                        + "7. Add SalesPerson\n"
                        + "8. Exit\n";
         System.out.println(menu); 
    }
    void addSalesPerson(){
        Scanner input = new Scanner(System.in);
        
        System.out.println("Please enter your sale person's first name(max: 20 characters)");
        String firstName = input.nextLine();
        while (firstName.length() > 20) {
            System.out.println("Please enter your sale person's first name(max: 20 characters)");
            firstName= input.nextLine();
        }
        
        System.out.println("Please enter your sale person's last name(max: 20 characters)");
        String lastName = input.nextLine();
        while (lastName.length() > 20) {
            System.out.println("Please enter your sale person's last name(max: 20 characters)");
            lastName = input.nextLine();
        }
        
                    
        System.out.println("Please enter your sale person's phone Number(max: 20 characters)");
        String phoneNum = input.nextLine();
        while (phoneNum.length() > 20) {
            System.out.println("Please enter your sale person's phone Number(max: 20 characters)");
            phoneNum = input.nextLine();
        }
        
        System.out.println("Please enter your sale person's address(max: 20 characters)");
        String address = input.nextLine();
        while (address.length() > 20) {
            System.out.println("Please enter your sale person's address(max: 20 characters)");
            address = input.nextLine();
        }
        
        System.out.println("Please enter sale person's commission rate");
        while (!input.hasNextInt()) 
        {        
            input.next(); 
        System.out.println("Please enter valid sale person's commission rate");
        }
        int commission = input.nextInt(); 
        SalesPerson sp = new SalesPerson(firstName,lastName,phoneNum,address,commission);
    }
}
