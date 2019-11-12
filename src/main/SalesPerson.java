/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
/**
 *
 * @author mou lue huang
 */
public class SalesPerson {
    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    //Use Case 15
    //create a new salesperson to add to database
    public SalesPerson(String name,String email, float comission_percent, int totalSales) {
        /**while(name.length > "XXX"){
            System.out.println("Please insert a name that is less than");
        }**/
        while(comission_percent < 0.0){
            System.out.println("Please insert a comission_person that is greater than 0.0");
        }
        String[] name_split = name.split(" ");
        String firstName = name_split[0];
        String lastName = name_split[1];
        
        
        //check all Salespersons input and validate that primary key does not exist yet
        
        //query to add to salesperson database
    }
    public SalesPerson(){
        
    }
    //Use Case 14
    //display totalcomission for each salesperson
    private void displayTotalComission(String name){
        //query to check for the name
        
    }
    //Use Case 9
    //display total sales and total comission for each salesperson
    private void displayAllSalesPerson(){
        //query to print all salesperson
    }
    
    private void deleteSalesPerson(){
        //asks for input of what salesperson you would like to delete
        //query to delete the salesperson
    }
   
}
