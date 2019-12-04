package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Items {
    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    
    private String name;
    private int quantity;
    private float sellingPrice;
    private float costPrice;
    private int quantitySold;
    private float priceSold;
    private float profit;
    
    
    public Items(String itemName, int itemQuantity, float itemSellingPrice, float itemCostPrice) {
    	
    	name = itemName;
    	quantity = itemQuantity;
    	sellingPrice = itemSellingPrice;
    	costPrice = itemCostPrice;
    	quantitySold = 0;
    	priceSold = 0;
    	profit = 0;
    	
    }
    
    public Items() {
    
    }
    
    public void addQuantity(int productID, int number)
    {
    	Statement stmt = null;
        ResultSet rs = null;
        
        stmt = conn.createStatement();
        String productSQL = "SELECT QUANTITYINSTOCK FROM PRODUCTS WHERE EID =" + productID;
        rs = stmt.executeQuery(productSQL);
        quantity = rs.getInt("QUANTITYINSTOCK");
    	quantity = quantity + number;
    	conn.close();
    	String query = "UPDATE PRODUCTS SET QUANTITYINSTOCK = ? WHERE EID =" + productID;
    	PreparedStatement proSql = conn.prepareStatement(query);
    	proSql.setInt(1, quantity);
    	proSql.executeUpdate();
    	conn.close();
    }
    
    public void changeQuantity(int productID, int number)
    {
    	Statement stmt = null;
        ResultSet rs = null;
        
        stmt = conn.createStatement();
        String productSQL = "SELECT QUANTITYINSTOCK FROM PRODUCTS WHERE EID =" + productID;
        rs = stmt.executeQuery(productSQL);
    	quantity = number;
    	conn.close();
    	String query = "UPDATE PRODUCTS SET QUANTITYINSTOCK = ? WHERE EID =" + productID;
    	PreparedStatement proSql = conn.prepareStatement(query);
    	proSql.setInt(1, quantity);
    	proSql.executeUpdate();
    	conn.close();
    }
    
    public float getQuantity(int productID)
    {
    	return quantity;
    }
    
    public void addQuantitySold(int productID, int number)
    {
    	quantitySold = quantitySold + number;
    }
    
    public void addPriceSold()
    {
    	priceSold = priceSold + sellingPrice;
    }
    public int getTotalSold()
    {
    	return quantitySold;
    }
    
    public float getPriceSold()
    {
    	return priceSold;
    }
    
    public void setName(String newName)
    {
    	name = newName;
    }
    
    public String getName()
    {
    	return name;
    }
    
    
    public void setCostPrice(float newPrice)
    {
    	costPrice = newPrice;
    }
    
    public float getCostPrice()
    {
    	return costPrice;
    }
    
    public void setSellingPrice(float newPrice)
    {
    	sellingPrice = newPrice;
    }
    
    public float getSellingPrice()
    {
    	return sellingPrice;
    }
    
    public void calcProfit()
    {
    	profit = profit + (sellingPrice - costPrice);
    }
    
    public float getProfit()
    {
    	return profit;
    }
    
    public void displayProfitList()
    {
    	
    	
    }
    
    void insertSPToDatabase() {
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            String insertNewSPSQL = String.format("INSERT INTO products(productName,quantityInStock,MSRP,buyPrice, QUANTITYSOLD, PRICESOLD) values (%s,'%d','%d','%d','%d','%d')",name, quantity, sellingPrice, costPrice, quantitySold, priceSold);
            System.out.println(insertNewSPSQL);
            stmt.executeUpdate(insertNewSPSQL);
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    		
    
  }
