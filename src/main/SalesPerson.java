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
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author mou lue huang
 */
public final class SalesPerson {
    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    private int eID;
    
    //Use Case 15
    //create a new salesperson to add to database
    static int salary = 50000;
    public SalesPerson(String firstName, String lastName, String phoneNum, String address, int commission){
        setEID();
        insertSPToDatabase(firstName,lastName,phoneNum,address,salary,commission);
        //check all Salespersons input and validate that primary key does not exist yet
        
    }
    public SalesPerson(){
    }
    
    //Use Case 14
    //display totalcomission
    void displayTotalCommission(String firstName, String lastName){
        Statement stmt = null;
        ResultSet rs = null;
        //query to check for the name
        boolean valid = checkSalesPerson(firstName,lastName);
        String salesPerson_display_format = "%-25s%-25s%-25s%-25s\n";
        System.out.printf(salesPerson_display_format, "firstName", "lastName","commission rate","totalcommission");
        try{
            if (valid){
                stmt = conn.createStatement();
                String salesPersonSQL = "SELECT EID FIRSTNAME, LASTNAME, COMMISSIONRATE,TOTALCOMMISSION FROM EMPLOYEES WHERE FIRSTNAME = '" + firstName + "' AND LASTNAME = '" + lastName + "'" ;
                rs = stmt.executeQuery(salesPersonSQL);
                
                while (rs.next()){
                    String first_name = rs.getString("FIRSTNAME");
                    String last_name = rs.getString("LASTNAME");
                    int commissionrate = rs.getInt("COMMISSIONRATE");
                    int totalcommission = rs.getInt("TOTALCOMMISSION");
                    System.out.printf(salesPerson_display_format, dispNull(firstName), dispNull(lastName),commissionrate + "%","$"+totalcommission);
                }
                rs.close();
                stmt.close();
            }
            else{
                System.out.println("No valid sales person in the database");
            }
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        
    }
    //Use Case 9
    //display total Sales
    void displayTotalSales(String firstName, String lastName){
        //query to check for the name
        int ID = getID(firstName,lastName);
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            String salesPersonSQL = "SELECT COUNT(salesRepID) AS totalSales FROM Orders WHERE salesRepID = " + ID;
            //System.out.println(salesPersonSQL);
            rs = stmt.executeQuery(salesPersonSQL);
            
            //loop through database and see if the user input's salesperson's name exists or not
            //if exists, set the flag to be true
            while (rs.next()) {
               int totalSales = rs.getInt("totalSales");
               System.out.println(firstName + " " + lastName + " has made " + totalSales + " total Sales");
            }
            rs.close();
            stmt.close();
            
        } 
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    public int getID(String firstName, String lastName){
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            String salesPersonSQL = "SELECT * FROM EMPLOYEES";
            rs = stmt.executeQuery(salesPersonSQL);
            
            //loop through database and see if the user input's salesperson's name exists or not
            //if exists, set the flag to be true
            while (rs.next()) {
                int employeeID = rs.getInt("EID");
                String first_name = rs.getString("FIRSTNAME");
                String last_name = rs.getString("LASTNAME");
                //i_first/lastname is the name we can return true
                if (firstName.equals(first_name) && lastName.equals(last_name)) {
                    return employeeID;
                }
            }
            rs.close();
            stmt.close();
            return -1;
            
        } 
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        return -1;
    }
    
    public void setEID() {
        int eID = 0;
        String query = "SELECT COALESCE(number, 0) eID FROM \n"
                + "(SELECT MAX(eID) number from employees) x";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                eID = rs.getInt("eID");
            }
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        if (eID == 0) {
            this.eID = 1;
        } else {
            this.eID = eID + 1;
        }
    }
    boolean checkSalesPerson(String i_firstName, String i_lastName){
        Statement stmt = null;
        ResultSet rs = null;
        System.out.println(i_firstName);
        
        System.out.println(i_lastName);
        try {
            stmt = conn.createStatement();
            String salesPersonSQL = "SELECT * FROM EMPLOYEES";
            //initlize for salesperson flag
            boolean s_name_flag = false;
            rs = stmt.executeQuery(salesPersonSQL);
            
            //loop through database and see if the user input's salesperson's name exists or not
            //if exists, set the flag to be true
            while (rs.next()) {
                String first_name = rs.getString("FIRSTNAME");
                String last_name = rs.getString("LASTNAME");
                //i_first/lastname is the name we can return true
                if (i_firstName.equals(first_name) && i_lastName.equals(last_name)) {
                    s_name_flag  = true;
                }
            }
            rs.close();
            stmt.close();
            return s_name_flag;
            
        } 
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
        return false;
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
            
            String salesPerson_display_format = "%-25s%-25s%-25s%-25s%-25s%-25s%-25s%-25s\n";
            System.out.printf(salesPerson_display_format, "employeeID", "firstName", "lastName", "phoneNum", "address","salary","commissionrate","totalcommission");

            while (rs.next()) {
                int employeeID = rs.getInt("EID");
                String firstName = rs.getString("FIRSTNAME");
                String lastName = rs.getString("LASTNAME");
                String phoneNum = rs.getString("PHONENUMBER");
                String address = rs.getString("ADDRESS");
                int salary = rs.getInt("SALARY");
                int commissionrate = rs.getInt("COMMISSIONRATE");
                int totalcommission = rs.getInt("TOTALCOMMISSION");
                System.out.printf(salesPerson_display_format, employeeID, dispNull(firstName), dispNull(lastName), dispNull(phoneNum), dispNull(address),salary,commissionrate/100,totalcommission);
                            
            }
            rs.close();
            stmt.close();
        }
        catch(SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    
    }
    

    //query to add new salesperson into database
    void insertSPToDatabase(String firstName, String lastName, String phoneNum, String address, int salary, int commission) {
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            String insertNewSPSQL = String.format("INSERT INTO Employees(EID, FIRSTNAME,LASTNAME,PHONENUMBER,ADDRESS,SALARY,COMMISSIONRATE,TOTALCOMMISSION) values (%d,'%s','%s','%s','%s',%d,%d,%d)", eID, firstName,lastName,phoneNum,address,salary,commission,0);
            //System.out.println(insertNewSPSQL);
            System.out.println("Employee added to database \n");
            stmt.executeUpdate(insertNewSPSQL);
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }

    //if null just change string to N/A
    String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    
    public boolean isValidSalesperson(String fName, String lName) {
        String query = "SELECT firstName, lastName FROM employees WHERE firstName = '" + fName + "' AND lastName = '" + lName + "'";
        String firstName = "";
        String lastName = "";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                firstName = rs.getString("firstName");
                lastName = rs.getString("lastName");
            }
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return firstName.equals(fName) && lastName.equals(lName);
    }
}

     
