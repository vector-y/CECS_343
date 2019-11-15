/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author mou lue huang
 */
public final class SalesPerson {
    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    //Use Case 15
    //create a new salesperson to add to database
    static int salary = 50000;
    public SalesPerson(String firstName, String lastName, String phoneNum, String address, int commission){
        int eID = getMaxID();
        insertSPToDatabase(eID,firstName,lastName,phoneNum,address,salary,commission);
        //check all Salespersons input and validate that primary key does not exist yet
        
    }
    public SalesPerson(){
    }
    //Use Case 14
    //display totalcomission
    private void displayTotalCommission(String name){
        //query to check for the name
        
    }
    //Use Case 9
    //display total Sales
    private void displayTotalSales(String name){
        //query to check for the name
        
    }
    
    
    //display all Sales person
    void displayAllSalesPerson(){
        //query to print all salesperson
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String getMaxSQL = "SELECT * FROM EMPLOYEES";
            ResultSet rs = null;
            rs = stmt.executeQuery(getMaxSQL);
            
            String salesPerson_display_format = "%-25s%-25s%-25s%-25s%-25s%-25s%-25s\n";
            while (rs.next()) {
                int employeeID = rs.getInt("EID");
                String firstName = rs.getString("FIRSTNAME");
                String lastName = rs.getString("LASTNAME");
                String phoneNum = rs.getString("PHONENUMBER");
                String address = rs.getString("ADDRESS");
                int salary = rs.getInt("SALARY");
                int commissionrate = rs.getInt("COMMISSIONRATE");
                System.out.printf(salesPerson_display_format, employeeID, dispNull(firstName), dispNull(lastName), dispNull(phoneNum), dispNull(address),salary,commissionrate/100);
                            
            }
        }
        catch(SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    

    //query to add new salesperson into database
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

    //get the maxID 
    private int getMaxID() {
        Statement stmt;
        int maxID = 0;
        try {
            
            stmt = conn.createStatement();
            String getMaxSQL = "SELECT MAX(EID) AS MAXID FROM EMPLOYEES";
            ResultSet rs = null;rs = stmt.executeQuery(getMaxSQL);
            
            while (rs.next()) {
                maxID = rs.getInt("MAXID");
            }
        }
        catch(SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        return maxID+1;
        
    }
    //if null just change string to N/A
    String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
}

     