package main;


import java.sql.Date;
import java.sql.Time;
import java.util.Scanner;

public class Menu {
    

    public Menu (){}
    void printMenu(){
        System.out.println("Please select an option.\n");
        System.out.println("Main Menu");
        String menu =    "1. New Sale\n"
                        + "2. Add Customer\n"
                        + "3. Add Employee\n"
                        + "4. View/Edit Items\n"
                        + "5. View Employee Info\n"
                        + "6. View Report\n"
                        + "7. Make Payment\n"
                        + "8. Exit\n";
        System.out.print(menu + "\n>> "); 
    }
    
    void makePayment(){
        Scanner console = new Scanner(System.in);
        System.out.println("Please enter all fields to make a payment:");
        
        System.out.print("Customer Number: ");
        String customerNumber = console.nextLine();
        int i_customerID = Integer.parseInt(customerNumber);
        System.out.println("Customer Account Info:");
        String query = "SELECT orderNumber, total, pmtreceived, balance FROM customers "
                + "INNER JOIN orders USING (firstName, lastName) INNER JOIN report "
                + "USING (orderNumber) WHERE cid = " + i_customerID;
        Invoice i = new Invoice();
        System.out.println(i.getTable(query));
        
        System.out.print("Order Number: ");
        String orderNumber = console.nextLine();
        int i_orderID = Integer.parseInt(orderNumber);
        
        System.out.println("Date (YYYY-MM-DD): ");
        String date = console.nextLine();
        
        System.out.println("Amount: ");
        String amount = console.nextLine();
        double d_amount = Double.parseDouble(amount);
      
        Payments p = new Payments(i_customerID,i_orderID,date,d_amount);
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
        while (!option.equals("3")){
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
        
        // Adding sales person
        System.out.print("Enter Salesperson's First Name: ");
        String eFirstName = console.nextLine();
        System.out.print("Enter Salesperson's Last Name: ");
        String eLastName = console.nextLine();
        int eID = 0;
        SalesPerson sales = new SalesPerson();
        boolean isSPValid = sales.isValidSalesperson(eFirstName, eLastName);
        if (!isSPValid) {
            System.out.println("Salesperson not found. ");
            System.out.println("Exiting sale.  Please try again.");
            return;
        } else {
            eID = sales.getID(eFirstName, eLastName);
            System.out.println("Attached Salesperson.");
        }
        //Starting sale prompt
        Invoice sale = new Invoice(today, time, firstName, lastName, eID);
        sale.addNewInvoice();
        
    }
    
    void displayReports() {
        Scanner in = new Scanner(System.in);
        Invoice invoice = new Invoice();
        String input = "";
        System.out.println("Select Report to View:");
        System.out.println("1. Open Invoices");
        System.out.println("2. Close Invoices");
        System.out.print(">> ");
        input = in.nextLine();
        if (input.equals("1")) {
            // open invoice
            String query = "SELECT * FROM report WHERE balance > 0"; // report is a view table
            System.out.println(invoice.getTable(query));
        } else if (input.equals("2")) {
            // close invoice
            String query = "SELECT * FROM report WHERE balance = 0";
            System.out.println(invoice.getTable(query));
        } else {
            System.out.println("Not a vaild input.  Please try again.");
        }
        
    }
    
    void displayItemSubMenu() {
        Scanner in = new Scanner(System.in);
        System.out.println("View Item Menu");
        String menu =    "1. Display Items\n"
                       + "2. Display Items fewer than 5 in inventory\n"
                       + "3. Add Item Quantity"
                       + "4. Remove Item Quantity\n"
                       + "5. Change Item's Quantity"
                       + "6. Change Item's Selling Price\n"
                       + "7. Change Item's Cost Price"
                       + "8. Exit";
        System.out.println(menu);
        String option = in.nextLine();
        Items item = new Items();
        while (option != "8")
        {
            switch (option) 
            {
                case "1" :
                	//display
                	break;
                case "2":
                	//display less than 5
                	break;
                case "3":
                	System.out.println("Please type the name of the item you wish to add the quantity amount");
                    
                	break;
                case "4":
                	//remove item
                	break;
                case "5":
                	//change item's quantity
                	break;
                case "6":
                	//change item selling price
                	break;
                case "7":
                	//change item cost price
                	break;
            }
            System.out.println(menu);
            option = in.nextLine();
        }
    }
}
