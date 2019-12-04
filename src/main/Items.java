package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class Items {
    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    
    private String name;
    private float quantity;
    private float sellingPrice;
    private float costPrice;
    private int quantitySold;
    private float priceSold;
    private float profit;
    
    
    public Items(String itemName, float itemQuantity, float itemSellingPrice, float itemCostPrice) {
    	
    	name = itemName;
    	quantity = itemQuantity;
    	sellingPrice = itemSellingPrice;
    	costPrice = itemCostPrice;
    	quantitySold = 0;
    	priceSold = 0;
    	profit = 0;
    	insertSPToDatabase();
    }
    
    public Items() {
    
    }
    
    public void addQuantity(float number)
    {
    	quantity = quantity + number;
    }
    
    public void changeQuantity(float number)
    {
    	quantity = number;
    }
    
    public float getQuantity()
    {
    	return quantity;
    }
    
    public void addQuantitySold(int number)
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
    
    
    void insertSPToDatabase() {
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            String insertNewSPSQL = String.format("INSERT INTO Items(NAME,ITEMQUANTITY,MSRP,COSTPRICE, QUANTITYSOLD, PRICESOLD) values (%s,'%d','%d','%d','%d','%d')",name, quantity, sellingPrice, costPrice, quantitySold, priceSold);
            System.out.println(insertNewSPSQL);
            stmt.executeUpdate(insertNewSPSQL);
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    		
    
  }
