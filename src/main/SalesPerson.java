/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author mou lue huang
 */
public class SalesPerson {
    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    //Use Case 15
    //create a new salesperson to add to database
    static int eID = 1;
    static int salary = 50000;
    public SalesPerson(String firstName, String lastName, String phoneNum, String address, int commission){
        insertSPToDatabase(eID,firstName,lastName,phoneNum,address,salary,commission);
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

    void insertSPToDatabase(int eID, String firstName, String lastName, String phoneNum, String address, int salary, int commission) {
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            String insertNewSPSQL = String.format("INSERT INTO Employees(EID,FIRSTNAME,LASTNAME,PHONENUMBER,ADDRESS,SALARY,COMMISSIONRATE) values (%d,'%s','%s','%s','%s',%d,%d)", eID,firstName,lastName,phoneNum,address,salary,commission);
            System.out.println(insertNewSPSQL);
            stmt.executeUpdate(insertNewSPSQL);
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
}
