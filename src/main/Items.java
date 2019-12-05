package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Items {
    DBConnect dbConnection = new DBConnect();
    Connection conn = dbConnection.connect();
    
    public int PRODUCTNUMBER = 0;
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
    	insertSPToDatabase();
    	
    }
    
    public Items() {
    
    }
    
    public void addQuantity(int productID, int number)
    {
    	String query = "SELECT productName, MSRP FROM products";
        
    	Statement stmt = null;
        ResultSet rs = null;
        
        try {
        stmt = conn.createStatement();
        String productSQL = "SELECT QUANTITYINSTOCK FROM PRODUCTS WHERE EID =" + productID;
        rs = stmt.executeQuery(productSQL);
        quantity = rs.getInt("QUANTITYINSTOCK");
    	quantity = quantity + number;
    	
    	query = "UPDATE PRODUCTS SET QUANTITYINSTOCK = ? WHERE EID =" + productID;
    	PreparedStatement proSql = conn.prepareStatement(query);
    	proSql.setInt(1, quantity);
    	proSql.executeUpdate();
    	
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
    }
    
    public void changeQuantity(int productID, int number)
    {  
        try {
        Statement stmt = conn.createStatement();
	ResultSet rs = stmt.executeQuery("SELECT QUANTITYINSTOCK FROM PRODUCTS WHERE productNumber =" + productID);
			
	
    	quantity = number;
    	String query = "UPDATE PRODUCTS SET QUANTITYINSTOCK = ? WHERE productNumber =" + productID;
    	PreparedStatement proSql = conn.prepareStatement(query);
    	proSql.setInt(1, quantity);
    	proSql.executeUpdate();
    	
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
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
    
     public String getName(int option) {
        String name = "";
        int count = 1;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT productName, msrp FROM products ORDER BY productName");
            
            while (rs.next()) { 
               if(count == option) {
                   name = rs.getString("productName");
                   break;
               }
               count += 1;
            }
    	} catch(SQLException e) {
            System.out.println("SQL exception occured" + e);
        }
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
    
    /**
     * To get the cost of the item that is selected from the getItemsForSale list.
     * @param option
     * @return 
     */
    public double getCost(int option) {
        double cost = 0;
        int count = 1;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT productName, msrp FROM products ORDER BY productName");
            
            while (rs.next()) { 
               count += 1;
               if(count == option * 2) {
                   cost = rs.getDouble("msrp");
                   break;
               }
            }
    	} catch(SQLException e) {
            System.out.println("SQL exception occured" + e);
        }
        return cost;
    }
        /**
     * To get the cost of the item by product name.
     * @param option
     * @return 
     */
    public double getCost(String name) {
        double cost = 0;
        String productName = "";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT productName, msrp FROM products ORDER BY productName");
            
            while (rs.next()) { 
                productName = rs.getString("productName");
                if(productName.equals(name)) {
                   cost = rs.getDouble("msrp");
                   break;
                }
            }
    	} catch(SQLException e) {
            System.out.println("SQL exception occured" + e);
        }
        return cost;
    }
    
    /**
     * To get the items in inventory that is available for sale. 
     * @return 
     */
    public String getItemsForSale() {
        String query = "SELECT productName, msrp FROM products ORDER BY productName";
        return getTable(query);
    }
    
    public void displayProfitList()
    {
    	try {
	    	Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM products WHERE quantityInStock < 5");
	        System.out.format("%4s%32s%32s%32s%32s%32s","ID","Name","Quantity","MSRP","CostPrice","Profit\n");
            
            while (rs.next()) {
               int did = rs.getInt("productNumber");
               String dname = rs.getString("productName");
               int dquantity = rs.getInt("quantityInStock");
               float dMSRP = rs.getFloat("MSRP");
               float dcostPrice = rs.getFloat("buyPrice");
               float dprofit = rs.getFloat("profit");
               System.out.format("%4s%32s%32s%32s%32s%32s", did,dname,dquantity,dMSRP,dcostPrice,dprofit+"\n");
               
            }
    	} catch(SQLException e) {
            System.out.println("SQL exception occured" + e);
        }
    }
    
    public void displayIDList() 
    {
        try {
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");
            System.out.format("%4s%32s%32s%32s%32s%32s","ID","Name","Quantity","MSRP","CostPrice","Profit\n");
            
            while (rs.next()) {
               int did = rs.getInt("productNumber");
               String dname = rs.getString("productName");
               int dquantity = rs.getInt("quantityInStock");
               float dMSRP = rs.getFloat("MSRP");
               float dcostPrice = rs.getFloat("buyPrice");
               float dprofit = rs.getFloat("profit");
               System.out.format("%4s%32s%32s%32s%32s%32s", did,dname,dquantity,dMSRP,dcostPrice,dprofit+"\n");
               
            }
         } catch(SQLException e) {
            System.out.println("SQL exception occured" + e);
         }
    }
    
    private void incrNum() {
        int number = 0;
        String query = "SELECT COALESCE(number, 0) productNumber FROM \n"
                + "(SELECT MAX(productNumber) number from products) x";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                number = rs.getInt("productNumber");
            }
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        if (number == 0) {
            this.PRODUCTNUMBER = 1;
        } else {
            this.PRODUCTNUMBER = number + 1;
        }
    }
    
    void insertSPToDatabase() {
    	incrNum();
        try
        {
            String query = String.format("INSERT INTO products(productNumber,productName,quantityInStock,MSRP,buyPrice, profit) VALUES (?,?,?,?,?,?)");
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, PRODUCTNUMBER);
            stmt.setString(2, name);
            stmt.setInt(3, quantity);
            stmt.setFloat(4, sellingPrice);
            stmt.setFloat(5, costPrice);
            stmt.setFloat(6, profit);
            stmt.executeUpdate();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    /**
     * Helper function to create menu for the Items for sale.
     * @param query - The query to create the table with.
     * @return table as a String
     */
    public String getTable(String query) {
        Statement stmt = null;
        String table = "";
        String label = "";
        int columnSize = 0;
        int column = 0;
        int count = 1;
        try {
            // To list the column labels.
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            column = rsmd.getColumnCount();
            
            for (int i = 1; i <= column; i++) {
                label = rsmd.getColumnName(i);
                columnSize = rsmd.getColumnDisplaySize(i); // Each column may have a different length
                table += String.format("%-" + columnSize + "s", label); // To format by column size from left.
            }
            table += "\n";
            // To store all the rows with format. 
            if (!rs.next()) {
                return "No Items in Inventory to Sell.  Please add items.";
            } else {
                do {
                    table += count + ") ";
                    for (int i = 1; i <= column; i++) {
                        columnSize = rsmd.getColumnDisplaySize(i);
                        table += String.format("%-" + columnSize + "s", rs.getString(i));
                    }
                    table += "\n";
                    count += 1;
                } while (rs.next());
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return table;
    }
  }
